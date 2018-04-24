package edu.iastate.sdmay1809;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.iastate.sdmay1809.shared.DiffConfig;
import edu.iastate.sdmay1809.shared.InstanceTracker.InstanceParser;
import edu.iastate.sdmay1809.shared.InstanceTracker.InvalidInstanceFormatException;

public class DiffLinker {

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
		JSONObject newMapOptimized = new JSONObject();
		JSONArray mapping = new JSONArray();

		optimizeMapStructure(newMap, newMapOptimized);

		println("Linking instances...");

		Iterator<String> iter = newMapOptimized.keys();

		while (iter.hasNext()) {
			String key = iter.next();
			JSONArray instances = newMapOptimized.getJSONArray(key);

			instancesLinked += linkInstances(key, instances, mapping);
		}

		Writer writer = mapping.write(new PrintWriter(Paths.get(config.DIFF_TEST_DIR, "diffInstanceMap.json").toFile()), 1, 2);
		writer.close();

		int mappingLength = mapping.length();
		JSONArray interestingCases = new JSONArray();
		for (int i = 0; i < mappingLength; i++) {
			JSONObject link = mapping.getJSONObject(i);
			if (link.has("old")) {
				if (!link.getJSONObject("old").getString("status")
						.equals(link.getJSONObject("new").getString("status"))) {
					println(link.toString());
					interestingCases.put(link);
				}
			}

		}
		writer = interestingCases.write(new PrintWriter(Paths.get(config.DIFF_TEST_DIR, "diffInteresting.json").toFile()), 1, 2);
		writer.close();

		PrintWriter pw = new PrintWriter(Paths.get(config.DIFF_TEST_DIR, "diffInteresting.csv").toFile());

		pw.write(
				"New Version, status, id, name, filename, , Old Version, status, id, name, filename,\n");

		int interestingCasesLength = interestingCases.length();
		for (int i = 0; i < interestingCasesLength; i++) {
			JSONObject link = interestingCases.getJSONObject(i);
			JSONObject newData = link.getJSONObject("new");
			JSONObject oldData = link.getJSONObject("old");

			pw.write("" + config.NEW_TAG + ", " + newData.getString("status") + ", " + newData.getString("id") + ", "
					+ newData.getString("name") + ", " + newData.getString("filename") + ", , ");
			pw.write("" + config.OLD_TAG + ", " + oldData.getString("status") + ", " + oldData.getString("id") + ", "
					+ oldData.getString("name") + ", " + oldData.getString("filename"));
			pw.write("\n");

		}
		
		pw.close();

