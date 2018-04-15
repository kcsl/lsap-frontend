package edu.iastate.sdmay1809;

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
								SPIN_PATHS_TO_CHANGE };
		
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
				
				try
				{
					criteriaObject = configObject.getJSONObject(s);
					for (String key : criteriaObject.keySet())
					{
						curCriteria.put(key, criteriaObject.getBoolean(key));
					}
				}
				
				catch(JSONException e)
				{
					throw new Exception("CONFIG: Error parsing object \"" + s + "\" in config file!");
				}
				
				criteria.put(s, curCriteria);
			}
			
			for (String s : pathLabels)
			{
				JSONArray pathObject;
				Set<String> curPaths = new HashSet<String>();
				
				try
				{
					pathObject = configObject.getJSONArray(s);
					
					for (Object path : pathObject)
					{
						curPaths.add((String) path);
					}
				}
				
				catch(JSONException e)
				{
					throw new Exception("CONFIG: Error parsing object \"" + s + "\" in config file!");
				}
				
				paths.put(s, curPaths);
			}
			
			JSONArray typeObject;
			
			try
			{
				typeObject = configObject.getJSONArray(FUNCTION_RETURN_TYPES);
				
				for (Object type : typeObject)
				{
					functionReturnTypes.add((String) type);
				}
			}
			
			catch(JSONException e)
			{
				throw new Exception("CONFIG Error parsing object \"" + FUNCTION_RETURN_TYPES + "\" in config file!");
			}
			
			for (String function : functionLabels)
			{
				JSONArray functionObject;
				Set<Function> curFunctions = new HashSet<Function>();
				
				try
				{
					functionObject = configObject.getJSONArray(function);
					
					for (Object f : functionObject)
					{
						curFunctions.add(new Function((String) f));
					}
				}
				
				catch(JSONException e)
				{
					throw new Exception("CONFIG: Error parsing object \"" + function + "\" in config file!");
				}
				
				functions.put(function, curFunctions);
			}
			
			for (String macro : macroLabels)
			{
				JSONArray macroObject;
				Set<Macro> curMacros = new HashSet<Macro>();
				
				try
				{
					macroObject = configObject.getJSONArray(macro);
					
					for (Object m : macroObject)
					{
						curMacros.add(new Macro((String) m));
					}
				}
				
				catch(JSONException e)
				{
					throw new Exception("CONFIG: Error parsing object \"" + macro + "\" in config file!");
				}
				
				macros.put(macro, curMacros);
			}
		}
	}
	
	public Map<String, Boolean> getCriteria(String label)
	{	
		return criteria.getOrDefault(label, null);
	}
	
	public Set<String> getPaths(String label)
	{
		return paths.getOrDefault(label, null);
	}
	
	public Set<String> getFunctionReturnTypes()
	{
		return functionReturnTypes;
	}
	
	public Set<Function> getFunctions(String label)
	{
		return functions.getOrDefault(label, null);
	}
	
	public Set<Macro> getMacros(String label)
	{
		return macros.getOrDefault(label, null);
	}
}
