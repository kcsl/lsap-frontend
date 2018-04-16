package edu.iastate.sdmay1809;

public class PatcherMain 
{
	public static void main(String[] args) throws Exception
	{
		Patcher patcher = new Patcher("resources/patcherConfig.json", "resources/", "resources/testing/patch/real/", false, true);
		patcher.patch();		
	}
}
