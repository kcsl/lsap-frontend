package edu.iastate.sdmay1809;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;

import edu.iastate.sdmay1809.shared.DiffConfig;
import edu.iastate.sdmay1809.shared.InstanceTracker.InstanceParserManager;
import edu.iastate.sdmay1809.shared.InstanceTracker.InstanceTracker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataBaseFileTranslator extends InstanceTracker {

	String versionNum;
	
	public DataBaseFileTranslator(String sourceDirectory) {
		super(sourceDirectory);
		InstanceParserManager.put(new DatabaseObjectParser());
		File f = new File(sourceDirectory);
		DiffConfig config = DiffConfig.builder(	f.getParentFile().getParent() + "/config.json").build();
		versionNum = config.NEW_TAG.replaceAll("\\-|\\.", "");
	}
	
	@Override
	public boolean run(File outputFile, boolean outputCheckOverride) {
		File outputJSONFile = new File(outputFile + "/db_" + versionNum + ".json");

		// Don't run if file exists and override is false
		if (outputJSONFile.exists() && !outputCheckOverride) {
			System.out.println("Skipping data translation since the file already exists!");
			return false;
		}
		File[] instances = getInstances();
		boolean retVal = true;

		
		JSONObject versionObject = createVersionObject(instances);	

		System.out.println("instances: " + instances.length + ", instanceMap:" + versionObject.length());

		try {
			writeVersionObject(versionObject, outputJSONFile.getAbsolutePath());
		} catch (IOException io) {
			System.err.println("[FATAL] : Could not create database json file in " + outputFile.getAbsolutePath());
			retVal = false;
		} 
		return retVal;
	}
	
	protected void writeVersionObject(JSONObject versionObject, String outputJSONFile) throws IOException{
		JSONObject toWrite = new JSONObject();
		toWrite.put(versionNum, versionObject);
		FileWriter fw = new FileWriter(new File(outputJSONFile), false);
		toWrite.write(fw);
		fw.close();
	}	
	
	protected JSONObject createVersionObject(File[] instances) {
		JSONObject versionObject = new JSONObject();
		for (File f : instances) {
			try {
				JSONObject cfg = new JSONObject();
				JSONObject pcg = new JSONObject();
				JSONObject instance = parseEntry(f.getName(), "dboparser");
				instance.put("type", f.getParentFile().getName());
				String pathTo = versionNum + "/" + instance.getString("type") + "/" + instance.getString("id") + "/";
				instance.put("mpg", pathTo + "mpg.png");
				Map<String, String> graphToUUID = new HashMap<String,String>();
				for(File graph : f.listFiles()) {
					if((!graph.getName().startsWith("EFG") &&
							!graph.getName().startsWith("CFG") &&
							!graph.getName().startsWith("PCG")) ||
							!graph.getName().endsWith(".png"))
						continue;
					String graphID = graph.getName().split("\\[|\\]")[1];
					String graphType = graph.getName().substring(0, 3);
					String uuid;
					if(graphToUUID.containsKey(graphID))
						uuid = graphToUUID.get(graphID);
					else {
						uuid = UUID.randomUUID().toString();
						graphToUUID.put(graphID, uuid);
					}
					String fixedType = graphType.equals("CFG") ? "cfg" : "pcg";
					String dir = versionNum + "/" + instance.getString("type") + "/" +
						instance.getString("id") + "/" + fixedType + "_" + uuid + ".png";
					if(graphType.toLowerCase().equals("cfg")) {
						cfg.put(uuid, dir);
					}
					else if(graphType.toLowerCase().equals("pcg") ||
							graphType.toLowerCase().equals("efg")) {
						pcg.put(uuid, dir);
					}
				}
				instance.put("cfg", cfg);
				instance.put("pcg", pcg);
				versionObject.put(instance.getString("id"), instance);
			} catch (Exception e) {
				System.err.println("[ERROR] : Threw exception when parsing " + f.getName());

			}
		}
		return versionObject;
	}

}
