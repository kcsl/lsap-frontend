package edu.iastate.sdmay1809.shared.InstanceTracker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InstanceParserManager {
	
	private static Map<String, InstanceParser> parserMap = new HashMap<String, InstanceParser>();
	
	public static InstanceParser put(String key, InstanceParser parser) {
		InstanceParser prev = parserMap.get(key);
		parserMap.put(key, parser);
		return prev;
	}
	
	public static InstanceParser get(String key) {
		return parserMap.get(key);
	}
	
	public static Collection<InstanceParser> getParsers() {
		return parserMap.values();
	}
}
