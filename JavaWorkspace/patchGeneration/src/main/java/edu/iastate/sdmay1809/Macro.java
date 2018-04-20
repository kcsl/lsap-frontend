package edu.iastate.sdmay1809;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Macro implements Locker
{
	public static PatchConfig config;
	
	private String name;
	private List<String> parameters;
	private Function bodyFunction;
	
	public Macro(String macroDefinition, Function bodyFunction) throws Exception
	{
		this(macroDefinition);
		this.bodyFunction = bodyFunction;
	}
	
	public Macro(String macroDefinition) throws Exception
	{
		if (macroDefinition == null) throw new Exception("MACRO: Unable to find a macro definition in \"" + macroDefinition + "\"");
				
		Matcher m = Pattern.compile("^\\s*#\\s*[defineDEFINE]{6}\\s+\\w+\\s*\\([^\\)]*\\)").matcher(macroDefinition);
		if (!m.find()) throw new Exception("MACRO: Unable to find a macro definition in \"" + macroDefinition + "\"");
		
		macroDefinition = m.group().replace("\n", " ");
		parameters = new ArrayList<String>();
		
		name = macroDefinition.replaceFirst("^\\s*#\\s*[defineDEFINE]{6}\\s+", "").replaceFirst("\\([^\\)]*\\).*$", "").trim();
		String[] paramList = macroDefinition.replaceFirst("^.*" + name + "\\s*\\(", "").replaceFirst("\\).*$", "").trim().split(",");
		
		for (String param : paramList)
		{
			if (param.trim().length() > 0) parameters.add(param.trim());
		}
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public String getParameter(int i)
	{
		if (i < 0 || i >= parameters.size()) return null;
		
		return parameters.get(i);
	}
	
	public List<String> getParameters()
	{
		return parameters;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null) return false;
		if (this == o) return true;
		if (!Locker.class.isInstance(o)) return false;
		if (Macro.class.isInstance(o))
		{
			if (parameters.size() != ((Macro) o).parameters.size()) return false;
			for (int i = 0; i < parameters.size(); i++)
			{
				if (!parameters.get(i).equals(((Macro) o).parameters.get(i))) return false;
			}
			
			return true;
		}
		return this.name.equals(((Locker)o).getName());
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        return result;
	}
	
	public static boolean isLockingMacro(Macro m, Map<String, Boolean> criteria)
	{
		for (String s : criteria.keySet())
		{
			if (m.getName().contains(s) != criteria.get(s)) return false;
		}
		
		return true;
	}
	
	@Override
	public String toString()
	{
		String string = "#define " + name + "(" + String.join(", ", parameters) + ")";
		
		if (bodyFunction != null)
		{
			string += " " + bodyFunction.getName() + "(";
			
			for (int i = 0; i < bodyFunction.getParameters().size(); i++)
			{
				Parameter p = bodyFunction.getParameter(i);
				if (p.getType().contains("*")) string += "((void*) 0)";
				else string += "0";
				
				if (i != bodyFunction.getParameters().size() - 1) string += ", ";
			}
			
			string += ")";
		}
		
		return string;
	}
	
	public void setBodyFunction(Function f)
	{
		bodyFunction = f;
	}
}
