package edu.iastate.sdmay1809;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines a C macro including macro name, parameters, and macro body
 */
public class Macro implements Comparable<Macro> {
	
	// Private variables for the macro's name, parameters, and body
	private String macroName;
	private ArrayList<String> params;
	private Function macroBody;
	
	// Assume line was found to be a define macro
	public Macro(String line)
	{
		params = new ArrayList<String>();
		
		// Remove the preprocessor directive and split into macro name and parameters
		String tmp = line.replace("#define", "").replace("//", "").trim();
		String[] macroParts = tmp.split("\\(", 2);
		
		macroName = macroParts[0].trim();
		
		// Remove the trailing ")" and split the "all parameters" string into an array
		macroParts[1] = macroParts[1].substring(0, macroParts[1].indexOf(")"));
		String[] allParams = macroParts[1].split(",");
		
		// For each parameter in the array, add it to the list of parameters
		for (String p : allParams)
		{
			params.add(p.trim());
		}
		
		// Initially, there's no body for the macro
		macroBody = null;
	}
	
	// Get the macro name
	public String getName()
	{
		return macroName;
	}
	
	// Get a parameter at a given index
	public String getParameter(int index)
	{
		if (index < 0 || index >= params.size()) return null;
		
		return params.get(index);
	}
	
	// Get the parameters for the macro. Macro parameters don't have types, only names
	public ArrayList<String> getParameters()
	{
		return params;
	}
		
	// Set which function should be used for the macro body
	public void setMacroBody(Function body)
	{
		macroBody = body;
	}
	
	// This function determines whether a list of macros contains a macro with a given name
	public static boolean contains(List<Macro> macros, String name)
	{
		// Iterate through the list and return true if a macro with the given name is found
		for (Macro m : macros)
		{
			if (m.getName().equals(name)) return true;
		}
		
		return false;
	}
	
	// This function generates a proper C macro definition
	public String printAsDefine()
	{
		// Parameters for the macro and it's body function
		String parameters = "";
		String bodyParameters = "";
		
		// If there's no body for the macro, return null
		if (macroBody == null) return null;
		
		// Loop through the longer parameter list
		for (int i = 0; i < Math.max(params.size(), macroBody.getParameters().size()); i++)
		{
			String param = "";
			
			// Only get the next value if it exists
			if (i < params.size())
			{
				param = params.get(i);
				
				// Add the parameter to the list of macro parameters, including a "," if not the first parameter in the list
				if (parameters.length() != 0) parameters += ", ";
				parameters += param;
			}
			
			if (i < macroBody.getParameters().size())
			{
				// Add a ", " to the body function parameter list if there are already parameters present
				if (bodyParameters.length() != 0) bodyParameters += ", ";
	
				// There is a macro parameter in addition to a body parameter
				if (!param.equals(""))
				{
					// This parameter type is a special case where we pass in an element of the struct
					if (macroBody.getParameter(i).getType().equals("struct lockdep_map *"))
					{
						bodyParameters += "&(" + param + ")->dep_map";
					}
					
					// For spinlocks, there's a macro where the order of parameters is flipped.
					// This takes care of that case
					else if (param.equals("atomic"))
					{
						bodyParameters += "lock";
					}
					
					// In most cases, just pass in the parameter itself
					else
					{
						bodyParameters += param;
					}
				}
				
				// If there are no more macro parameters, but there are still body parameters
				else
				{
					// The value we pass in to the function depends on the type of the parameter
					String type = macroBody.getParameter(i).getType();
									
					// If the type of the parameter is int, pass in a "0"
					if (type.contains("int"))
					{
						bodyParameters += "0";
					}
					
					else
					{
						bodyParameters += "NULL";
					}
				}
			}
		}
		
		// Return a proper C macro definition
		return "#define " + macroName + "(" + parameters + ") " + macroBody.getName() + "(" + bodyParameters + ")";
	}

	@Override
	public int compareTo(Macro m) {
		return macroName.compareTo(m.macroName);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == this) return true;
		if (o.getClass() != Macro.class) return false;
		
		return this.getName().equals(((Macro) o).getName());
	}
}
