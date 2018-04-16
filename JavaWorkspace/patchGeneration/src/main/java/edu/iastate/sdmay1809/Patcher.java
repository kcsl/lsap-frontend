package edu.iastate.sdmay1809;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.javatuples.Pair;

import edu.iastate.sdmay1809.shared.Utils;

public class Patcher {
	private PatchConfig config;
	private String kernelPath;
	private String outputPath;
	private boolean debug;
	@SuppressWarnings("unused")
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
		Pair<Set<Function>, Set<Macro>> mutexLocks = generateLSAPMutexHeaderFile();
		Pair<Set<Function>, Set<Macro>> spinLocks = generateLSAPSpinHeaderFile();

		removeMutexDefinitions(mutexLocks);
		removeSpinDefinitions(spinLocks);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public Pair<Set<Function>, Set<Macro>> generateLSAPMutexHeaderFile() throws Exception
	{
		Set<Function> functions = new LinkedHashSet<Function>();
		Set<Macro> macros = new LinkedHashSet<Macro>();
		
		functions.addAll(config.getFunctions(PatchConfig.MUTEX_FUNCTIONS_TO_INCLUDE));
		macros.addAll(config.getMacros(PatchConfig.MUTEX_MACROS_TO_INCLUDE));

		for (String filePath : config.getPaths(PatchConfig.MUTEX_PATHS_TO_READ))
		{
			String fileSource;
			
			try
			{
				fileSource = String.join("\n", Files.readAllLines(Paths.get(kernelPath, filePath), Charset.forName("UTF-8")));
			}
			
			catch (IOException e)
			{
				System.err.println("PATCHER: Unable to read file \"" + filePath + "\"! Skipping it.");
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
						if (Function.isLockingFunction(f, config.getCriteria(PatchConfig.MUTEX_FUNCTION_CRITERIA))) functions.add(f);
					}
				}
				
				catch (Exception e) 
				{
					System.err.println("PATCHER: Attempted to make function from non-function string:\n" + matcher.group());
				}
			}
									
			matcher = Pattern.compile("#\\s*[defineDEFINE]{6}\\s+\\w+\\s*\\([^\\)]*\\)").matcher(fileSource);			
			while (matcher.find())
			{
				try
				{
					Macro m = new Macro(matcher.group());
					if (Macro.isLockingMacro(m, config.getCriteria(PatchConfig.MUTEX_MACRO_CRITERIA))) macros.add(m);
				}
				
				catch (Exception e)
				{
					System.err.println("PATCHER: Attempted to make macro from non-macro string:\n" + matcher.group());
				}
			}
			
		}
		
		for (Iterator<Function> iter = functions.iterator(); iter.hasNext();)
		{
			Function f = iter.next();
			if (macros.contains(f))
			{
				iter.remove();
			}
		}
		
		/*
		 * TODO: Map macros to their functions
		 */
		for (Macro m : macros)
		{
			int minLevDist = Integer.MAX_VALUE;

			for (Function f : functions)
			{
				int curLevDist = Utils.levenshteinDistance(m.getName(), f.getName());
				
				if (curLevDist < minLevDist)
				{
					minLevDist = curLevDist;
					m.setBodyFunction(f);
				}
			}
		}
		
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
		
		if (debug)
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
	
	@SuppressWarnings("unlikely-arg-type")
	public Pair<Set<Function>, Set<Macro>> generateLSAPSpinHeaderFile() throws Exception
	{
		Set<Function> functions = new LinkedHashSet<Function>();
		Set<Macro> macros = new LinkedHashSet<Macro>();
		
		functions.addAll(config.getFunctions(PatchConfig.SPIN_FUNCTIONS_TO_INCLUDE));
		macros.addAll(config.getMacros(PatchConfig.SPIN_MACROS_TO_INCLUDE));

		for (String filePath : config.getPaths(PatchConfig.SPIN_PATHS_TO_READ))
		{
			String fileSource;
			
			try
			{
				fileSource = String.join("\n", Files.readAllLines(Paths.get(kernelPath, filePath), Charset.forName("UTF-8")));
			}
			
			catch (IOException e)
			{
				System.err.println("PATCHER: Unable to read file \"" + filePath + "\"! Skipping it.");
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
						if (Function.isLockingFunction(f, config.getCriteria(PatchConfig.SPIN_FUNCTION_CRITERIA))) functions.add(f);
					}
				}
				
				catch (Exception e) 
				{
					System.err.println("PATCHER: Attempted to make function from non-function string:\n" + matcher.group());
				}
			}
									
			matcher = Pattern.compile("#\\s*[defineDEFINE]{6}\\s+\\w+\\s*\\([^\\)]*\\)").matcher(fileSource);			
			while (matcher.find())
			{
				try
				{
					Macro m = new Macro(matcher.group());
					if (Macro.isLockingMacro(m, config.getCriteria(PatchConfig.SPIN_MACRO_CRITERIA))) macros.add(m);
				}
				
				catch (Exception e)
				{
					System.err.println("PATCHER: Attempted to make macro from non-macro string:\n" + matcher.group());
				}
			}
		}
				
		for (Iterator<Function> iter = functions.iterator(); iter.hasNext();)
		{
			Function f = iter.next();
			if (macros.contains(f))
			{
				iter.remove();
			}
		}
		
		/*
		 * TODO: Map macros to their functions
		 */
		for (Macro m : macros)
		{
			int minLevDist = Integer.MAX_VALUE;

			for (Function f : functions)
			{
				int curLevDist = Utils.levenshteinDistance(m.getName(), f.getName());
				
				if (curLevDist < minLevDist)
				{
					minLevDist = curLevDist;
					m.setBodyFunction(f);
				}
			}
		}
		
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
		
		if (debug)
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
		for (String filePath : config.getPaths(PatchConfig.MUTEX_PATHS_TO_CHANGE))
		{
			String fileSource = "";
			String newSource = "";
			
			try
			{
				fileSource = String.join("\n", Files.readAllLines(Paths.get(kernelPath, filePath)));
			}
			
			catch (IOException e)
			{
				System.err.println("PATCHER: Unable to read file \"" + filePath + "\"! Skipping it.");
				continue;
			}
			
			Matcher matcher = Pattern.compile("(\\w+\\s*(\\*)?\\s+(\\*)?\\s*)+\\w+\\s*\\([^\\)]*\\)").matcher(fileSource);
			
			while(matcher.find())
			{
				try
				{
					String functionString = matcher.group();
					if (locks.getValue0().contains(new Function(functionString)))
					{
						String transfer = fileSource.substring(0, matcher.start());
						newSource += transfer;
						fileSource = fileSource.substring(matcher.end());
					}
				}
				
				catch (Exception e)
				{
					System.err.println("PATCHER: Unable to find a function definition in \"" + matcher.group() + "\"! Skipping it.");
				}
			}
			
			
			
		}
	}
	
	public void removeSpinDefinitions(Pair<Set<Function>, Set<Macro>> locks)
	{
		
	}	
}
