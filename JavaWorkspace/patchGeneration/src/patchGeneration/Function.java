package patchGeneration;

import java.util.ArrayList;
import java.util.List;

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
	public Function(String functionLine)
	{
		parameters = new ArrayList<Parameter>();
		
		// Split the line into two parts: the function name and return type, and it's parameters
		String[] functionParts = functionLine.replace("//", "").split("\\(", 2);
		String[] parameterList = functionParts[1].substring(0, functionParts[1].indexOf(")")).split(",");
		
		// For each parameter in the string array, add it to the parameter list
		for (String parameter : parameterList)
		{
			parameters.add(new Parameter(parameter));
		}
		
		// index represents the location of the break between the return type and the function name
		int index = functionParts[0].lastIndexOf(" ");
		
		// Set the function name and return type
		returnType = functionParts[0].substring(0, index).trim();
		functionName = functionParts[0].substring(index + 1, functionParts[0].length()).trim();
		
		// Some functions contain extern and __must_check. Remove these parts and extra spaces
		if (returnType.contains("extern")) returnType = returnType.replace("extern", "").trim();
		if (returnType.contains("__must_check")) returnType = returnType.replace("__must_check", "").trim();		
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
		
		// Empty body. Return  0 for int functions, do nothing otherwise
		if (returnType.equals("void")) line += "}";
		else line += "return 0;}";
		
		return line;
	}
	
	// This function determines whether a list of functions contains a function with a given name
	public static boolean contains(List<Function> functions, String name)
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
}
