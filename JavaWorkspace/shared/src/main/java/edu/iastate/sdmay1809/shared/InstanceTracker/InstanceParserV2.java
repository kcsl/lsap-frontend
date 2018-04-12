package edu.iastate.sdmay1809.shared.InstanceTracker;

import org.json.JSONObject;

public class InstanceParserV2 implements InstanceParser {
	
	private String outerGroupRegex;
	private String innerGroupRegex;
	
	public InstanceParserV2() {
		this.outerGroupRegex = "(?:\\]|@)@+(?:\\[|@)";
		this.innerGroupRegex = "(?:,?[a-z]{1}:|\\+)";
	}

	@Override
	public String getName() {
		return "V2";
	}

	@Override
	public JSONObject parseEntry(String instance) throws InvalidInstanceFormatException {
		JSONObject inst = new JSONObject();
		String[] outerGroups = instance.split(outerGroupRegex);
		String[] innerGroups = outerGroups[2].replaceAll("@", "/").split(innerGroupRegex);
		
		try {
			inst.put("status", outerGroups[0]);
			inst.put("id", outerGroups[1]);
			inst.put("name", outerGroups[3]);
			inst.put("offset", Integer.parseInt(innerGroups[2]));
			inst.put("length", Integer.parseInt(innerGroups[3]));
			inst.put("length2", Integer.parseInt(innerGroups[1]));
			inst.put("filename", innerGroups[0].split("\\/", 2)[1]);
		} catch (Exception e) {
			throw new InvalidInstanceFormatException(e.getMessage());
		}
		
		return inst;
	}
}
