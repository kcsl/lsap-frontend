package edu.iastate.sdmay1809;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

public class IniReader {
	private ArrayList<Criteria> mutexFunctionCriteria;
	private ArrayList<Criteria> mutexMacroCriteria;
	private ArrayList<Criteria> spinFunctionCriteria;
	private ArrayList<Criteria> spinMacroCriteria;
	private ArrayList<String> mutexPaths;
	private ArrayList<String> spinPaths;
	
	public IniReader(String path) throws InvalidFileFormatException, IOException
	{
		mutexFunctionCriteria = new ArrayList<Criteria>();
		mutexMacroCriteria = new ArrayList<Criteria>();
		spinFunctionCriteria = new ArrayList<Criteria>();
		spinMacroCriteria = new ArrayList<Criteria>();
		mutexPaths = new ArrayList<String>();
		spinPaths = new ArrayList<String>();
		
		Ini ini = new Ini(new File(path));
		
		for (String sectionName : ini.keySet())
		{
			for (String option : ini.get(sectionName).keySet())
			{			
				Criteria c = null;
				
				if (sectionName.contains("Criteria")) c = new Criteria(option, ini.get(sectionName, option, boolean.class));

				switch (sectionName)
				{
					case "MutexFunctionCriteria":
						mutexFunctionCriteria.add(c);
						break;
					
					case "MutexMacroCriteria":
						mutexMacroCriteria.add(c);
						break;
					
					case "SpinFunctionCriteria":
						spinFunctionCriteria.add(c);
						break;
					
					case "SpinMacroCriteria":
						spinMacroCriteria.add(c);
						break;
					
					case "MutexPaths":
						if (ini.get(sectionName, option, boolean.class)) mutexPaths.add(option);
						break;
					
					case "SpinPaths":
						if (ini.get(sectionName, option, boolean.class)) spinPaths.add(option);
						break;
					
					default:;
				}
			}
		}
	}
	
	public ArrayList<Criteria> getMutexFunctionCriteria()
	{
		return mutexFunctionCriteria;
	}

	public ArrayList<Criteria> getMutexMacroCriteria()
	{
		return mutexMacroCriteria;
	}

	public ArrayList<Criteria> getSpinFunctionCriteria()
	{
		return spinFunctionCriteria;
	}

	public ArrayList<Criteria> getSpinMacroCriteria()
	{
		return spinMacroCriteria;
	}

	public ArrayList<String> getMutexPaths()
	{
		return mutexPaths;
	}

	public ArrayList<String> getSpinPaths()
	{
		return spinPaths;
	}

}
