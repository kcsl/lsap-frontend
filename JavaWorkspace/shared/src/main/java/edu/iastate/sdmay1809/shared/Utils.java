package edu.iastate.sdmay1809.shared;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

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
		for (String command : commands) {
			sb.append(command + " ");
		}
		sb.append("\n");

		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(commands, null, dir);
		BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

		while ((line = input.readLine()) != null) {
			sb.append(line + "\n");
		}

		int exitValue = pr.waitFor();
		sb.append("\nProcess exited with value " + exitValue + "\n");

		input.close();

		return sb.toString();
	}

	public static Map<String, Object> convertToMap(JSONObject obj) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (obj == null) {
			return map;
		}

		Iterator<String> it = obj.keys();

		while (it.hasNext()) {
			// Get the key and value stored at this point
			String key = it.next();
			Object val = obj.get(it.next());

			// Convert it to List/Map equivalent if it is a JSON variant
			Object eq = val;
			if (val instanceof JSONArray) {
				eq = Utils.convertToList((JSONArray) val);
			} else if (val instanceof JSONObject) {
				eq = Utils.convertToMap((JSONObject) val);
			}

			// Store it in the map
			map.put(key, eq);
		}

		return map;
	}

	public static List<Object> convertToList(JSONArray arr) {
		List<Object> list = new ArrayList<Object>();

		if (arr == null) {
			return list;
		}

		int arrLength = arr.length();

		for (int i = 0; i < arrLength; i++) {
			// Get the object at each index
			Object val = arr.get(i);
			// Convert it to List/Map equivalent if it is a JSON variant
			Object eq = val;
			if (val instanceof JSONArray) {
				eq = Utils.convertToList((JSONArray) val);
			} else if (val instanceof JSONObject) {
				eq = Utils.convertToMap((JSONObject) val);
			}
			
			// Store it in the list
			list.add(eq);
		}

		return list;
	}
	
	public static Map<String, Object> insert(String key, Object value, Map<String, Object> map) {
		String[] parts = key.split("\\.");
		Map<String, Object> retMap = Utils.coalesce(map, new HashMap<String, Object>());
		
		if(parts.length > 1) {
			
		} else {
			
		}
	}
}
