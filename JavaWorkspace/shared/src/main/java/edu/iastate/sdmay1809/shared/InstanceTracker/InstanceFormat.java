package edu.iastate.sdmay1809.shared.InstanceTracker;

import org.json.JSONObject;

public abstract class InstanceFormat {

	String outerGroupRegex;
	String innerGroupRegex;
	
	abstract JSONObject parseEntry(String instance) throws InvalidInstanceFormatException;
}
