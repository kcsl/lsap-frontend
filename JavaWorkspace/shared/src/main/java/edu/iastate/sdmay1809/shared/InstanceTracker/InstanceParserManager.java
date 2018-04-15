package edu.iastate.sdmay1809.shared.InstanceTracker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InstanceParserManager {
	
	private static Map<String, InstanceParser> parserMap = new HashMap<String, InstanceParser>();
	
	public static InstanceParser put(InstanceParser parser) {
		if(parser == null) {
			return null;
		}
		
		return parserMap.put(parser.getName(), parser);
	}
	
	public static InstanceParser get(String key) {
		if(key == null) {
			return null;
		}
		
		return parserMap.get(key);
	}
	
	public static void clear() {
		parserMap.clear();
	}
	
	public static Collection<InstanceParser> getParsers() {
		return parserMap.values();
	}
}
