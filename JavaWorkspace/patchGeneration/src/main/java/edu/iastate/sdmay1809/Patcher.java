package edu.iastate.sdmay1809;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.javatuples.Pair;

public class Patcher {
	private PatchConfig config;
	private String kernelPath;
	private String outputPath;
	private boolean debug;
	private boolean verbose;
	
	public Patcher(String configPath, String kernelPath, String outputPath, boolean debug, boolean verbose) throws Exception
	{
		this(configPath, kernelPath, outputPath, debug);
		this.verbose = verbose;
	}

	public Patcher(String configPath, String kernelPath, String outputPath, boolean debug) throws Exception
	{
		this(configPath, kernelPath, outputPath);
		this.debug = debug;
		this.verbose = false;
	}

	public Patcher(String configPath, String kernelPath, String outputPath) throws Exception
	{
		this(configPath);
		this.kernelPath = kernelPath;
		this.outputPath = outputPath;
		this.debug = false;
		this.verbose = false;
	}
	
	public Patcher(String configPath) throws Exception
	{
		config = new PatchConfig(configPath);
		this.kernelPath = "resources/";
		this.outputPath = "resources/testing/patch/real/";
		this.debug = false;
		this.verbose = false;
		
		Function.config = config;
		Macro.config = config;
	}

	public void patch() throws Exception
	{
		if (verbose)
		{
			Map<String, Boolean> map;
			
			System.out.println("Verbose output is: ON");
			
			System.out.println("Kernel Path: " + kernelPath);
			System.out.println("Output Path: " + outputPath);
			if (kernelPath.equals(outputPath)) System.out.println("Kernel path and output path are the same. Original kernel will be overwritten.");
			System.out.println("Debugging: " + (debug ? "ON" : "OFF"));
			
			System.out.println("\nMutex Files to be Read");
			for (String s : config.getPaths(PatchConfig.MUTEX_PATHS_TO_READ)) System.out.println("\t" + s);
			
			System.out.println("\nMutex Files to Change");
			for (String s : config.getPaths(PatchConfig.MUTEX_PATHS_TO_CHANGE)) System.out.println("\t" + s);

			System.out.println("\nSpin Files to be Read");
			for (String s : config.getPaths(PatchConfig.SPIN_PATHS_TO_READ)) System.out.println("\t" + s);

			System.out.println("\nSpin Files to Change");
			for (String s : config.getPaths(PatchConfig.SPIN_PATHS_TO_CHANGE)) System.out.println("\t" + s);

			System.out.println("\nMutex Functions to Include");
			for (Function f : config.getFunctions(PatchConfig.MUTEX_FUNCTIONS_TO_INCLUDE)) System.out.println("\t" + f);
			
			System.out.println("\nMutex Macros to Include");
			for (Macro m : config.getMacros(PatchConfig.MUTEX_MACROS_TO_INCLUDE)) System.out.println("\t" + m);

			System.out.println("\nSpin Functions to Include");
			for (Function f : config.getFunctions(PatchConfig.SPIN_FUNCTIONS_TO_INCLUDE)) System.out.println("\t" + f);

			System.out.println("\nSpin Macros to Include");
			for (Macro m : config.getMacros(PatchConfig.SPIN_MACROS_TO_INCLUDE)) System.out.println("\t" + m);
			
			System.out.println("\nMutex Function Criteria:");
			for (String s : (map = config.getCriteria(PatchConfig.MUTEX_FUNCTION_CRITERIA)).keySet()) System.out.println("\t" + s + ": " + map.get(s));
			
			System.out.println("\nMutex Macro Criteria:");
			for (String s : (map = config.getCriteria(PatchConfig.MUTEX_MACRO_CRITERIA)).keySet()) System.out.println("\t" + s + ": " + map.get(s));

			System.out.println("\nSpin Function Criteria:");
			for (String s : (map = config.getCriteria(PatchConfig.SPIN_FUNCTION_CRITERIA)).keySet()) System.out.println("\t" + s + ": " + map.get(s));

			System.out.println("\nSpin Macro Criteria:");
			for (String s : (map = config.getCriteria(PatchConfig.SPIN_MACRO_CRITERIA)).keySet()) System.out.println("\t" + s + ": " + map.get(s));
			
			System.out.println("\nAcceptable Function Return Types:");
			for (String s : config.getFunctionReturnTypes()) System.out.println("\t" + s);
		}
		
		Pair<Set<Function>, Set<Macro>> mutexLocks = generateLSAPMutexHeaderFile();
		Pair<Set<Function>, Set<Macro>> spinLocks = generateLSAPSpinHeaderFile();

		removeMutexDefinitions(mutexLocks);
		removeSpinDefinitions(spinLocks);
	}
	
