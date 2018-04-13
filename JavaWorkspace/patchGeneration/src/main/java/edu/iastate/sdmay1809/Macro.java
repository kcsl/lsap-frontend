package edu.iastate.sdmay1809;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.javatuples.Pair;

public class Macro implements Locker
{
	private String name;
	private List<String> parameters;
	
	public Macro(String macroDefinition) throws Exception
	{
		if (macroDefinition == null) throw new Exception("Unable to find a macro definition in \"" + macroDefinition + "\"");
				
		Matcher m = Pattern.compile("^\\s*#\\s*define\\s+\\w+\\s*\\([^\\)]*\\)").matcher(macroDefinition.toLowerCase());
		if (!m.find()) throw new Exception("Unable to find a macro definition in \"" + macroDefinition + "\"");
		
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
			
		return this.name.equals(((Locker)o).getName());
	}
	
	public static boolean isLockingMacro(Macro m, Set<Pair<String, Boolean>> criteria)
	{
		for (Pair<String, Boolean> c : criteria)
		{
			if (m.getName().contains(c.getValue0()) != c.getValue1()) return false;
		}
		
		return true;
	}
}
