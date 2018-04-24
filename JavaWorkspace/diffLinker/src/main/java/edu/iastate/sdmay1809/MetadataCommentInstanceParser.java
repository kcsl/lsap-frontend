package edu.iastate.sdmay1809;

import org.json.JSONObject;

import edu.iastate.sdmay1809.shared.InstanceTracker.InstanceParser;
import edu.iastate.sdmay1809.shared.InstanceTracker.InvalidInstanceFormatException;

public class MetadataCommentInstanceParser implements InstanceParser {

	private String name;

	public MetadataCommentInstanceParser() {
		this.name = "MetadataCommentInstanceParser";
	}

	public MetadataCommentInstanceParser(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public JSONObject parseEntry(String instance) throws InvalidInstanceFormatException {
		JSONObject obj = null;
		if (instance.trim().matches("\\/\\* (?:[a-zA-Z0-9\\.\\/\\_-]+@@@){5}[a-zA-Z0-9\\.\\/\\_-]+ \\*\\/")) {
			try {
				String[] commentSplit = instance.trim().split(" ");
				String[] dataSplit = commentSplit[1].split("@@@");
				obj = new JSONObject();

				obj.put("id", dataSplit[0]).put("name", dataSplit[5]).put("status", dataSplit[1])
						.put("filename", dataSplit[4]).put("offset", Integer.parseInt(dataSplit[2]))
						.put("length", Integer.parseInt(dataSplit[3]));
			} catch (Exception e) {
				throw new InvalidInstanceFormatException(e.getMessage());
			}
		} else {
			throw new InvalidInstanceFormatException();
		}

		return obj;
	}

}
