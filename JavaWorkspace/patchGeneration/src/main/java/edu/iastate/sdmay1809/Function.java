package edu.iastate.sdmay1809;

import java.util.ArrayList;
import java.util.Set;

/**
 * This class defines a C function including return type and parameter names and types
 */
public class Function implements Comparable<Function>
{
	// Private variables for the function's return type, name and parameters
	private String returnType;
	private String functionName;
	private ArrayList<Parameter> parameters;
	
	// Constructor. Pass in a line that contains a function declaration
	public Function(String functionLine) throws Exception
	{
		functionName = getFunctionName(functionLine);
		
		if (functionName == null) throw new Exception("String doesn't represent a function definition!");
		
		parameters = new ArrayList<Parameter>();
		
		returnType = functionLine.replaceFirst(functionName + ".*$", "")
								 .replaceFirst("^\\s*(extern\\s+)?(static\\s+)?(inline\\s+)?(?=(struct\\s+)?\\w+(\\s*\\*\\s*|\\s+))", "")
								 .replace("__must_check", "")
								 .trim();
		
		for (String parameter : functionLine.replaceFirst("^.*\\(", "").replaceFirst("\\).*$", "").trim().split(",", -1))
		{
			if (!parameter.trim().equals("")) parameters.add(new Parameter(parameter.trim()));
		}
	}
	
	// Get the function return type
	public String getType()
	{
		return returnType;
	}
	
	// Get the function name
	public String getName()
	{
		return functionName;
	}
	
	// Get an individual parameter at a given index
	public Parameter getParameter(int index)
	{
		if (index >= parameters.size() || index < 0) return null;
		
		return parameters.get(index);
	}
	
	// Get a list of parameters
	public ArrayList<Parameter> getParameters()
	{
		return parameters;
	}
	
	// This function generates a string for declaring a function
	public String convertToStaticInline()
	{
		// Generate the first part of the string
		String line = "static inline " + returnType + " " + functionName + "(";
		
		// For each parameter, print the parameter type and name
		for (int i = 0; i < parameters.size(); i++)
		{
			String type = parameters.get(i).getType();
			String name = parameters.get(i).getName();
			
			if (i > 0) line += ", ";
			
			line += type;
			
			// If the parameter type isn't a pointer, we need space between the type and parameter name
			if (type.charAt(type.length() - 1) != '*') line += " ";
			line += name;
		}
		
		// End of parameters, start of body
		line += "){";
		
		// Empty body
		if (returnType.charAt(returnType.length() - 1) == '*') line += "return NULL;}";	// Pointer return type, return NULL
		else if (returnType.equals("void")) line += "}";								// Void return type, don't return
		else line += "return 0;}";														// Non-pointer return type, return 0
		
		return line;
	}
	
	// This function determines whether a list of functions contains a function with a given name
	public static boolean contains(Set<Function> functions, String name)
	{
		// Iterate through all functions in the list. Return true if the desired name is found
		for (Function f : functions)
		{
			if (f.getName().equals(name)) return true;
		}
		
		return false;
	}

	@Override
	public int compareTo(Function f) {
		return functionName.compareTo(f.functionName);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == this) return true;
		if (o.getClass() != Function.class) return false;
		
		return this.getName().equals(((Function)o).getName());
	}
	
	public static String getFunctionName(String line)
	{
		if (line == null) return null;
		else line = line.replaceAll("__must_check", "");
		if (!line.matches("^\\s*(?!return)(extern\\s+)?(static\\s+)?(inline\\s+)?(struct\\s+)?\\w+(\\s*\\*\\s*|\\s+)\\w+\\s*\\(.*$")) return null;
		return line.replaceFirst("^\\s*(?!return)(extern\\s+)?(static\\s+)?(inline\\s+)?(struct\\s+)?\\w+(\\s*\\*\\s*|\\s+)", "").replaceFirst("\\s*\\(.*$", "").trim();
	}
}
