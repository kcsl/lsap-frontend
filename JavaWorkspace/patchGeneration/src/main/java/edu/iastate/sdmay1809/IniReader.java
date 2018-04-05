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
		
	public Set<String> getPaths(String tag)
	{
		if (sections.containsKey(tag)) return sections.get(tag);
		return null;
	}
	
	public Set<Criteria> getCriteria(String tag)
	{
		Set<Criteria> criteria = null;
		
		if (sections.containsKey(tag))
		{
			criteria = new HashSet<Criteria>();
			
			for (String option : sections.get(tag))
			{
				if (!option.contains("=")) continue;
				
				String[] optionParts = option.split("=", 2);
				
				criteria.add(new Criteria(optionParts[0], optionParts[1].equals("true")));
			}
		}
		
		return criteria;
	}
	
}
