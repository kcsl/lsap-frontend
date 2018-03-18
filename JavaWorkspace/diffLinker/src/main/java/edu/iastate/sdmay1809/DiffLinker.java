package edu.iastate.sdmay1809;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DiffLinker {
	public static void main(String[] args) throws JSONException, IOException {
		ArrayList<Long> timings = new ArrayList<Long>();
		
		DiffConfig config = DiffConfig.builder(args).build();

		timings.add(System.nanoTime());
		DiffLinker dl = new DiffLinker(config, true, 3);
		int instancesLinked = dl.run("newInstanceMap.json");
		if (instancesLinked < 0) {
			System.err.println("[ERROR] could not link instances!");
		} else {
			System.out.println("Done! Linked " + instancesLinked + " instances");
		}

		timings.add(System.nanoTime());

		// Print out time differences
		for (int i = 1; i < timings.size(); i++) {
			long startTime = timings.get(i - 1);
			long endTime = timings.get(i);
			long duration = (endTime - startTime); // divide by 1000000 to get
													// milliseconds.
			System.out.println("Function #" + i + ": " + duration + "ns = " + duration / 1000000.0 + "ms");
		}
	}

	public static boolean setCurrentDirectory(String directory_name) {
		boolean result = false; // Boolean indicating whether directory was set
		File directory; // Desired current working directory

		directory = new File(directory_name).getAbsoluteFile();
		if (directory.exists() || directory.mkdirs()) {
			result = (System.setProperty("user.dir", directory.getAbsolutePath()) != null);
		}

		return result;
	}

	private DiffConfig config;
	private boolean allowPrintStatements = true;
	private int lineSearchThreshold = 3;

	public DiffLinker(DiffConfig config, boolean allowPrintStatements, int lineSearchThreshold) {
		this.allowPrintStatements = allowPrintStatements;
		this.lineSearchThreshold = lineSearchThreshold;
		this.config = config;
	}

	public int run(String newMapFilename) throws JSONException, IOException {
		if (!setCurrentDirectory(config.KERNEL_DIR)) {
			println("Couldn't get the kernel dir");
			return -1;
		}

		int instancesLinked = 0;
		Charset utf8 = Charset.forName("UTF-8");
		println("Parsing new instance map...");
		Path newInstance = Paths.get(config.DIFF_TEST_DIR, newMapFilename);
		String newInstanceContent = String.join("\n", Files.readAllLines(newInstance, utf8));
		JSONArray newMap = new JSONArray(newInstanceContent);
		
		JSONArray mapping = new JSONArray();

		println("Tracking instances...");
		for (int i = 0; i < newMap.length(); i++) {
			JSONObject instance = newMap.getJSONObject(i);
			
			if(linkInstances(mapping, instance)) {
				instancesLinked++;
			}
		}

		return instancesLinked;
	}
	
	public boolean linkInstances(JSONArray mapping, JSONObject object) throws IOException {
		String filename = object.getString("filename");
		long offset = object.getLong("offset");
		
		String name = object.getString("name");
		String status = object.getString("status");
		String id = object.getString("id");
		long length = object.getLong("length");
		
		String metadata = null;
		// Need to capture comment data within a certain threshold of lines
		
		String[] linesFound = {};
		String buffer = "";
		byte[] byteBuffer = new byte[40]; // byte buffer to capture data 40 bytes at a time
		RandomAccessFile r = new RandomAccessFile(new File(filename), "rw");
		
		// Start at offset
		r.seek(offset);
		
		// While we can still get lines
		while(linesFound.length < this.lineSearchThreshold) {
			// Get the current offset and go back 40 bytes
			long currOffset = r.getFilePointer();
			r.seek(currOffset - 40);
			// Capture the 40 bytes
			r.readFully(byteBuffer, 0, 40);
			buffer = new String(byteBuffer) + buffer;
			// Split the buffer to capture the lines
			linesFound = buffer.split("\n", this.lineSearchThreshold);
		}
		
		// Close the file
		r.close();
		
		// Search for our string
		for(int i = linesFound.length - 1; i >= 0; i--) {
			if(linesFound[i].trim().matches("^/* (?:[[:alnum:]]+@@@){5}[[:alnum:]]+ */$")) {
				metadata = linesFound[i].trim();
			}
		}
		
		// TODO: Add validation checking
	
		JSONObject map = new JSONObject();
		JSONObject newData = new JSONObject()
				.put("id", id)
				.put("name", name)
				.put("status", status)
				.put("filename", filename)
				.put("offset", offset)
				.put("length", length);
		if(metadata != null) {
			println("Found String: " + metadata);
			
			// TODO: Break out comment into actual data!
			JSONObject oldData = new JSONObject()
					.put("metadata", metadata);
			map.put("new", newData).put("old", oldData);
		} else {
			println("No String found");
			
			map.put("new", newData);
		}
		
		mapping.put(map);
		
		return (metadata != null);
	}

	public void println(String msg) {
		if (allowPrintStatements) {
			System.out.println(msg);
		}
	}
}
