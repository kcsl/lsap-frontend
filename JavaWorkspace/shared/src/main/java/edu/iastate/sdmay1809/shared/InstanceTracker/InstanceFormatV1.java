package edu.iastate.sdmay1809.shared.InstanceTracker;

import org.json.JSONException;
import org.json.JSONObject;

class InstanceFormatV1 extends InstanceFormat {
	
	InstanceFormatV1() {
		super();
		this.outerGroupRegex = "(\\]|@)@+(\\[|@)";
		this.innerGroupRegex = "\\+\\/*";
	}

	@Override
	JSONObject parseEntry(String instance) throws InvalidInstanceFormatException {
		JSONObject inst = new JSONObject();
		String[] outerGroups = instance.split(outerGroupRegex);
		String[] innerGroups = outerGroups[2].replaceAll("@", "/").split(innerGroupRegex);

		try {
			inst.put("status", outerGroups[0]);
			inst.put("id", outerGroups[1]);
			inst.put("name", outerGroups[3]);
			inst.put("offset", Integer.parseInt(innerGroups[0]));
			inst.put("length", Integer.parseInt(innerGroups[1]));
			inst.put("filename", innerGroups[2].split("\\/", 2)[1]);
		} catch(JSONException e) {
			throw new InvalidInstanceFormatException(e.getMessage());
		}

		return inst;
	}

}
