package edu.iastate.sdmay1809.shared.InstanceTracker;

import org.json.JSONObject;

public interface InstanceParser {
	JSONObject parseEntry(String instance) throws InvalidInstanceFormatException;
}
