package edu.iastate.sdmay1809.shared;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;

public class Utils {

	@SafeVarargs
	public static <T> T coalesce(T... params) {
		if (params == null) {
			return null;
		}
		for (T param : params) {
			if (param != null) {
				return param;
			}
		}
		return null;
	}

	public static <T> T[] concatenate(T[] a, T[] b) {
		if (a == null && b == null) {
			return null;
		}

		int aLen = (a != null) ? a.length : 0;
		int bLen = (b != null) ? b.length : 0;
		
		if (a == null) {
			@SuppressWarnings("unchecked")
			T[] c = (T[]) Array.newInstance(b.getClass().getComponentType(), aLen + bLen);
			System.arraycopy(b, 0, c, 0, bLen);
			return c;
		} else if (b == null) {
			@SuppressWarnings("unchecked")
			T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
			System.arraycopy(a, 0, c, 0, aLen);
			return c;
		} else {
			@SuppressWarnings("unchecked")
			T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
			System.arraycopy(a, 0, c, 0, aLen);
			System.arraycopy(b, 0, c, aLen, bLen);
			return c;
		}
	}
	
	public static String execute(String[] commands, File dir) throws IOException, InterruptedException {
		StringBuilder sb = new StringBuilder();
		String line = null;
		
		sb.append("Performing command: ");
		for(String command : commands) {
			sb.append(command + " ");
		}
		sb.append("\n");
		
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(commands, null, dir);
		BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		
		while((line = input.readLine()) != null) {
			sb.append(line + "\n");
		}
		
		int exitValue = pr.waitFor();
		sb.append("\nProcess exited with value " + exitValue + "\n");
		
		input.close();
		
		return sb.toString();
	}
	
	public static int levenshteinDistance (CharSequence lhs, CharSequence rhs) {                          
	    int lLen = lhs.length() + 1;                                                     
	    int rLen = rhs.length() + 1;                                                     
	                                                                                    
	    // the array of distances                                                       
	    int[] cost = new int[lLen];                                                     
	    int[] newCost = new int[lLen];                                                  
	                                                                                    
	    // initial cost of skipping prefix in String s0                                 
	    for (int i = 0; i < lLen; i++) cost[i] = i;                                     
	                                                                                    
	    // dynamically computing the array of distances                                  
	                                                                                    
	    // transformation cost for each letter in s1                                    
	    for (int j = 1; j < rLen; j++) {                                                
	        // initial cost of skipping prefix in String s1                             
	        newCost[0] = j;                                                             
	                                                                                    
	        // transformation cost for each letter in s0                                
	        for(int i = 1; i < lLen; i++) {                                             
	            // matching current letters in both strings                             
	            int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;             
	                                                                                    
	            // computing cost for each transformation                               
	            int costReplace = cost[i - 1] + match;                                 
	            int costInsert  = cost[i] + 1;                                         
	            int costDelete  = newCost[i - 1] + 1;                                  
	                                                                                    
	            // keep minimum cost                                                    
	            newCost[i] = Math.min(Math.min(costInsert, costDelete), costReplace);
	        }                                                                           
	                                                                                    
	        // swap cost/newcost arrays                                                 
	        int[] swap = cost; 
	        cost = newCost; 
	        newCost = swap;                          
	    }                                                                               
	                                                                                    
	    // the distance is the cost for transforming all letters in both strings        
	    return cost[lLen - 1];                                                          
	}
}
