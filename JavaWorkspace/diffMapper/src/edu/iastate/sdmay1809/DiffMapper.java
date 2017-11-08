package edu.iastate.sdmay1809;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DiffMapper {

	public static String OLD_TAG = "v3.17-rc1";
	public static String NEW_TAG = "v3.18-rc1";
	public static String DIFF_TEST_DIR = "/Volumes/dhanwada_cs/";
	public static String KERNEL_DIR = "/Volumes/dhanwada_cs/kernel/";
	public static String RESULT_DIR = "/Volumes/dhanwada_cs/sdmay18-09/linux-kernel-" + OLD_TAG.substring(1) + "/";
	public static String[] TYPES = { "mutex", "spin" };

	public static void main(String[] args) throws JSONException, IOException {
		ArrayList<Long> timings = new ArrayList<Long>();

		timings.add(System.nanoTime());
		InstanceTracker it = new InstanceTracker(RESULT_DIR);
		it.run(DIFF_TEST_DIR, false);

		timings.add(System.nanoTime());
		DiffMapper dm = new DiffMapper(false);
		int changesApplied = dm.run("oldInstanceMap_full.json");
		if(changesApplied < 0) {
			System.err.println("[ERROR] could not map differences!");
		} else {
			System.out.println("Done! Applied " + changesApplied + " changes");
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
	
	private boolean allowPrintStatements = true;
	
	public DiffMapper(boolean allowPrintStatements) {
		this.allowPrintStatements = allowPrintStatements;
	}

	public int run(String inputMapFilename) throws JSONException, IOException {
		if (!setCurrentDirectory(KERNEL_DIR)) {
			println("Couldn't get the kernel dir");
			return -1;
		}

		println("Parsing instance map...");
		Path oldInstance = Paths.get(DIFF_TEST_DIR, inputMapFilename);
		Charset utf8 = Charset.forName("UTF-8");
		String oldInstanceContent = String.join("\n", Files.readAllLines(oldInstance, utf8));
		JSONArray arr = new JSONArray(oldInstanceContent);
		JSONObject changes = new JSONObject();

		println("Tracking instances...");
		for (int i = 0; i < arr.length(); i++) {
			JSONObject instance = arr.getJSONObject(i);
			trackMetaData(changes, instance);
		}

		println("Applying changes...");
		return applyChanges(changes);
	}

	public void trackMetaData(JSONObject changes, JSONObject object) throws JSONException, IOException {
		String fileName = object.getString("filename");
		String name = object.getString("name");
		String status = object.getString("status");
		String id = object.getString("id");
		long offset = object.getLong("offset");
		long length = object.getLong("length");

		String comment = "/* " + id + "@@@" + status + "@@@" + offset + "@@@" + length + "@@@" + fileName + "@@@" + name
				+ " */\n";
		int commentLength = comment.length();

		JSONObject metaData = new JSONObject().put("metadata", comment).put("offset", offset).put("length",
				commentLength);
		JSONArray fname;
		try {
			fname = new JSONArray();
			changes.putOnce(fileName, fname);
		} catch (JSONException e) {
		}

		changes.getJSONArray(fileName).put(metaData);
	}

	public int applyChanges(JSONObject changes) throws JSONException, IOException {
		int changesApplied = 0;
		@SuppressWarnings("unchecked")
		Iterator<String> iter = changes.keys();
		while (iter.hasNext()) {
			String fname = iter.next();
			JSONArray sorted = sort(changes.getJSONArray(fname));
			int numChanges = sorted.length();

			insert(KERNEL_DIR + fname, sorted);

			println("Applied " + String.format("%3d", numChanges) + " changes to: " + fname);
			changesApplied += numChanges;
		}
		
		return changesApplied;
	}

	public JSONArray sort(JSONArray array) throws JSONException {
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
				}
			}
			sorted.put(array.remove(minIdx));
		}

		return sorted;
	}

	public void insert(String filename, JSONArray instances) throws JSONException, IOException {
		File fbak = new File(filename + "~");
		RandomAccessFile r = new RandomAccessFile(new File(filename), "rw");
		RandomAccessFile rtemp = new RandomAccessFile(fbak, "rw");
		FileChannel sourceChannel = r.getChannel();
		FileChannel targetChannel = rtemp.getChannel();
		long fileSize = r.length();
		long lastOffset = 0;
		for (int i = 0; i < instances.length(); i++) {
			JSONObject instance = instances.getJSONObject(i);
			long offset = instance.getLong("offset");
			String content = instance.getString("metadata");
			String whitespace;
			byte[] whitespaceBuffer = new byte[40];
			if (i == 0) {
				sourceChannel.transferTo(offset, (fileSize - offset), targetChannel);
				sourceChannel.truncate(offset);
				r.seek(offset - 40);
				r.readFully(whitespaceBuffer, 0, 40);
				whitespace = new String(whitespaceBuffer);
//				whitespace = new String(whitespaceBuffer).replaceAll("[^\f\t\r\n]", " ");
				r.writeBytes(content);
				r.writeBytes(whitespace.substring(whitespace.lastIndexOf('\n') + 1));
				targetChannel.position(0L);
			} else {
				long currOffset = r.getFilePointer();
				sourceChannel.transferFrom(targetChannel, currOffset, (offset - lastOffset));
				r.seek(currOffset + (offset - lastOffset) - 40);
				r.readFully(whitespaceBuffer, 0, 40);
				whitespace = new String(whitespaceBuffer);
//				whitespace = new String(whitespaceBuffer).replaceAll("[^\f\t\r\n]", " ");
				r.writeBytes(content);
				r.writeBytes(whitespace.substring(whitespace.lastIndexOf('\n') + 1));
			}
			lastOffset = offset;
		}
		sourceChannel.transferFrom(targetChannel, r.getFilePointer(), targetChannel.size() - targetChannel.position());
		sourceChannel.close();
		targetChannel.close();
		r.close();
		rtemp.close();
		fbak.delete();
	}
	
	public void println(String msg) {
		if(allowPrintStatements) {
			System.out.println(msg);
		}
	}
}
