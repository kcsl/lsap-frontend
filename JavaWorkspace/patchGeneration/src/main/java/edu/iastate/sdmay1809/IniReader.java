package edu.iastate.sdmay1809;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class IniReader 
{
	private Map<String, Set<String>> sections;
	
	private static final String mutexPaths = "MutexPaths";
	private static final String spinPaths = "SpinPaths";
	private static final String mutexFunctionCriteria = "MutexFunctionCriteria";
	private static final String mutexMacroCriteria = "MutexMacroCriteria";
	private static final String spinFunctionCriteria = "SpinFunctionCriteria";
	private static final String spinMacroCriteria = "SpinMacroCriteria";
	
	public IniReader(String path) throws FileNotFoundException
	{
		Scanner s = new Scanner(new File(path));
		String line = "";
		String nextLine = "";
		String option = "";
		
		sections = new HashMap<String, Set<String>>();
		
		while (s.hasNextLine())
		{
			line = (nextLine.length() > 0) ? nextLine : s.nextLine();
			
			if (line.matches("\\[\\w+\\]"))
			{
				Set<String> options = new HashSet<String>();
				
				while(true)
				{
					if (!s.hasNextLine()) break;
					
					option = s.nextLine();
					
					if (option.matches("\\[\\w+\\]")) break;
					if (option.length() > 0) options.add(option);
				}
				
				sections.put(line.replace("[", "").replace("]", ""), options);
			}
			
			nextLine = option;
		}
		
		s.close();
	}
	
	public Set<String> getMutexPaths()
	{
		if (sections.containsKey(mutexPaths)) return sections.get(mutexPaths);
		return null;
	}
	
	public Set<String> getSpinPaths()
	{
		if (sections.containsKey(spinPaths)) return sections.get(spinPaths);
		return null;
	}
	
	public Set<Criteria> getMutexFunctionCriteria()
	{
		Set<Criteria> criteria = null;
		
		if (sections.containsKey(mutexFunctionCriteria))
		{
			criteria = new HashSet<Criteria>();
			
			for (String option : sections.get(mutexFunctionCriteria))
			{
				if (!option.contains("=")) continue;
				
				String[] optionParts = option.split("=", 2);
				
				criteria.add(new Criteria(optionParts[0], optionParts[1].equals("true")));
			}
		}
		
		return criteria;
	}
	
	public Set<Criteria> getMutexMacroCriteria()
	{
		Set<Criteria> criteria = null;
		
		if (sections.containsKey(mutexMacroCriteria))
		{
			criteria = new HashSet<Criteria>();
			
			for (String option : sections.get(mutexMacroCriteria))
			{
				if (!option.contains("=")) continue;
				
				String[] optionParts = option.split("=", 2);
				
				criteria.add(new Criteria(optionParts[0], optionParts[1].equals("true")));
			}
		}
		
		return criteria;
	}
	
	public Set<Criteria> getSpinFunctionCriteria()
	{
		Set<Criteria> criteria = null;
		
		if (sections.containsKey(spinFunctionCriteria))
		{
			criteria = new HashSet<Criteria>();
			
			for (String option : sections.get(spinFunctionCriteria))
			{
				if (!option.contains("=")) continue;
				
				String[] optionParts = option.split("=", 2);
				
				criteria.add(new Criteria(optionParts[0], optionParts[1].equals("true")));
			}
		}
		
		return criteria;
	}
	
	public Set<Criteria> getSpinMacroCriteria()
	{
		Set<Criteria> criteria = null;
		
		if (sections.containsKey(spinMacroCriteria))
		{
			criteria = new HashSet<Criteria>();
			
			for (String option : sections.get(spinMacroCriteria))
			{
				if (!option.contains("=")) continue;
				
				String[] optionParts = option.split("=", 2);
				
				criteria.add(new Criteria(optionParts[0], optionParts[1].equals("true")));
			}
		}
		
		return criteria;
	}
}
