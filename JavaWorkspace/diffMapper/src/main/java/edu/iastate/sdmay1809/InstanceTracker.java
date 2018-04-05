package edu.iastate.sdmay1809;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class InstanceTracker {

	String sourceDirectory;

	public InstanceTracker(String sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public boolean run(String outputDirectory, boolean outputCheckOverride) {
		File[] instances = getInstances();
		String outputJSONFile;
		boolean retVal = true;

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
		JSONArray instanceMap = createInstanceMap(instances);

		System.out.println("instances: " + instances.length + ", instanceMap:" + instanceMap.length());

		try {
			writeMap(instanceMap, outputJSONFile);
		} catch (IOException io) {
			System.err.println("[FATAL] : Could not create oldInstanceMap.json in " + outputDirectory);
			retVal = false;
		} 
		return retVal;
	}
	
	protected void writeMap(JSONArray instanceMap, String outputJSONFile) throws IOException{
		FileWriter fw = new FileWriter(new File(outputJSONFile), false);
		instanceMap.write(fw);
		fw.close();
	}

	protected JSONArray createInstanceMap(File[] instances) {
		JSONArray instanceMap = new JSONArray();
		for (File f : instances) {
			try {
				JSONObject instance = parseEntry(f.getName());
				instanceMap.put(instance);
			} catch (Exception e) {
				System.err.println("[ERROR] : Threw exception when parsing " + f.getName());
			}
		}
		return instanceMap;
	}
	
	protected File[] getInstances() {
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

	protected JSONObject parseEntry(String instance) throws Exception {
		JSONObject inst = new JSONObject();
		String[] outerGroups = instance.split("(\\]|@)@+(\\[|@)");
		String[] innerGroups = outerGroups[2].replaceAll("@", "/").split("\\+\\/*");

		try {
			inst.put("status", outerGroups[0]);
			inst.put("id", outerGroups[1]);
			inst.put("name", outerGroups[3]);
			inst.put("offset", Integer.parseInt(innerGroups[0]));
			inst.put("length", Integer.parseInt(innerGroups[1]));
			inst.put("filename", innerGroups[2].split("\\/", 2)[1]);
		} catch(Exception e) {
			throw new Exception(e.getMessage());
		}

		return inst;
	}
}