	public Pair<Set<Function>, Set<Macro>> generateLSAPMutexHeaderFile() throws Exception
	{
		if (verbose) System.out.println("+--------------------+\n|    Mutex  locks    |\n+--------------------+\n");

		Set<Function> functions = new LinkedHashSet<Function>();
		Set<Macro> macros = new LinkedHashSet<Macro>();
		
		functions.addAll(config.getFunctions(PatchConfig.MUTEX_FUNCTIONS_TO_INCLUDE));
		macros.addAll(config.getMacros(PatchConfig.MUTEX_MACROS_TO_INCLUDE));

		for (String filePath : config.getPaths(PatchConfig.MUTEX_PATHS_TO_READ))
		{
			if (verbose) System.out.println("Reading \"" + filePath + "\" for functions and macros...");
			
			String fileSource;
			
			try
			{
				fileSource = String.join("\n", Files.readAllLines(Paths.get(kernelPath, filePath), Charset.forName("UTF-8")));
			}
			
			catch (IOException e)
			{
				if (debug || verbose) System.err.println("PATCHER: Unable to read file \"" + filePath + "\"! Skipping it.");
				continue;
			}
						
			Matcher matcher = Pattern.compile("(\\w+\\s*(\\*)?\\s+(\\*)?\\s*)+\\w+\\s*\\([^\\)]*\\)").matcher(fileSource);			
			while (matcher.find())
			{
				try
				{
					String functionLine = matcher.group();
					if (!functionLine.toLowerCase().contains("define") && !functionLine.toLowerCase().contains("return"))
					{
						Function f = new Function(matcher.group());
						if (Function.isLockingFunction(f, config.getCriteria(PatchConfig.MUTEX_FUNCTION_CRITERIA)) && f.toString().length() > 0)
						{
							if (verbose) System.out.println("\tFunction found in \"" + functionLine + "\"");
							functions.add(f);
						}
					}
				}
				
				catch (Exception e) 
				{
					if (debug || verbose) System.err.println("PATCHER: Attempted to make function from non-function string:\n" + matcher.group());
				}
			}
									
			matcher = Pattern.compile("#\\s*[defineDEFINE]{6}\\s+\\w+\\s*\\([^\\)]*\\)").matcher(fileSource);			
			while (matcher.find())
			{
				try
				{
					Macro m = new Macro(matcher.group());
					if (Macro.isLockingMacro(m, config.getCriteria(PatchConfig.MUTEX_MACRO_CRITERIA))) 
					{
						if (verbose) System.out.println("\tMacro found in \"" + matcher.group() + "\"");
						macros.add(m);
					}
				}
				
				catch (Exception e)
				{
					if (debug || verbose) System.err.println("PATCHER: Attempted to make macro from non-macro string:\n" + matcher.group());
				}
			}
			
		}
		
		if (verbose) System.out.println("\nSome functions and macros have the same name. Functions with the same name as a macro will not be added to the patched header file.");
		for (Iterator<Function> iter = functions.iterator(); iter.hasNext();)
		{
			Function f = iter.next();
			if (macros.contains(f))
			{
				if (verbose) System.out.println("\tRemoving function \"" + f.getName() + "\"");
				iter.remove();
			}
		}
		
		if (verbose) System.out.println("\nMapping macros to their respective function calls...");
		for (Macro m : macros)
		{
			String[] macroNameParts = m.getName().split("(?=[A-Z])|_");
			int bestMatchCount = Integer.MIN_VALUE;
			String functionName = "";
			
			for (Function f : functions)
			{
				List<String> functionNameParts = Arrays.stream(f.getName().split("(?=[A-Z])|_")).collect(Collectors.toList());
				int matchCount = 0;
				
				for (String mnp : macroNameParts)
				{
					if (functionNameParts.contains(mnp)) matchCount++;
				}
				
				if (matchCount > bestMatchCount)
				{
					bestMatchCount = matchCount;
					m.setBodyFunction(f);
					functionName = f.getName();
				}
			}
			
			if (verbose) System.out.println("\tMacro \"" + m.getName() + "\" will call function \"" + functionName + "\"");
		}
		
		if (verbose) System.out.println("\nGenerating mutex lock header file...");
		
		List<String> outputContent = new ArrayList<String>();

		outputContent.add("/**************************************************************/");
		outputContent.add("// MUTEX LOCK");
		outputContent.add("/**************************************************************/");
		outputContent.add("");
		outputContent.add("#ifndef __LINUX_L_SAP_MUTEX_H");
		outputContent.add("#define __LINUX_L_SAP_MUTEX_H");
		outputContent.add("#include <linux/spinlock_types.h>");
		outputContent.add("");
		outputContent.add("// Definition for \"mutex\" related functions with empty bodies.");

		for (Function f : functions)
		{
			outputContent.add(f.toString());
		}
		
		outputContent.add("");
		outputContent.add("// Define a macro wrapper for extra functions for query unification.");
		
		for (Macro m : macros)
		{
			outputContent.add(m.toString());
		}
		
		outputContent.add("");
		outputContent.add("#endif /* __LINUX_L_SAP_MUTEX_H */");
		outputContent.add("");
		
		if (debug || verbose)
		{
			for (String line : outputContent)
			{
				System.out.println(line);
			}
		}
		
		File directory = new File(Paths.get(outputPath, "include/linux/lsap_mutex_lock." + (debug ? "txt" : "h")).toString().replaceFirst("\\w+\\.\\w+$", ""));
		if (!directory.exists()) directory.mkdirs();
		Files.write(Paths.get(outputPath, "include/linux/lsap_mutex_lock." + (debug ? "txt" : "h")), outputContent, Charset.forName("UTF-8"));
		
		return new Pair<Set<Function>, Set<Macro>>(functions, macros);
	}
	
