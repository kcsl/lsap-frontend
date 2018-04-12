package edu.iastate.sdmay1809;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.iastate.sdmay1809.shared.DiffConfig;
import edu.iastate.sdmay1809.shared.InstanceTracker;

import java.util.UUID;

public class DataBaseFileTranslator extends InstanceTracker {

	String versionNum;
	File workspaceDir;
	
	public DataBaseFileTranslator(String sourceDirectory) {
		super(sourceDirectory);
		workspaceDir = (new File(sourceDirectory)).getParentFile().getParentFile();
		DiffConfig config = DiffConfig.builder(workspaceDir.getAbsolutePath()).build();
		versionNum = config.NEW_TAG.replaceAll("\\-|\\.", "");
	}
	
	@Override
	public boolean run(String outputDirectory, boolean outputCheckOverride) {
		File[] instances = getInstances();
		String outputJSONFile;
		boolean retVal = true;

		outputJSONFile = outputDirectory;
		
		if (!outputDirectory.endsWith("/")) {
			outputJSONFile += "/";
		} 
		
		outputJSONFile += "db_" + versionNum + ".json";

		File check = new File(outputJSONFile);

		// Don't run if file exists and override is false
		if (check.exists() && !outputCheckOverride) {
			System.out.println("Skipping data translation since the file already exists!");
			return false;
		}
		
		if(!createAssetDirectory(outputCheckOverride)) {
			System.err.println("[FATAL] : Could not create asset directory structure in " + outputDirectory);
			return false;
		}
		
		JSONArray instanceMap = createInstanceMap(instances);	

		System.out.println("instances: " + instances.length + ", instanceMap:" + instanceMap.length());

		try {
			writeMap(instanceMap, outputJSONFile);
		} catch (IOException io) {
			System.err.println("[FATAL] : Could not create database json file in " + outputDirectory);
			retVal = false;
		} 
		return retVal;
	}
	
	@Override
	protected void writeMap(JSONArray instanceMap, String outputJSONFile) throws IOException{
		JSONObject toWrite = new JSONObject();
		toWrite.put(versionNum, instanceMap);
		FileWriter fw = new FileWriter(new File(outputJSONFile), false);
		toWrite.write(fw);
		fw.close();
	}
	
	@Override
	protected JSONArray createInstanceMap(File[] instances) {
		JSONArray instanceMap = new JSONArray();
		for (File f : instances) {
			try {
				JSONObject instance = parseEntry(f.getName());
				instance.put("type", f.getParentFile().getName());
				String pathTo = versionNum + "/" + instance.getString("type") + "/" + instance.getString("id") + "/";
				instance.put("mpg", pathTo + "mpg.png");
				instance = addToAssetDirectory(f, instance);
			} catch (Exception e) {
				System.err.println("[ERROR] : Threw exception when parsing " + f.getName());
			}
		}
		return instanceMap;
	}
	
	private boolean createAssetDirectory(boolean outputOverride) {
		File dist = new File(workspaceDir.getAbsolutePath() + "/dist");
		if(!dist.exists()){
			try {
				dist.createNewFile();
			} catch (IOException e) {
				System.err.println("[ERROR] : Could not create dist file");
				return false;
			}
		}
		
		File assets = new File(dist.getAbsolutePath() + "/assets_" + versionNum);
		
		if(assets.exists() && !outputOverride) {
			System.out.println("Skipping writing assets file since the file already exists!");
		} 
		else {
			try {
				assets.createNewFile();
			} catch (IOException e) {
				System.err.println("[ERROR] : Could not create asset file for version " + versionNum );
				return false;
			}
		}
		
		File spin = new File(assets.getAbsolutePath() + "/spin");
		File mutex = new File(assets.getAbsolutePath() + "/mutex");
		
		if(spin.exists() && !outputOverride) {
			System.out.println("Skipping writing spin file since the file already exists!");
		}
		else {
			try {
				spin.createNewFile();
			} catch (IOException e) {
				System.err.println("[ERROR] : Could not create spin file for version " + versionNum );
				return false;
			}
		}
		
		if(mutex.exists() && !outputOverride) {
			System.out.println("Skipping writing mutex file since the file already exists!");
		}
		else {
			try {
				mutex.createNewFile();
			} catch (IOException e) {
				System.err.println("[ERROR] : Could not create mutex file for version " + versionNum );
				return false;
			}
		}
		
		return true;
	}
	
	private JSONObject addToAssetDirectory(File instance, JSONObject toUpdate) {

		
		return null;
	}
	
	@Override
	protected JSONObject parseEntry(String instance) throws Exception {
		JSONObject inst = new JSONObject();
		String[] outerGroups = instance.split("(\\]|@)@+(\\[|@)");
		String[] innerGroups = outerGroups[2].replaceAll("@", "/").split("\\+\\/*");

		try {
			inst.put("driver", innerGroups[2].split("\\/", 2)[1].split(".")[0]);
			inst.put("filename", innerGroups[2]);
			inst.put("id", outerGroups[1]);
			inst.put("length", Integer.parseInt(innerGroups[1]));
			inst.put("offset", Integer.parseInt(innerGroups[0]));
			inst.put("status", outerGroups[0]);
			inst.put("title", outerGroups[3]);
		} catch(Exception e) {
			throw new Exception(e.getMessage());
		}

		return inst;
	}

}