		return instancesLinked;
	}

	private void optimizeMapStructure(JSONArray map, JSONObject optimized) {
		for (int i = 0; i < map.length(); i++) {
			JSONObject instance = map.getJSONObject(i);

			String filename = instance.getString("filename");

			JSONArray fileInstances = null;
			if (optimized.has(filename)) {
				fileInstances = optimized.getJSONArray(filename);
			} else {
				fileInstances = new JSONArray();
				optimized.put(filename, fileInstances);
			}

			fileInstances.put(instance);
		}
	}

	private int linkInstances(String filename, JSONArray instances, JSONArray diffMap) throws IOException {
		JSONArray sorted = sort(instances);
		int length = sorted.length();
		int instancesLinked = 0;

		Path fileToSearch = Paths.get(config.KERNEL_DIR, filename);
		// println("Searching through: " + fileToSearch.toString());
		// Need to capture comment data within a certain threshold of lines

		RandomAccessFile r = new RandomAccessFile(fileToSearch.toFile(), "r");

		for (int i = 0; i < length; i++) {
			JSONObject instance = sorted.getJSONObject(i);

			if (linkInstance(instance, diffMap, r)) {
				instancesLinked++;
			}

		}

		r.close();

		println((instancesLinked < length ? "MISSED " : "SUCCESS") + ", " + fileToSearch.toString() + ", "
				+ String.format("%2d", length) + ", " + String.format("%2d", instancesLinked));

		return instancesLinked;
	}

	private boolean linkInstance(JSONObject instance, JSONArray diffMap, RandomAccessFile r) throws IOException {
		long offset = instance.getLong("offset");
		String filename = instance.getString("filename");
		String name = instance.getString("name");
		String status = instance.getString("status");
		String id = instance.getString("id");
		long length = instance.getLong("length");

		JSONObject oldData = null;
		String metadata = null;
		String after;
		String before;
		try {
			after = captureLines(r, offset, 3, false);
			before = captureLines(r, offset, lineSearchThreshold, true);
		} catch (CaptureLinesFailedException e) {
			System.err.println("[ERROR]: Couldn't Capture Lines for id: " + id);
			return false;
		}
		String buffer = before + after;
		String[] linesFound = buffer.split("\n");

		// println("Found " + linesFound.length + " lines");
		// println("Buffer: \n" + buffer);
		
		InstanceParser ip = new MetadataCommentInstanceParser();

		// Search for our string
		for (int i = linesFound.length - 1; i >= 0; i--) {
			try {
				oldData = ip.parseEntry(linesFound[i]);
				metadata = linesFound[i].trim();
				break;
			} catch (InvalidInstanceFormatException e) {}
		}

		// TODO: Add validation checking

		JSONObject map = new JSONObject();
		JSONObject newData = new JSONObject().put("id", id).put("name", name).put("status", status)
				.put("filename", filename).put("offset", offset).put("length", length);
		if (oldData != null) {
			oldData.put("metadata", metadata);
			map.put("new", newData).put("old", oldData);
		} else {
			// println("No String found");
			map.put("new", newData);
		}

		diffMap.put(map);

		return (oldData != null);
	}

	private String captureLines(RandomAccessFile r, long offset, int maxLines, boolean before) throws CaptureLinesFailedException {
		String buffer = "";
		byte[] byteBuffer;
		String[] linesFound = {};
		long fileLength;
		long currOffset = 0;

		try {
			r.seek(offset);
			fileLength = r.length();
		} catch (Exception e) {
			System.err.println("Couldn't Seek to this offset!");
			e.printStackTrace(System.err);
			throw new CaptureLinesFailedException(e.getMessage());
		}

		while (linesFound.length < maxLines) {
			try {
				currOffset = r.getFilePointer();
				int newLength = before ? (int) Math.min(40, currOffset) : (int) Math.min(40, fileLength - 1 - offset);
				byteBuffer = new byte[newLength];
				if (before) {
					r.seek(currOffset - newLength);
					r.readFully(byteBuffer, 0, newLength);
					r.seek(currOffset - newLength);

					buffer = new String(byteBuffer) + buffer;
				} else {
					r.readFully(byteBuffer, 0, newLength);
					buffer = buffer + new String(byteBuffer);
				}

				linesFound = buffer.split("\n", maxLines);
				if (newLength < 40) {
					break;
				}
			} catch (Exception e) {
				System.err.println("Couldn't Capture Bytes at offset: " + currOffset);
				e.printStackTrace(System.err);
				throw new CaptureLinesFailedException(e.getMessage());
			}
		}

		return buffer;
	}

	private void println(String msg) {
		if (allowPrintStatements) {
			System.out.println(msg);
		}
	}

	private JSONArray sort(JSONArray array) throws JSONException {
		JSONArray sorted = new JSONArray();

		while (array.length() > 0) {
			int minIdx = 0;
			JSONObject min = array.getJSONObject(0);
			JSONObject tmp;
			for (int i = 1; i < array.length(); i++) {
				tmp = array.getJSONObject(i);
				if (tmp.getInt("offset") < min.getInt("offset")) {
					min = tmp;
					minIdx = i;
				} else if (tmp.getInt("offset") == min.getInt("offset")
						&& tmp.getString("id").equals(min.getString("id"))) {
					// Duplicate detected -- remove it
					array.remove(i);
					i--;
				}
			}
			sorted.put(array.remove(minIdx));
		}

		return sorted;
	}
}
