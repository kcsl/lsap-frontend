package edu.iastate.sdmay1809.shared.InstanceTracker;

import org.json.JSONObject;

public class InstanceParserV2 implements InstanceParser {
	
	private String outerGroupRegex;
	private String innerGroupRegex;
	
	public InstanceParserV2() {
		this.outerGroupRegex = "(?:\\]|@)@+(?:\\[|@)";
		this.innerGroupRegex = "(?:,[a-z]{1}(?::|_)|\\+)";
	}

	@Override
	public String getName() {
		return "V2";
	}

	@Override
	public JSONObject parseEntry(String instance) throws InvalidInstanceFormatException {
		JSONObject inst = new JSONObject();
		
		String[] outerGroups = instance.split(outerGroupRegex);
		String base = "," + outerGroups[2].replaceAll("@", "/");
		String[] innerGroups = base.split(innerGroupRegex);
		
		try {
			inst.put("status", outerGroups[0]);
			inst.put("id", outerGroups[1]);
			inst.put("name", outerGroups[3]);
			inst.put("offset", Integer.parseInt(innerGroups[3]));
			inst.put("length", Integer.parseInt(innerGroups[4]));
			inst.put("length2", Integer.parseInt(innerGroups[2]));
			inst.put("filename", innerGroups[1].split("\\/", 3)[2]);
		} catch (Exception e) {
			for(StackTraceElement s: e.getStackTrace()) {
				System.err.println(s.toString());
			}
			throw new InvalidInstanceFormatException(e.getMessage());

		}
		
		return inst;
	}
}