	public Pair<Set<Function>, Set<Macro>> generateLSAPSpinHeaderFile() throws Exception
	{
		if (verbose) System.out.println("+--------------------+\n|     Spin locks     |\n+--------------------+\n");
		
		Set<Function> functions = new LinkedHashSet<Function>();
		Set<Macro> macros = new LinkedHashSet<Macro>();
		
		functions.addAll(config.getFunctions(PatchConfig.SPIN_FUNCTIONS_TO_INCLUDE));
		macros.addAll(config.getMacros(PatchConfig.SPIN_MACROS_TO_INCLUDE));

		for (String filePath : config.getPaths(PatchConfig.SPIN_PATHS_TO_READ))
		{
			if (verbose) System.out.println("Reading \"" + filePath + "\" for functions and macros...");
			
			String fileSource;
			
			try
			{
				fileSource = String.join("\n", Files.readAllLines(Paths.get(kernelPath, filePath), Charset.forName("UTF-8")));
			}
			
			catch (IOException e)
			{
				if (debug || verbose) System.err.println("PATCHER: Unable to read file \"" + filePath + "\"! Skipping it.");
				continue;
			}
			
			Matcher matcher = Pattern.compile("(\\w+\\s*(\\*)?\\s+(\\*)?\\s*)+\\w+\\s*\\([^\\)]*\\)").matcher(fileSource);			
			while (matcher.find())
			{
				try
				{
					String functionLine = matcher.group();
					if (!functionLine.toLowerCase().contains("define") && !functionLine.toLowerCase().contains("return"))
					{
						Function f = new Function(matcher.group());
						if (Function.isLockingFunction(f, config.getCriteria(PatchConfig.SPIN_FUNCTION_CRITERIA)) && f.toString().length() > 0)
						{
							if (verbose) System.out.println("\tFunction found in \"" + functionLine + "\"");
							functions.add(f);
						}
					}
				}
				
				catch (Exception e) 
				{
					if (debug || verbose) System.err.println("PATCHER: Attempted to make function from non-function string:\n" + matcher.group());
				}
			}
			
			matcher = Pattern.compile("#\\s*[defineDEFINE]{6}\\s+\\w+\\s*\\([^\\)]*\\)").matcher(fileSource);			
			while (matcher.find())
			{
				try
				{
					Macro m = new Macro(matcher.group());
					if (Macro.isLockingMacro(m, config.getCriteria(PatchConfig.SPIN_MACRO_CRITERIA))) 
					{
						if (verbose) System.out.println("\tMacro found in \"" + matcher.group() + "\"");
						macros.add(m);
					}
				}
				
				catch (Exception e)
				{
					if (debug || verbose) System.err.println("PATCHER: Attempted to make macro from non-macro string:\n" + matcher.group());
				}
			}
		}
				
		if (verbose) System.out.println("\nSome functions and macros have the same name. Functions with the same name as a macro will not be added to the patched header file.");
		for (Iterator<Function> iter = functions.iterator(); iter.hasNext();)
		{
			Function f = iter.next();
			if (macros.contains(f))
			{
				iter.remove();
			}
		}
		
		if (verbose) System.out.println("\nMapping macros to their respective function calls...");
		for (Macro m : macros)
		{
			String[] macroNameParts = m.getName().split("(?=[A-Z])|_");
			int bestMatchCount = Integer.MIN_VALUE;
			String functionName = "";
			
			for (Function f : functions)
			{
				List<String> functionNameParts = Arrays.stream(f.getName().split("(?=[A-Z])|_")).collect(Collectors.toList());
				int matchCount = 0;
				
				for (String mnp : macroNameParts)
				{
					if (functionNameParts.contains(mnp)) matchCount++;
				}
				
				if (matchCount > bestMatchCount)
				{
					bestMatchCount = matchCount;
					m.setBodyFunction(f);
					functionName = f.getName();
				}
			}
			
			if (verbose) System.out.println("\tMacro \"" + m.getName() + "\" will call function \"" + functionName + "\"");
		}
		
		if (verbose) System.out.println("\nGenerating spin lock header file...");

		List<String> outputContent = new ArrayList<String>();

		outputContent.add("/**************************************************************/");
		outputContent.add("// SPIN LOCK");
		outputContent.add("/**************************************************************/");
		outputContent.add("");
		outputContent.add("#ifndef __LINUX_L_SAP_SPINLOCK_H");
		outputContent.add("#define __LINUX_L_SAP_SPINLOCK_H");
		outputContent.add("#include <linux/spinlock_types.h>");
		outputContent.add("");
		outputContent.add("// Definition for \"spinlock\" related functions with empty bodies.");

		for (Function f : functions)
		{
			outputContent.add(f.toString());
		}
		
		outputContent.add("");
		outputContent.add("// Define a macro wrapper for extra functions for query unification.");
		
		for (Macro m : macros)
		{
			outputContent.add(m.toString());
		}
		
		outputContent.add("");
		outputContent.add("#endif /* __LINUX_L_SAP_SPINLOCK_H */");
		outputContent.add("");
		
		if (debug || verbose)
		{
			for (String line : outputContent)
			{
				System.out.println(line);
			}
		}
		
		File directory = new File(Paths.get(outputPath, "include/linux/lsap_spinlock." + (debug ? "txt" : "h")).toString().replaceFirst("\\w+\\.\\w+$", ""));
		if (!directory.exists()) directory.mkdirs();
		Files.write(Paths.get(outputPath, "include/linux/lsap_spinlock." + (debug ? "txt" : "h")), outputContent, Charset.forName("UTF-8"));
		
		return new Pair<Set<Function>, Set<Macro>>(functions, macros);
	}
	
