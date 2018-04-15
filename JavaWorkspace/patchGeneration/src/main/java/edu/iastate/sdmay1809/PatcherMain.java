package edu.iastate.sdmay1809;

import edu.iastate.sdmay1809.shared.Utils;

public class PatcherMain 
{
	public static void main(String[] args) throws Exception
	{
		Patcher patcher = new Patcher("resources/patcherConfig.json", "resources/", "resources/testing/patch/real/", false, true);
		patcher.patch();
		
//		System.out.println(Utils.levenshteinDistance("THE BROWN FOX JUMPED OVER THE RED COW", "THE RED COW JUMPED OVER THE GREEN CHICKEN"));
//		System.out.println(Utils.levenshteinDistance("THE BROWN FOX JUMPED OVER THE RED COW", "THE RED COW JUMPED OVER THE RED COW"));
//		System.out.println(Utils.levenshteinDistance("THE BROWN FOX JUMPED OVER THE RED COW", "THE RED FOX JUMPED OVER THE BROWN COW"));
	}
	

}
