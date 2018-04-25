package edu.iastate.sdmay1809;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import edu.iastate.sdmay1809.shared.Config;

public class PatchConfig extends Config{
	public static String MUTEX_FUNCTION_CRITERIA;
	public static String MUTEX_MACRO_CRITERIA;
	public static String SPIN_FUNCTION_CRITERIA;
	public static String SPIN_MACRO_CRITERIA;
	public static String MUTEX_PATHS_TO_READ;
	public static String MUTEX_PATHS_TO_CHANGE;
	public static String SPIN_PATHS_TO_READ;
	public static String SPIN_PATHS_TO_CHANGE;
	public static String FUNCTION_RETURN_TYPES;
	public static String MUTEX_FUNCTIONS_TO_INCLUDE;
	public static String MUTEX_MACROS_TO_INCLUDE;
	public static String SPIN_FUNCTIONS_TO_INCLUDE;
	public static String SPIN_MACROS_TO_INCLUDE;
	public static String MUTEX_FILES_TO_INCLUDE_HEADER_IN;
	public static String SPIN_FILES_TO_INCLUDE_HEADER_IN;
	
	protected PatchConfig(Builder builder)
	{
		super(builder);
		
		MUTEX_FUNCTION_CRITERIA = (String) configOptions.get("mutex_function_criteria");
		MUTEX_MACRO_CRITERIA = (String) configOptions.get("mutex_macro_criteria");
		SPIN_FUNCTION_CRITERIA = (String) configOptions.get("spin_function_criteria");
		SPIN_MACRO_CRITERIA = (String) configOptions.get("spin_macro_criteria");
		MUTEX_PATHS_TO_READ = (String) configOptions.get("mutex_paths_to_read");
		MUTEX_PATHS_TO_CHANGE = (String) configOptions.get("mutex_paths_to_change");
		SPIN_PATHS_TO_READ = (String) configOptions.get("spin_paths_to_read");
		SPIN_PATHS_TO_CHANGE = (String) configOptions.get("spin_paths_to_change");
		FUNCTION_RETURN_TYPES = (String) configOptions.get("function_return_types");
		MUTEX_FUNCTIONS_TO_INCLUDE = (String) configOptions.get("mutex_functions_to_include");
		MUTEX_MACROS_TO_INCLUDE = (String) configOptions.get("mutex_macros_to_include");
		SPIN_FUNCTIONS_TO_INCLUDE = (String) configOptions.get("spin_functions_to_include");
		SPIN_MACROS_TO_INCLUDE = (String) configOptions.get("spin_macros_to_include");
		MUTEX_FILES_TO_INCLUDE_HEADER_IN = (String) configOptions.get("mutex_files_to_include_header_in");
		SPIN_FILES_TO_INCLUDE_HEADER_IN = (String) configOptions.get("spin_files_to_include_header_in");
	}
	
	public static Builder builder(String[] args) {
		Builder b = Config.builder(Builder.class, args);
		
		Options options = new Options();
		options.addOption("v", "verbose", false, "Print additional information to the console");
		options.addOption(Option.builder("k")
								.longOpt("kernel-path")
								.hasArg()
								.numberOfArgs(1)
								.argName("path")
								.desc("The path to the root directory of the kernel being patched. By default, it is the directory \"resources/kernel/\"")
								.build());
		options.addOption(Option.builder("d")
								.longOpt("debug")
								.hasArg()
								.numberOfArgs(1)
								.argName("path")
								.desc("The directory in which to store the Patcher output in .txt form. By default, it is \"resources/patch/debug/\"")
								.build());
		options.addOption(Option.builder("o")
								.longOpt("output")
								.hasArg()
								.numberOfArgs(1)
								.argName("path")
								.desc("The directory in which to store the Patcher output in .h form. By default, it is \"resources/patch/real/\"\n"
										+ "If the -o and -d tags are both present, -d will override -o")
								.build());
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		
		try
		{
			cmd = parser.parse(options, args);
		}
		
		catch (ParseException e)
		{
			System.err.println("[FATAL]: " + e.getMessage());
			System.exit(0);
		}
		
		if (cmd.hasOption('v'))	b.setConfig("verbose", true);
		if (cmd.hasOption('d')) b.setConfig("debug", true);
		
		if (cmd.hasOption('k')) b.setConfig("kernel_dir", cmd.getOptionValue('k'));
		if (cmd.hasOption('d')) b.setConfig("output_dir", cmd.getOptionValue('d'));
		else if (cmd.hasOption('o')) b.setConfig("output_dir", cmd.getOptionValue('o'));
		
		return b;	
	}

