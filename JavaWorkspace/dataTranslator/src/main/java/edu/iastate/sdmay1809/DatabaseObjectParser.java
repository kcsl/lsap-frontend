package edu.iastate.sdmay1809;

import org.json.JSONObject;

import edu.iastate.sdmay1809.shared.InstanceTracker.InstanceParser;
import edu.iastate.sdmay1809.shared.InstanceTracker.InvalidInstanceFormatException;

public class DatabaseObjectParser implements InstanceParser {
	public JSONObject parseEntry(String instance) throws InvalidInstanceFormatException {
		JSONObject inst = new JSONObject();
		String[] outerGroups = instance.split("(\\]|@)@+(\\[|@)");
		String[] innerGroups = outerGroups[2].replaceAll("@", "/").split("\\+\\/*");

		try {
			inst.put("driver", innerGroups[2].split("\\/", 2)[1].split("\\.")[0]);
			inst.put("filename", innerGroups[2]);
			inst.put("id", outerGroups[1]);
			inst.put("length", Integer.parseInt(innerGroups[1]));
			inst.put("offset", Integer.parseInt(innerGroups[0]));
			inst.put("status", outerGroups[0]);
			inst.put("title", outerGroups[3]);
		} catch(Exception e) {
			for(StackTraceElement s : e.getStackTrace()) {
				System.err.println("\n" + s.toString());
			}
			throw new InvalidInstanceFormatException();
		}

		return inst;
	}

	@Override
	public String getName() {
		return "dboparser";
	}
}
