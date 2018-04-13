package edu.iastate.sdmay1809;

public class Parameter {
	private String type;
	private String name;
	
	public Parameter(String fullParam) throws Exception
	{
		try
		{
			name = fullParam.trim().replaceFirst("^.*(\\*|[ ])", "").trim();
			type = fullParam.trim().replaceFirst("\\w+$", "").replaceFirst("\\s*\\*\\s*", "*").trim();
		}
		
		catch (Exception e)
		{
			throw new Exception("PARAMETER: Unable to parse parameter from " + fullParam + "!");
		}
		
		if (name.length() == 0 || type.length() == 0) throw new Exception("PARAMETER: Unable to parse parameter from " + fullParam + "!");
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
