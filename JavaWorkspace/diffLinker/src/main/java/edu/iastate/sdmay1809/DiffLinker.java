package edu.iastate.sdmay1809;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.iastate.sdmay1809.shared.DiffConfig;
import edu.iastate.sdmay1809.shared.InstanceTracker;

public class DiffLinker {
	public static void main(String[] args) throws JSONException, IOException {
		ArrayList<Long> timings = new ArrayList<Long>();

		DiffConfig config = DiffConfig.builder(args).build();

		timings.add(System.nanoTime());
		InstanceTracker it = new InstanceTracker(config.RESULT_DIR);
		it.run(config.DIFF_TEST_DIR, false);

		timings.add(System.nanoTime());
		DiffLinker dl = new DiffLinker(config, true, 10);
		int instancesLinked = dl.run("oldInstanceMap.json");
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

	private DiffConfig config;
	private boolean allowPrintStatements = true;
	private int lineSearchThreshold = 10;

	public DiffLinker(DiffConfig config, boolean allowPrintStatements, int lineSearchThreshold) {
		this.allowPrintStatements = allowPrintStatements;
		this.lineSearchThreshold = lineSearchThreshold;
		this.config = config;
	}

	public int run(String newMapFilename) throws JSONException, IOException {
		int instancesLinked = 0;
		Charset utf8 = Charset.forName("UTF-8");
		println("Parsing new instance map...");
		Path newInstance = Paths.get(config.DIFF_TEST_DIR, newMapFilename);
		String newInstanceContent = String.join("\n", Files.readAllLines(newInstance, utf8));
		JSONArray newMap = new JSONArray(newInstanceContent);

		JSONArray mapping = new JSONArray();

		println("Linking instances...");
		for (int i = 0; i < newMap.length(); i++) {
			JSONObject instance = newMap.getJSONObject(i);

			if (linkInstances(mapping, instance)) {
				instancesLinked++;
			}
		}

		mapping.write(new PrintWriter(Paths.get(config.DIFF_TEST_DIR, "diffInstanceMap.json").toFile()), 1, 2);

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
		Path fileToSearch = Paths.get(config.KERNEL_DIR, filename);

		println("Searching through: " + fileToSearch.toString());
		// Need to capture comment data within a certain threshold of lines

		String[] linesFound = {};
		String buffer = "";
		byte[] byteBuffer = new byte[40]; // byte buffer to capture data 40
											// bytes at a time
		RandomAccessFile r = new RandomAccessFile(fileToSearch.toFile(), "rw");

		// Start at offset
		r.seek(offset);

		// capture some starting data
		int j = 0;
		while (j++ < 3) {
			long currOffset = r.getFilePointer();
			int newLength = (int) Math.min(40, r.length() - 1 - currOffset);
			// Capture the 40 bytes
			r.readFully(byteBuffer, 0, newLength);
			buffer = buffer + new String(byteBuffer);
			linesFound = buffer.split("\n");
			if (newLength < 40) {
				break;
			}
		}

		int linesFoundAfter = linesFound.length;

		// Start at offset again
		r.seek(offset);

		// While we can still get lines
		while (linesFound.length < this.lineSearchThreshold + linesFoundAfter) {
			// Get the current offset and go back 40 bytes
			long currOffset = r.getFilePointer();
			long newOffset = Math.max(currOffset - 40, 0);
			r.seek(newOffset);
			// Capture the 40 bytes
			r.readFully(byteBuffer, 0, 40);
			r.seek(newOffset);
			buffer = new String(byteBuffer) + buffer;
			// Split the buffer to capture the lines
			linesFound = buffer.split("\n", this.lineSearchThreshold + linesFoundAfter);
			// println("Parsing from " + newOffset + " to " + currOffset + "
			// Current Buffer: \n" + buffer);
			if (newOffset == 0) {
				break;
			}
		}

		// Close the file
		r.close();

		println("Searching through " + linesFound.length + " lines");

		// Search for our string
		for (int i = linesFound.length - 1; i >= 0; i--) {
			if (linesFound[i].trim().matches("\\/\\* (?:[a-zA-Z0-9\\.\\/\\_]+@@@){5}[a-zA-Z0-9\\.\\/\\_]+ \\*\\/")) {
				metadata = linesFound[i].trim();
				break;
			}
		}

		// TODO: Add validation checking

		JSONObject map = new JSONObject();
		JSONObject newData = new JSONObject().put("id", id).put("name", name).put("status", status)
				.put("filename", filename).put("offset", offset).put("length", length);
		if (metadata != null) {
			println("Found String: " + metadata);

			String[] commentSplit = metadata.split(" ");
			JSONObject oldData = new JSONObject();
			String[] dataSplit = commentSplit[1].split("@@@");

			oldData.put("id", dataSplit[0]).put("name", dataSplit[5]).put("status", dataSplit[1])
					.put("filename", dataSplit[4]).put("offset", dataSplit[2]).put("length", dataSplit[3]);

			oldData.put("metadata", metadata);
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
