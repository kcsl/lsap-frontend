package edu.iastate.sdmay1809;

public class Parameter
{
	private String type;
	private String name;
	
	public Parameter(String fullParam)
	{
		int index = fullParam.length() - 1;
		
		while (index >= 0 && (fullParam.charAt(index) != ' ' && fullParam.charAt(index) != '*'))
		{
			index--;
		}
		
		type = fullParam.substring(0, index + 1).trim();
		name = fullParam.substring(index + 1, fullParam.length()).trim();
	}
	
	public String getType()
	{
		return type;
	}
	
	public String getName()
	{
		return name;
	}
}
