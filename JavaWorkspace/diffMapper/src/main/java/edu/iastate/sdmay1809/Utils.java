package edu.iastate.sdmay1809;

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
}