	public void removeMutexDefinitions(Pair<Set<Function>, Set<Macro>> locks) throws Exception
	{
		if (verbose) System.out.println("+-----------------------------+\n| Editing current mutex locks |\n+-----------------------------+\n");
		
		for (String filePath : config.getPaths(PatchConfig.MUTEX_PATHS_TO_CHANGE))
		{
			if (verbose) System.out.println("Editing file \"" + filePath + "\"...");
			
			String fileSource = "";
			String[] fileSourceSplit;
			String newSource = "";
			
			try
			{
				fileSourceSplit = Files.readAllLines(Paths.get(kernelPath, filePath)).toArray(new String[0]);
			}
			
			catch (IOException e)
			{
				if (debug || verbose) System.err.println("PATCHER: Unable to read file \"" + filePath + "\"! Skipping it.");
				continue;
			}
			
			Macro macro;
			
			if (verbose) System.out.println("Commenting out patched macros...");
			for (int i = 0; i < fileSourceSplit.length; i++)
			{
				String line = fileSourceSplit[i];
								
				try	{ macro = new Macro(line); }
				catch (Exception e) { if (verbose) System.err.println("PATCHER: Attempted to make macro from non-macro string \"" + line + "\". Skipping it."); continue; }
				
				if (locks.getValue1().contains(macro))
				{
					while(line.replaceFirst("//.*", "").trim().endsWith("\\"))
					{
						fileSourceSplit[i] = "//" + line;
						line = fileSourceSplit[++i];
					}
					
					fileSourceSplit[i] = "//" + line;
					if (verbose) System.out.println("\tCommented out macro \"" + macro.getName() + "\"");
				}
			}
			
			fileSource = String.join("\n", fileSourceSplit);
			
			Pattern p = Pattern.compile("(\\w+\\s*(\\*)?\\s+(\\*)?\\s*)+\\w+\\s*\\([^\\)]*\\)");
			Matcher matcher = p.matcher(fileSource);
			
			if (verbose) System.out.println("Commenting out patched functions...");
			while (matcher.find())
			{
				String functionString = matcher.group();				
				Function f;
				
				try { f = new Function(functionString); }
				catch (Exception e) { if (debug || verbose) System.err.println("PATCHER: Unable to find function definition in \"" + functionString + "\". Skipping it."); continue; }
				
				if (locks.getValue0().contains(f))
				{	
					String transfer = fileSource.substring(0, matcher.start());
					String ignored = "";
					
					if (!transfer.endsWith("\n"))
					{
						String[] functionSplit = functionString.split("\n");
						int i = transfer.length() - transfer.replaceAll("\n", "").length();
						
						for (int j = 0; j < functionSplit.length; j++, i++)
						{
							if (fileSourceSplit[i].contains(functionSplit[j]) && fileSourceSplit[i].trim().startsWith("#"))
							{
								ignored += functionSplit[j] + "\n";
								functionSplit[j] = "";
							}
						}
						
						functionString = Pattern.compile("\n").splitAsStream(String.join("\n", functionSplit)).filter(s -> s != null && !s.isEmpty()).collect(Collectors.joining("\n"));						
					}
					
					functionString = "//" + functionString.replaceAll("\n", "\n//");
					newSource += transfer + ignored + functionString;
					fileSource = fileSource.substring(matcher.end());
					matcher = p.matcher(fileSource);
					
					if (verbose) System.out.println("\tCommented out function \"" + f.getName() + "\"");
				}
			}
			
			newSource += fileSource;
			
			if (debug)
			{
				System.out.println(newSource);
			}
			
			String fileName = filePath.replaceFirst("\\.\\w+$", (debug ? ".txt" : ".h"));
			File directory = new File(Paths.get(outputPath, fileName).toString().replaceFirst("\\w+\\.\\w+$", ""));
			if (!directory.exists()) directory.mkdirs();
			Files.write(Paths.get(outputPath, fileName), Arrays.stream(newSource.split("\n")).collect(Collectors.toList()), Charset.forName("UTF-8"));
		}
	}
	
