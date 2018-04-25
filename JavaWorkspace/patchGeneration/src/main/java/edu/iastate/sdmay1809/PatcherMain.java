package edu.iastate.sdmay1809;

public class PatcherMain 
{
	public static void main(String[] args) throws Exception
	{
		Patcher patcher = new Patcher(PatchConfig.builder(args).build());
		patcher.patch();
	}
}
