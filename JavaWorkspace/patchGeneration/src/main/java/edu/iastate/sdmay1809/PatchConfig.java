package edu.iastate.sdmay1809;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PatchConfig {
	private static PatchConfig instance;
	private static Path jsonPath;
	
	public static String MUTEX_FUNCTION_CRITERIA = "MutexFunctionCriteria";
	public static String MUTEX_MACRO_CRITERIA = "MutexMacroCriteria";
	public static String SPIN_FUNCTION_CRITERIA = "SpinFunctionCriteria";
	public static String SPIN_MACRO_CRITERIA = "SpinMacroCriteria";
	public static String MUTEX_PATHS_TO_READ = "MutexPathsToRead";
	public static String MUTEX_PATHS_TO_CHANGE = "MutexPathsToChange";
	public static String SPIN_PATHS_TO_READ = "SpinPathsToRead";
	public static String SPIN_PATHS_TO_CHANGE = "SpinPathsToChange";
	
	private Map<String, Map<String, Boolean>> criteria;
	private Map<String, Set<String>> paths;
	
	protected PatchConfig() throws Exception
	{
		String configContent;
		JSONObject configObject = null;
		
		criteria = new HashMap<String, Map<String, Boolean>>();
		paths = new HashMap<String, Set<String>>();
		
		try
		{
			configContent = String.join("\n", Files.readAllLines(jsonPath, Charset.forName("UTF-8")));
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
	}
	
	public static PatchConfig getInstance() throws Exception
	{
		if (instance == null)
		{
			if (jsonPath == null) throw new Exception("CONFIG: No JSON config file was chosen!");
			instance = new PatchConfig();
		}
		return instance;
	}
	
	public static void setJSONPath(String path)
	{
		jsonPath = Paths.get(path);
	}
	
	public Map<String, Boolean> getCriteria(String label)
	{	
		return criteria.getOrDefault(label, null);
	}
	
	public Set<String> getPaths(String label)
	{
		return paths.getOrDefault(label, null);
	}
}