	public static class Builder extends Config.Builder<PatchConfig> {
		{
			String cwd = System.getProperty("user.dir");
			configOptions.putIfAbsent("mutex_function_criteria", "MutexFunctionCriteria");
			configOptions.putIfAbsent("mutex_macro_criteria", "MutexMacroCriteria");
			configOptions.putIfAbsent("spin_function_criteria", "SpinFunctionCriteria");
			configOptions.putIfAbsent("spin_macro_criteria", "SpinMacroCriteria");
			configOptions.putIfAbsent("mutex_paths_to_read", "MutexPathsToRead");
			configOptions.putIfAbsent("mutex_paths_to_change", "MutexPathsToChange");
			configOptions.putIfAbsent("spin_paths_to_read", "SpinPathsToRead");
			configOptions.putIfAbsent("spin_paths_to_change", "SpinPathsToChange");
			configOptions.putIfAbsent("function_return_types", "FunctionReturnTypes");
			configOptions.putIfAbsent("mutex_functions_to_include", "MutexFunctionsToInclude");
			configOptions.putIfAbsent("mutex_macros_to_include", "MutexMacrosToInclude");
			configOptions.putIfAbsent("spin_functions_to_include", "SpinFunctionsToInclude");
			configOptions.putIfAbsent("spin_macros_to_include", "SpinMacrosToInclude");			
			configOptions.putIfAbsent("mutex_files_to_include_header_in", "MutexFilesToIncludeHeaderIn");
			configOptions.putIfAbsent("spin_files_to_include_header_in", "SpinFilesToIncludeHeaderIn");
			configOptions.putIfAbsent("kernel_dir", Paths.get(cwd, "resources/kernel/"));
			configOptions.putIfAbsent("output_dir", Paths.get(cwd, "resources/patch/"));
			configOptions.putIfAbsent("debug", false);
			configOptions.putIfAbsent("verbose", false);
		}
		
		public Builder() {
			super();
		}
		
		public Builder(File file) {
			super();
		}
		
		public Builder(Builder base) {
			super(base);
		}
	}
	
	public String kernelPath()
	{
		return (String) configOptions.get("kernel_dir");
	}
	
	public String outputPath()
	{
		return (String) configOptions.get("output_dir");
	}
	
	public boolean debug()
	{
		return (boolean) configOptions.get("debug");
	}
	
	public boolean verbose()
	{
		return (boolean) configOptions.get("verbose");
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Boolean> getCriteria(String label)
	{	
		Object o = configOptions.get(label);
		
		if (o instanceof Map)
		{
			Map<String, Object> map = (Map<String, Object>) o;
			return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (boolean)e.getValue()));
		}
		
		return new HashMap<String, Boolean>();		
	}
	
	@SuppressWarnings("unchecked")
	public Set<String> getPaths(String label)
	{
		Object o = configOptions.get(label);
		
		if (o instanceof List)
		{
			List<Object> list = (List<Object>)o;
			return list.stream().map(e -> e.toString()).collect(Collectors.toSet());
		}
		
		return new HashSet<String>();
	}
	
	public Set<String> getFunctionReturnTypes()
	{
		return getPaths(FUNCTION_RETURN_TYPES);
	}	
	
	@SuppressWarnings("unchecked")
	public Set<Function> getFunctions(String label) throws Exception
	{
		Object o = configOptions.get(label);
		
		if (o instanceof List)
		{
			List<Object> list = (List<Object>)o;
			return list.stream().map(e -> {
				try{
					return new Function(this, e.toString());
				} 
				
				catch(Exception ex)
				{
					return null;
				}
			}).filter(e -> e != null).collect(Collectors.toSet());
		}
		
		return new HashSet<Function>();
	}
	
	@SuppressWarnings("unchecked")
	public Set<Macro> getMacros(String label) throws Exception
	{
		Object o = configOptions.get(label);
		
		if (o instanceof List)
		{
			List<Object> list = (List<Object>)o;
			return list.stream().map(e -> {
				try{
					return new Macro(e.toString());
				} 
				
				catch(Exception ex)
				{
					return null;
				}
			}).filter(e -> e != null).collect(Collectors.toSet());
		}
		
		return new HashSet<Macro>();
	}
}
