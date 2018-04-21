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
			Object val = obj.get(key);

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

	@SuppressWarnings("unchecked")
	public static <T> boolean contains(T container, String key, int idx) {
		if (container == null) {
			throw new IllegalArgumentException("Container must not be null!");
		}
		if (!(container instanceof List) && !(container instanceof Map)) {
			throw new IllegalArgumentException("Container must be a map or list!");
		}

		if (container instanceof List) {
			List<Object> list = (List<Object>) container;
			if (list.size() <= idx || idx < 0) {
				return false;
			} else {
				return list.get(idx) != null;
			}
		} else {
			if (key == null) {
				return false;
			} else {
				Map<String, Object> map = (Map<String, Object>) container;
				return map.containsKey(key);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Object get(T container, String key, int idx) {
		if (container == null) {
			throw new IllegalArgumentException("Container must not be null!");
		}
		if (!(container instanceof List) && !(container instanceof Map)) {
			throw new IllegalArgumentException("Container must be a map or list!");
		}

		if (container instanceof List) {
			List<Object> list = (List<Object>) container;
			if (list.size() <= idx || idx < 0) {
				return null;
			} else {
				return list.get(idx);
			}
		} else {
			if (key == null) {
				return null;
			} else {
				Map<String, Object> map = (Map<String, Object>) container;
				return map.get(key);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T put(T container, String key, int idx, Object value) {
		if (container == null) {
			throw new IllegalArgumentException("Container must not be null!");
		}
		if (!(container instanceof List) && !(container instanceof Map)) {
			throw new IllegalArgumentException("Container must be a map or list!");
		}

		if (container instanceof List) {
			List<Object> list = (List<Object>) container;
			if (idx < 0) {
				return container;
			} else if (list.size() <= idx) {
				list.add(idx, value);
				container = (T) list;
				return container;
			} else {
				if (list.get(idx) != null) {
					// TODO: Warn about overriding!
					return container;
				}
				list.set(idx, value);
				return container;
			}
		} else {
			if (key == null) {
				return container;
			} else {
				Map<String, Object> map = (Map<String, Object>) container;
				if (map.containsKey(key)) {
					// TODO: Warn about overriding!
					return container;
				}
				map.put(key, value);
				return container;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T insert(String key, Object value, T container) {
		String[] parts = key.split("\\.", 2);
		if (container == null) {
			throw new IllegalArgumentException("Container must not be null!");
		}
		if (!(container instanceof List) && !(container instanceof Map)) {
			throw new IllegalArgumentException("Container must be a map or list!");
		}
		
		int idx = -1;
		try {
			idx = Integer.parseInt(parts[0]);
		} catch (Exception e) {
			idx = -1;
		}

		if (parts.length > 1) {
			String[] innerParts = parts[1].split("\\.", 2);
			int idx2 = -1;
			try {
				idx2 = Integer.parseInt(innerParts[0]);
			} catch (Exception e) {
				idx2 = -1;
			}
			
			
			Object val;
			if (Utils.<T>contains(container, parts[0], idx2)) {
				val = Utils.<T>get(container, parts[0], idx2);
				if (idx2 == -1 && val instanceof Map) {
					Map<String, Object> innerMap = (Map<String, Object>) val;
					innerMap = Utils.<Map<String, Object>>insert(parts[1], value, innerMap);
					val = (Object) innerMap;
				} else if (idx2 != -1 && val instanceof List) {
					List<Object> innerList = (List<Object>) val;
					innerList = Utils.<List<Object>>insert(parts[1], value, innerList);
					val = (Object) innerList;
				}
			} else {
				if (idx2 == -1) {
					Map<String, Object> innerMap = new HashMap<String, Object>();
					innerMap = Utils.<Map<String, Object>>insert(parts[1], value, innerMap);
					val = (Object) innerMap;
				} else {
					List<Object> innerList = new ArrayListAnySize<Object>(idx2+1);
					innerList = Utils.<List<Object>>insert(parts[1], value, innerList);
					val = (Object) innerList;
				}
			}
			Utils.<T>put(container, parts[0], idx, val);
		} else {
			Utils.<T>contains(container, parts[0], 0);
			if (Utils.<T>contains(container, parts[0], 0)) {
				// TODO: Warn user about overriding property!
			} else {
				Utils.<T>put(container, parts[0], idx, value);
			}
		}
		
		return container;
	}
}
