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

		addMutexIncludeStatements(mutexLocks);
		addSpinIncludeStatements(spinLocks);

		removeMutexDefinitions(mutexLocks);
		removeSpinDefinitions(spinLocks);		
	}

	public Pair<Set<Function>, Set<Macro>> generateLSAPMutexHeaderFile() throws Exception
	{
		return generateHeaderFile(config.getFunctions(PatchConfig.MUTEX_FUNCTIONS_TO_INCLUDE), config.getMacros(PatchConfig.MUTEX_MACROS_TO_INCLUDE), config.getPaths(PatchConfig.MUTEX_PATHS_TO_READ),
								  config.getCriteria(PatchConfig.MUTEX_FUNCTION_CRITERIA), config.getCriteria(PatchConfig.MUTEX_MACRO_CRITERIA), "mutex");
	}
	
	public Pair<Set<Function>, Set<Macro>> generateLSAPSpinHeaderFile() throws Exception
	{
		return generateHeaderFile(config.getFunctions(PatchConfig.SPIN_FUNCTIONS_TO_INCLUDE), config.getMacros(PatchConfig.SPIN_MACROS_TO_INCLUDE), config.getPaths(PatchConfig.SPIN_PATHS_TO_READ),
				  config.getCriteria(PatchConfig.SPIN_FUNCTION_CRITERIA), config.getCriteria(PatchConfig.SPIN_MACRO_CRITERIA), "spin");
	}
	
	public void removeMutexDefinitions(Pair<Set<Function>, Set<Macro>> locks) throws Exception
	{
		removeDefinitions(locks, config.getPaths(PatchConfig.MUTEX_PATHS_TO_CHANGE), "mutex");
	}
	
	public void removeSpinDefinitions(Pair<Set<Function>, Set<Macro>> locks) throws Exception
	{
		removeDefinitions(locks, config.getPaths(PatchConfig.SPIN_PATHS_TO_CHANGE), "spin");
	}	

	public void addMutexIncludeStatements(Pair<Set<Function>, Set<Macro>> locks) throws Exception
	{
		addIncludeStatements(locks, config.getPaths(PatchConfig.MUTEX_FILES_TO_INCLUDE_HEADER_IN), "mutex");
	}
	
	public void addSpinIncludeStatements(Pair<Set<Function>, Set<Macro>> locks) throws Exception
	{
		addIncludeStatements(locks, config.getPaths(PatchConfig.SPIN_FILES_TO_INCLUDE_HEADER_IN), "spin");
	}
	
	private Pair<Set<Function>, Set<Macro>> generateHeaderFile(Set<Function> includedFunctions, Set<Macro> includedMacros, Set<String> pathsToReadFrom, 
			   Map<String, Boolean> functionCriteriaMap, Map<String, Boolean> macroCriteriaMap, String lockType) throws Exception
	{
		if (verbose) System.out.println("+--------------------\n|    " + lockType + "  locks    \n+--------------------\n");

		Set<Function> functions = new LinkedHashSet<Function>();
		Set<Macro> macros = new LinkedHashSet<Macro>();

		functions.addAll(includedFunctions);
		macros.addAll(includedMacros);

		for (String filePath : pathsToReadFrom)
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
						Function f = new Function(config, matcher.group());
						if (Function.isLockingFunction(f, functionCriteriaMap) && f.hasValidReturnType())
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
					if (Macro.isLockingMacro(m, macroCriteriaMap)) 
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

		if (verbose) System.out.println("\nGenerating " + lockType + " lock header file...");

		List<String> outputContent = new ArrayList<String>();

		outputContent.add("/**************************************************************/");
		outputContent.add("// " + lockType.toUpperCase() + " LOCK");
		outputContent.add("/**************************************************************/");
		outputContent.add("");
		outputContent.add("#ifndef __LINUX_L_SAP_" + lockType.toUpperCase() + "_H");
		outputContent.add("#define __LINUX_L_SAP_" + lockType.toUpperCase() + "_H");
		outputContent.add("#include <linux/spinlock_types.h>");
		outputContent.add("");
		outputContent.add("// Definition for \"" + lockType + "\" related functions with empty bodies.");

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
		outputContent.add("#endif /* __LINUX_L_SAP_" + lockType.toUpperCase() + "_H */");
		outputContent.add("");

		if (debug || verbose)
		{
			for (String line : outputContent)
			{
				System.out.println(line);
			}
		}

		File directory = new File(Paths.get(outputPath, "include/linux/lsap_" + lockType.toLowerCase() + "_lock." + (debug ? "txt" : "h")).toString().replaceFirst("\\w+\\.\\w+$", ""));
		if (!directory.exists()) directory.mkdirs();
		Files.write(Paths.get(outputPath, "include/linux/lsap_" + lockType + "_lock." + (debug ? "txt" : "h")), outputContent, Charset.forName("UTF-8"));

		return new Pair<Set<Function>, Set<Macro>>(functions, macros);	
	}	
	
	private void removeDefinitions(Pair<Set<Function>, Set<Macro>> locks, Set<String> paths, String lockType) throws IOException
	{
		if (verbose) System.out.println("+----------------------------\n| Editing current " +  lockType + " locks \n+----------------------------\n");

		for (String filePath : paths)
		{
			if (verbose) System.out.println("Editing file \"" + filePath + "\"...");
			if (debug) filePath = filePath.replaceFirst("\\.\\w+$", ".txt");
			
			String fileSource = "";
			String[] fileSourceSplit;
			String newSource = "";
			
			try
			{
				fileSourceSplit = Files.readAllLines(Paths.get(outputPath, filePath)).toArray(new String[0]);
			}
			
			catch (IOException e)
			{
				try
				{
					fileSourceSplit = Files.readAllLines(Paths.get(kernelPath, filePath)).toArray(new String[0]);
				}
				
				catch (IOException e2)
				{
					if (debug || verbose) System.err.println("PATCHER: Unable to read file \"" + filePath + "\"! Skipping it.");
					continue;
				}
			}
			
			Macro macro;
			
			if (verbose) System.out.println("Commenting out patched macros...");
			for (int i = 0; i < fileSourceSplit.length; i++)
			{
				String line = fileSourceSplit[i];
								
				if (line.toLowerCase().contains("export_symbol"))
				{
					fileSourceSplit[i] = "//" + line;
					continue;
				}
				
				try	{ macro = new Macro(line); }
				catch (Exception e) { if (verbose) System.err.println("PATCHER: Attempted to make macro from non-macro string \"" + line + "\". Skipping it."); continue; }
				
				if (locks.getValue1().contains(macro) || locks.getValue0().contains(macro))
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
				
				try { f = new Function(config, functionString); }
				catch (Exception e) { if (debug || verbose) System.err.println("PATCHER: Unable to find function definition in \"" + functionString + "\". Skipping it."); continue; }
				
				if ((locks.getValue0().contains(f) || locks.getValue1().contains(f)) && f.hasValidReturnType())
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
						
						functionString = Pattern.compile("\n").splitAsStream(String.join("\n", functionSplit)).filter(s -> !s.isEmpty()).collect(Collectors.joining("\n"));
					}
					
					functionString = "//" + functionString.replaceAll("\n", "\n//");
					fileSource = fileSource.substring(matcher.end());
					fileSourceSplit = fileSource.split("\n");
					
					while(fileSource.length() > 0 && fileSource.charAt(0) != '{' && fileSource.charAt(0) != ';')
					{
						functionString += fileSource.charAt(0);
						if (fileSource.charAt(0) == '\n') functionString += "//";
						fileSource = fileSource.substring(1);
					}
					
					if (fileSource.charAt(0) == '{')
					{
						int braceCount = 0;
						
						do
						{
							functionString += fileSource.charAt(0);
							
							if (fileSource.charAt(0) == '{') braceCount++;
							if (fileSource.charAt(0) == '}') braceCount--;
							if (fileSource.charAt(0) == '\n') functionString += "//";
							
							fileSource = fileSource.substring(1);
						} while(braceCount > 0);
					}
					
					newSource += transfer + ignored + functionString;
					matcher = p.matcher(fileSource);
					
					if (verbose) System.out.println("\tCommented out function \"" + f.getName() + "\"");
				}
			}
			
			newSource += fileSource;
			
			if (debug)
			{
				System.out.println(newSource);
			}
			
			String fileName = filePath;
			if (debug) fileName = fileName.replaceFirst("\\.\\w+$", ".txt");
			File directory = new File(Paths.get(outputPath, fileName).toString().replaceFirst("\\w+\\.\\w+$", ""));
			if (!directory.exists()) directory.mkdirs();
			Files.write(Paths.get(outputPath, fileName), Arrays.stream(newSource.split("\n")).collect(Collectors.toList()), Charset.forName("UTF-8"));
		}
	}

	private void addIncludeStatements(Pair<Set<Function>, Set<Macro>> locks, Set<String> filePaths, String lockType) throws IOException
	{
		for (String path : filePaths)
		{
			String fileContent;
			String[] fileContentSplit;
			try
			{
				fileContentSplit = Files.readAllLines(Paths.get(kernelPath, path), Charset.forName("UTF-8")).toArray(new String[0]);
				fileContent = String.join("\n", fileContentSplit);
			}
			
			catch (IOException e)
			{
				if (debug || verbose) System.err.println("PATCHER: Unable to read file \"" + path + "\"! Skipping it.");
				continue;
			}
			
			int firstMacroLine = 0;
			int firstFunctionLine = 0;
			
			for (int i = 0; i < fileContentSplit.length; i++)
			{
				Macro m;
				
				try { m = new Macro(fileContentSplit[i].replace("//", "")); }
				catch (Exception e) { continue; }
				
				if (locks.getValue0().contains(m) || locks.getValue1().contains(m))
				{
					firstMacroLine = i;
					break;
				}
			}
			
			Matcher m = Pattern.compile("(\\w+\\s*(\\*)?\\s+(\\*)?\\s*)+\\w+\\s*\\([^\\)]*\\)").matcher(fileContent);
			
			while (m.find())
			{
				Function f;
				
				try { f = new Function(config, m.group().replaceAll("//", "")); }
				catch (Exception e) { continue; }
				
				if ((locks.getValue0().contains(f) || locks.getValue1().contains(f)) && f.hasValidReturnType())
				{
					firstFunctionLine = fileContent.substring(0, m.start()).length() - fileContent.substring(0, m.start()).replace("\n", "").length() + 1;
					break;
				}
			}
			
			int braceCount = 0;
			int ifAndEndif = -1;
			int insertLine = 0;
			
			for (int i = 0; i < Math.min(firstMacroLine, firstFunctionLine); i++)
			{
				String line = fileContentSplit[i];
				
				if (line.trim().toLowerCase().matches("\\s*#\\s*endif.*")) ifAndEndif--;
				else if (line.trim().toLowerCase().matches("\\s*#\\s*if.*")) ifAndEndif++;
				braceCount += line.length() - line.replace("{", "").length();
				braceCount -= line.length() - line.replace("}", "").length();
				
				if (braceCount == 0 && ifAndEndif == 0) insertLine = i;
			}
			
			fileContentSplit[insertLine] = fileContentSplit[insertLine] + "\n#include <linux/lsap_" + lockType.toLowerCase() + "_lock.h>";

			String fileName = path;
			if (debug) fileName = fileName.replaceFirst("\\.\\w+$", ".txt");
			File directory = new File(Paths.get(outputPath, fileName).toString().replaceFirst("\\w+\\.\\w+$", ""));
			if (!directory.exists()) directory.mkdirs();
			Files.write(Paths.get(outputPath, fileName), Arrays.stream(fileContentSplit).collect(Collectors.toList()), Charset.forName("UTF-8"));
		}
	}
}
