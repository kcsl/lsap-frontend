package edu.iastate.sdmay1809;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InstanceTracker {

	String sourceDirectory;

	public InstanceTracker(String sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public boolean run(String outputDirectory, boolean outputCheckOverride) {
		JSONArray instanceMap = new JSONArray();
		File[] instances = getInstances();
		String outputJSONFile;
		FileWriter fw = null;

		if (outputDirectory.endsWith("/")) {
			outputJSONFile = outputDirectory + "oldInstanceMap.json";
		} else {
			outputJSONFile = outputDirectory + "/oldInstanceMap.json";
		}

		File check = new File(outputJSONFile);

		// Don't run if file exists and override is false
		if (check.exists() && !outputCheckOverride) {
			System.out.println("Skipping instance tracking since the file already exists!");
			return false;
		}

		for (File f : instances) {
			try {
				JSONObject instance = parseEntry(f.getName());
				instanceMap.put(instance);
			} catch (JSONException je) {
				System.err.println("[ERROR] : Threw exception when parsing " + f.getName());
			}
		}

		System.out.println("instances: " + instances.length + ", instanceMap:" + instanceMap.length());

		try {
			fw = new FileWriter(new File(outputJSONFile), false);
			instanceMap.write(fw);
			fw.close();
		} catch (IOException io) {
			System.err.println("[FATAL] : Could not create oldInstanceMap.json in " + outputDirectory);
		} catch (JSONException je) {
			System.err.println("[FATAL] : Could not write instanceMap to " + outputJSONFile);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					System.err.println("[ERROR] : Could not close File Writer!");
				}
			}
		}

		return true;
	}

	private File[] getInstances() {
		File sourceDir = new File(sourceDirectory);
		File[] subSourceDirs = sourceDir.listFiles(new DirectoryFilter());
		File[] combinedDirs = {};
		File[] currDirs;

		for (File dir : subSourceDirs) {
			currDirs = dir.listFiles(new DirectoryFilter());
			combinedDirs = Utils.concatenate(combinedDirs, currDirs);
		}

		return combinedDirs;
	}

	private JSONObject parseEntry(String instance) throws JSONException {
		JSONObject inst = new JSONObject();
		String[] outerGroups = instance.split("(\\]|@)@+(\\[|@)");
		String[] innerGroups = outerGroups[2].replaceAll("@", "/").split("\\+\\/*");

		inst.put("status", outerGroups[0]);
		inst.put("id", outerGroups[1]);
		inst.put("name", outerGroups[3]);
		inst.put("offset", Integer.parseInt(innerGroups[0]));
		inst.put("length", Integer.parseInt(innerGroups[1]));
		inst.put("filename", innerGroups[2].split("\\/", 2)[1]);

		return inst;
	}
}
