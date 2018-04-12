package edu.iastate.sdmay1809.shared.InstanceTracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.iastate.sdmay1809.shared.Utils;

public class InstanceTracker {

	String sourceDirectory;

	public InstanceTracker(String sourceDirectory) {
		InstanceParserManager.put(new InstanceParserV1());
		InstanceParserManager.put(new InstanceParserV2());
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

	protected JSONObject parseEntry(String instance) throws InvalidInstanceFormatException, Exception {
		return parseEntry(instance, null, true);
	}
	
	protected JSONObject parseEntry(String instance, String parser) throws InvalidInstanceFormatException, Exception {
		return parseEntry(instance, parser, true);
	}
	
	protected JSONObject parseEntry(String instance, String parser, boolean tryAllParsers) throws InvalidInstanceFormatException, Exception {
		InstanceParser p = InstanceParserManager.get(parser);
		if(p == null) {
			return parseEntryAll(instance);
		}
		
		try {
			return p.parseEntry(instance);
		} catch (InvalidInstanceFormatException e) {
			if(tryAllParsers) {
				System.err.println("[WARN] : Couldn't Parse Entry with Parser " + p.getName() + ", trying all parsers");
				return parseEntryAll(instance);
			} else {
				throw new InvalidInstanceFormatException(e.getMessage());
			}	
		}
	}
	
	private JSONObject parseEntryAll(String instance) throws Exception {
		JSONObject inst = null;
		for(InstanceParser p : InstanceParserManager.getParsers()) {
			try {
				inst = p.parseEntry(instance);
				break;
			} catch (InvalidInstanceFormatException e) {
				System.err.println("[WARN] : Couldn't Parse Entry with Parser " + p.getName());
			}
		}
		
		if(inst == null) {
			throw new Exception("No Parsers could parse this entry: " + instance);
		}
		
		return inst;
	}
}
