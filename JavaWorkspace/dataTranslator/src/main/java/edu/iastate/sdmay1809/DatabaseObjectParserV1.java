package edu.iastate.sdmay1809;

import org.json.JSONObject;

import edu.iastate.sdmay1809.shared.InstanceTracker.InstanceParserV1;
import edu.iastate.sdmay1809.shared.InstanceTracker.InvalidInstanceFormatException;

public class DatabaseObjectParserV1 extends InstanceParserV1 {
	public JSONObject parseEntry(String instance) throws InvalidInstanceFormatException {
		JSONObject inst = super.parseEntry(instance);
		inst.put("instance_id", inst.get("id"));
		inst.remove("id");
		inst.put("title", inst.get("name"));
		inst.remove("name");
		String [] list = ((String) inst.get("filename")).split("\\/");
		inst.put("driver", list[list.length-1]);
		return inst;
	}
}
