package edu.iastate.sdmay1809;

import edu.iastate.sdmay1809.shared.Utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PatchConfig 
{
	public static String MUTEX_FUNCTION_CRITERIA = "MutexFunctionCriteria";
	public static String MUTEX_MACRO_CRITERIA = "MutexMacroCriteria";
	public static String SPIN_FUNCTION_CRITERIA = "SpinFunctionCriteria";
	public static String SPIN_MACRO_CRITERIA = "SpinMacroCriteria";
	public static String MUTEX_PATHS_TO_READ = "MutexPathsToRead";
	public static String MUTEX_PATHS_TO_CHANGE = "MutexPathsToChange";
	public static String SPIN_PATHS_TO_READ = "SpinPathsToRead";
	public static String SPIN_PATHS_TO_CHANGE = "SpinPathsToChange";
	public static String FUNCTION_RETURN_TYPES = "FunctionReturnTypes";
	public static String MUTEX_FUNCTIONS_TO_INCLUDE = "MutexFunctionsToInclude";
	public static String MUTEX_MACROS_TO_INCLUDE = "MutexMacrosToInclude";
	public static String SPIN_FUNCTIONS_TO_INCLUDE = "SpinFunctionsToInclude";
	public static String SPIN_MACROS_TO_INCLUDE = "SpinMacrosToInclude";
	public static String MUTEX_FILES_TO_INCLUDE_HEADER_IN = "MutexFilesToIncludeHeaderIn";
	public static String SPIN_FILES_TO_INCLUDE_HEADER_IN = "SpinFilesToIncludeHeaderIn";
	
	private Map<String, Map<String, Boolean>> criteria;
	private Map<String, Set<String>> paths;
	private Set<String> functionReturnTypes;
	private Map<String, Set<Function>> functions;
	private Map<String, Set<Macro>> macros;
	
	public PatchConfig(String jsonPath) throws Exception
	{
		String configContent;
		JSONObject configObject = null;
		
		criteria = new HashMap<String, Map<String, Boolean>>();
		paths = new HashMap<String, Set<String>>();
		functionReturnTypes = new HashSet<String>();
		functions = new HashMap<String, Set<Function>>();
		macros = new HashMap<String, Set<Macro>>();
		
		try
		{
			configContent = String.join("\n", Files.readAllLines(Paths.get(jsonPath), Charset.forName("UTF-8")));
			configObject = new JSONObject(configContent);
		}
		
		catch (IOException e)
		{
			System.err.println("CONFIG: Unable to read config file! Using default values.");
			configObject = null;
		}
		
		catch (JSONException e)
		{
			System.err.println("CONFIG: Config file was not a JSON file! Using default values.");
			configObject = null;
		}
		
		String[] criteriaLabels = { MUTEX_FUNCTION_CRITERIA, 
									MUTEX_MACRO_CRITERIA,
									SPIN_FUNCTION_CRITERIA,
									SPIN_MACRO_CRITERIA };
		
		String[] pathLabels = { MUTEX_PATHS_TO_READ,
								MUTEX_PATHS_TO_CHANGE,
								SPIN_PATHS_TO_READ,
								SPIN_PATHS_TO_CHANGE,
								MUTEX_FILES_TO_INCLUDE_HEADER_IN,
								SPIN_FILES_TO_INCLUDE_HEADER_IN };
		
		String[] functionLabels = { MUTEX_FUNCTIONS_TO_INCLUDE,
									SPIN_FUNCTIONS_TO_INCLUDE };
		
		String[] macroLabels = { MUTEX_MACROS_TO_INCLUDE,
								 SPIN_MACROS_TO_INCLUDE };

		if (configObject != null)
		{
			for (String s : criteriaLabels)
			{
				JSONObject criteriaObject;
				Map<String, Boolean> curCriteria = new HashMap<String, Boolean>();
				
				criteriaObject = configObject.getJSONObject(s);
				for (String key : criteriaObject.keySet())
				{
					curCriteria.put(key, criteriaObject.getBoolean(key));
				}
				
				criteria.put(s, curCriteria);
			}
			
			for (String s : pathLabels)
			{
				JSONArray pathObject;
				Set<String> curPaths = new HashSet<String>();
				
				pathObject = configObject.getJSONArray(s);
				
				for (Object path : pathObject)
				{
					curPaths.add((String) path);
				}
				
				paths.put(s, curPaths);
			}
			
			JSONArray typeObject;
			
			typeObject = configObject.getJSONArray(FUNCTION_RETURN_TYPES);
			
			for (Object type : typeObject)
			{
				functionReturnTypes.add((String) type);
			}
			
			for (String function : functionLabels)
			{
				JSONArray functionObject;
				Set<Function> curFunctions = new HashSet<Function>();
				
				functionObject = configObject.getJSONArray(function);
				
				for (Object f : functionObject)
				{
					curFunctions.add(new Function(this, (String) f));
				}
				
				functions.put(function, curFunctions);
			}
			
			for (String macro : macroLabels)
			{
				JSONArray macroObject;
				Set<Macro> curMacros = new HashSet<Macro>();
				
				macroObject = configObject.getJSONArray(macro);
				
				for (Object m : macroObject)
				{
					curMacros.add(new Macro((String) m));
				}
				
				macros.put(macro, curMacros);
			}
		}
	}
	
	public Map<String, Boolean> getCriteria(String label)
	{	
		return criteria.getOrDefault(label, new HashMap<String, Boolean>());
	}
	
	public Set<String> getPaths(String label)
	{
		return paths.getOrDefault(label, new HashSet<String>());
	}
	
	public Set<String> getFunctionReturnTypes()
	{
		return Utils.coalesce(functionReturnTypes, new HashSet<String>());
	}
	
	public Set<Function> getFunctions(String label)
	{
		return functions.getOrDefault(label, new HashSet<Function>());
	}
	
	public Set<Macro> getMacros(String label)
	{
		return macros.getOrDefault(label, new HashSet<Macro>());
	}
}
