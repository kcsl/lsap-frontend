package edu.iastate.sdmay1809;

import org.json.JSONObject;

import edu.iastate.sdmay1809.shared.InstanceTracker.InstanceParserV2;
import edu.iastate.sdmay1809.shared.InstanceTracker.InvalidInstanceFormatException;

public class DatabaseObjectParserV2 extends InstanceParserV2 {
	public JSONObject parseEntry(String instance) throws InvalidInstanceFormatException {
		JSONObject inst = super.parseEntry(instance);
		inst.put("instance_id", inst.get("id"));
		inst.remove("id");
		inst.put("title", inst.get("name"));
		inst.put("driver", ((String) inst.get("filename")).split("\\/", 2)[1].split("\\.")[0]);
		return inst;
	}
}
