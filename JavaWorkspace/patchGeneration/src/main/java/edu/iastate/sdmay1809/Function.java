package edu.iastate.sdmay1809;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.javatuples.Pair;

public class Function implements Locker
{
	private String name;
	private String modifiers;
	private List<Parameter> parameters;
	
	public Function(String functionDefinition) throws Exception
	{
		if (functionDefinition == null) throw new Exception("Unable to find a function definition in \"" + functionDefinition + "\"!");
		
		Matcher m = Pattern.compile("^\\s*(\\w+\\s*(\\*)?\\s+(\\*)?\\s*)+\\w+\\s*\\([^\\)]*\\)").matcher(functionDefinition);
		if (!m.find()) throw new Exception("Unable to find a function definition in \"" + functionDefinition + "\"!");
		
		functionDefinition = m.group();
		functionDefinition = functionDefinition.replace("\n", " ");
		parameters = new ArrayList<Parameter>();
		
		name = functionDefinition.replaceFirst("^\\s*(\\w+\\s*(\\*)?\\s+(\\*)?\\s*)+(?=\\w+\\s*\\()", "").replaceFirst("\\([^\\)]*\\)\\s*.*\\s*$", "").trim();
		String[] paramList = functionDefinition.replaceFirst("^.*" + name + "\\s*\\(", "").replaceFirst("\\).*$", "").trim().split(",");
		
		for (String param : paramList)
		{
			if (param.trim().length() > 0) parameters.add(new Parameter(param));
		}
		
		modifiers = functionDefinition.replaceFirst(name + ".*$", "").trim();
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public String getModifiers()
	{
		return modifiers;
	}
	
	public Parameter getParameter(int i)
	{
		if (i < 0 || i >= parameters.size()) return null;
		return parameters.get(i);
	}
	
	public List<Parameter> getParameters()
	{
		return parameters;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null) return false;
		if (this == o) return true;
		if (!Locker.class.isInstance(o)) return false;
		return name.equals(((Locker) o).getName());
	}
	
	public static boolean isLockingFunction(Function f, Set<Pair<String, Boolean>> criteria)
	{
		for (Pair<String, Boolean> c : criteria)
		{
			if (f.getName().contains(c.getValue0()) != c.getValue1()) return false;
		}
		
		return true;
	}
}
