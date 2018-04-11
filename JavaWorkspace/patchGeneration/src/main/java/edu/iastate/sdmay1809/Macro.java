package edu.iastate.sdmay1809;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class defines a C macro including macro name, parameters, and macro body
 */
public class Macro implements Comparable<Macro> {
	
	// Private variables for the macro's name, parameters, and body
	private String macroName;
	private List<String> params;
	
	// Assume line was found to be a define macro
	public Macro(String line) throws Exception
	{
		macroName = getMacroName(line);
		
		if (macroName == null) throw new Exception("String doesn't represent a macro definition!");
		
		params = new ArrayList<String>();
		
		for (String p : line.replaceFirst("^\\s*#\\s*[Dd][Ee][Ff][Ii][Nn][Ee]\\s(\\w+)\\(", "").replaceFirst("\\).*$", "").split(",", -1))
		{
			if (!p.trim().equals("")) params.add(p.trim());
		}
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
	public List<String> getParameters()
	{
		return params;
	}
			
	// This function determines whether a list of macros contains a macro with a given name
	public static boolean contains(Set<Macro> macros, String name)
	{
		// Iterate through the list and return true if a macro with the given name is found
		for (Macro m : macros)
		{
			if (m.getName().equals(name)) return true;
		}
		
		return false;
	}
	
	// This function generates a proper C macro definition
	public String printAsDefine(Function macroBody)
	{
		// Parameters for the macro and it's body function
		String bodyParameters = "";
		
		// If there's no body for the macro, return null
		if (macroBody == null) return null;
		
		for (int i = 0; i < macroBody.getParameters().size(); i++)
		{
			String type = macroBody.getParameter(i).getType();
			if (type.charAt(type.length() - 1) == '*') bodyParameters += "NULL";	// Parameter is a pointer, pass NULL
			else bodyParameters += "0";												// Parameter isn't a pointer, pass 0
			
			if (i != macroBody.getParameters().size() - 1) bodyParameters += ", ";
		}
		
		// Return a proper C macro definition
		return "#define " + macroName + "(" + String.join(", ", params) + ") " + macroBody.getName() + "(" + bodyParameters + ")";
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
	
	public static String getMacroName(String line)
	{
		if (line == null || !line.matches("^\\s*#\\s*[Dd][Ee][Ff][Ii][Nn][Ee]\\s+\\w+\\s*\\(.*\\).*$")) return null;
		return line.replaceFirst("^\\s*#\\s*[Dd][Ee][Ff][Ii][Nn][Ee]\\s+", "").replaceFirst("\\(.*$", "").trim();
	}
}