	public void removeSpinDefinitions(Pair<Set<Function>, Set<Macro>> locks) throws Exception
	{
		if (verbose) System.out.println("+----------------------------+\n| Editing current spin locks |\n+----------------------------+\n");

		for (String filePath : config.getPaths(PatchConfig.SPIN_PATHS_TO_CHANGE))
		{
			if (verbose) System.out.println("Editing file \"" + filePath + "\"...");

			String fileSource = "";
			String[] fileSourceSplit;
			String newSource = "";
			
			try
			{
				fileSourceSplit = Files.readAllLines(Paths.get(kernelPath, filePath)).toArray(new String[0]);
			}
			
			catch (IOException e)
			{
				if (debug || verbose) System.err.println("PATCHER: Unable to read file \"" + filePath + "\"! Skipping it.");
				continue;
			}
			
			Macro macro;
			
			if (verbose) System.out.println("Commenting out patched macros...");
			for (int i = 0; i < fileSourceSplit.length; i++)
			{
				String line = fileSourceSplit[i];
								
				try	{ macro = new Macro(line); }
				catch (Exception e) { if (verbose) System.err.println("PATCHER: Attempted to make macro from non-macro string \"" + line + "\". Skipping it."); continue; }
				
				if (locks.getValue1().contains(macro))
				{
					while(line.replaceFirst("//.*", "").trim().endsWith("\\"))
					{
						fileSourceSplit[i] = "//" + line;
						line = fileSourceSplit[++i];
					}
					
					fileSourceSplit[i] = "//" + line;
					if (verbose) System.out.println("\tCommented out macro \"" + macro.getName() + "\"");
				}
			}
			
			fileSource = String.join("\n", fileSourceSplit);
			
			Pattern p = Pattern.compile("(\\w+\\s*(\\*)?\\s+(\\*)?\\s*)+\\w+\\s*\\([^\\)]*\\)");
			Matcher matcher = p.matcher(fileSource);
			
			if (verbose) System.out.println("Commenting out patched functions...");
			while (matcher.find())
			{
				String functionString = matcher.group();				
				Function f;
				
				try { f = new Function(functionString); }
				catch (Exception e) { if (debug || verbose) System.err.println("PATCHER: Unable to find function definition in \"" + functionString + "\". Skipping it."); continue; }
				
				if (locks.getValue0().contains(f))
				{	
					String transfer = fileSource.substring(0, matcher.start());
					String ignored = "";
					
					if (!transfer.endsWith("\n"))
					{
						String[] functionSplit = functionString.split("\n");
						int i = transfer.length() - transfer.replaceAll("\n", "").length();
						
						for (int j = 0; j < functionSplit.length; j++, i++)
						{
							if (fileSourceSplit[i].contains(functionSplit[j]) && fileSourceSplit[i].trim().startsWith("#"))
							{
								ignored += functionSplit[j] + "\n";
								functionSplit[j] = "";
							}
						}
						
						functionString = Pattern.compile("\n").splitAsStream(String.join("\n", functionSplit)).filter(s -> s != null && !s.isEmpty()).collect(Collectors.joining("\n"));
						
						System.out.println();
					}
					
					functionString = "//" + functionString.replaceAll("\n", "\n//");
					newSource += transfer + ignored + functionString;
					fileSource = fileSource.substring(matcher.end());
					matcher = p.matcher(fileSource);
					
					if (verbose) System.out.println("\tCommented out function \"" + f.getName() + "\"");
				}
			}
			
			newSource += fileSource;
			
			if (debug)
			{
				System.out.println(newSource);
			}
			
			String fileName = filePath.replaceFirst("\\.\\w+$", (debug ? ".txt" : ".h"));
			File directory = new File(Paths.get(outputPath, fileName).toString().replaceFirst("\\w+\\.\\w+$", ""));
			if (!directory.exists()) directory.mkdirs();
			Files.write(Paths.get(outputPath, fileName), Arrays.stream(newSource.split("\n")).collect(Collectors.toList()), Charset.forName("UTF-8"));
		}
	}	
}
