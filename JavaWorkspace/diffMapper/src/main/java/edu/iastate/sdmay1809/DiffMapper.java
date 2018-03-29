package edu.iastate.sdmay1809;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DiffMapper {
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
	private DiffConfig config;

	public DiffMapper(DiffConfig config, boolean allowPrintStatements) {
		this.allowPrintStatements = allowPrintStatements;
		this.config = config;
	}

	public int run(String inputMapFilename) throws JSONException, IOException {
		if (!setCurrentDirectory(config.KERNEL_DIR)) {
			println("Couldn't get the kernel dir");
			return -1;
		}
		
		performGitSetup();

		println("Parsing instance map...");
		Path oldInstance = Paths.get(config.DIFF_TEST_DIR, inputMapFilename);
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
		int retVal = applyChanges(changes);
		
		performGitCleanup();
		
		return retVal;
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

		JSONObject metaData = new JSONObject().put("metadata", comment).put("offset", offset)
				.put("length", commentLength).put("metaLength", length).put("metaStatus", status);
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
		Iterator<String> iter = changes.keys();
		while (iter.hasNext()) {
			String fname = iter.next();
			JSONArray sorted = sort(changes.getJSONArray(fname));
			int numChanges = sorted.length();

			insert(config.KERNEL_DIR + fname, sorted);

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
				} else if (tmp.getInt("offset") == min.getInt("offset")
						&& tmp.getInt("metaLength") == min.getInt("metaLength")
						&& tmp.getString("metaStatus").equals(min.getString("metaStatus"))) {
					// Duplicate detected -- remove it
					array.remove(i);
					i--;
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
			String buffer;
			byte[] whitespaceBuffer = new byte[40];

			if (i == 0) {
				sourceChannel.transferTo(offset, (fileSize - offset), targetChannel);
				sourceChannel.truncate(offset);
				targetChannel.position(0L);
				r.seek(offset - 40);
			} else {
				long currOffset = r.getFilePointer();
				sourceChannel.transferFrom(targetChannel, currOffset, (offset - lastOffset));
				r.seek(currOffset + (offset - lastOffset) - 40);
			}

			r.readFully(whitespaceBuffer, 0, 40);
			buffer = new String(whitespaceBuffer);

			r.seek(r.getFilePointer() - 40);
			r.writeBytes(buffer.substring(0, buffer.lastIndexOf('\n') + 1));
			r.writeBytes(buffer.substring(buffer.lastIndexOf('\n') + 1).replaceAll("[^\f\t\r\n]", " "));
			r.writeBytes(content);
			r.writeBytes(buffer.substring(buffer.lastIndexOf('\n') + 1));

			lastOffset = offset;
		}
		sourceChannel.transferFrom(targetChannel, r.getFilePointer(), targetChannel.size() - targetChannel.position());
		sourceChannel.close();
		targetChannel.close();
		r.close();
		rtemp.close();
		fbak.delete();
	}
	
	public void performGitSetup() {
		File kernel_dir = new File(config.KERNEL_DIR);
		
		// git checkout <old_tag>
		execGitCheckoutTag(config.OLD_TAG, kernel_dir);
		
		// git clean -xdfq
		execGitClean(kernel_dir);
		
		// git checkout -b diff_map
		execGitCreateBranch("diff_map", kernel_dir);
	}
	
	public void performGitCleanup() {
		File kernel_dir = new File(config.KERNEL_DIR);
		
		// git commit -am "add metadata"
		execGitCommitAll("add metadata", kernel_dir);
		
		// git checkout <new_tag>
		execGitCheckoutTag(config.NEW_TAG, kernel_dir);
		
		// git clean -xdfq
		execGitClean(kernel_dir);
		
		// git reset <old_version>
		execGitReset(config.OLD_TAG, kernel_dir);
		
		// git commit -am "upgrade to <new version>"
		execGitCommitAll("upgrade to " + config.NEW_TAG, kernel_dir);
		
		// git checkout -b diff_map_upgraded
		execGitCreateBranch("diff_map_upgraded", kernel_dir);
		
		// git rebase -s recursive -X theirs diff_map 
		execGitRebase("diff_map", kernel_dir);
	}
	
	public void execGitCheckoutTag(String tag, File dir) {
		try {
			println(Utils.execute(new String[] {"git", "checkout", tag}, dir));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void execGitClean(File dir) {
		try {
			println(Utils.execute(new String[] {"git", "clean", "-xdfq"}, dir));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void execGitCreateBranch(String branchName, File dir) {
		try {
			println(Utils.execute(new String[] {"git", "checkout", "-b", branchName}, dir));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void execGitReset(String commit, File dir) {
		try {
			println(Utils.execute(new String[] {"git", "reset", commit}, dir));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void execGitCommitAll(String message, File dir) {
		try {
			println(Utils.execute(new String[] {"git", "commit", "-am", message}, dir));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void execGitRebase(String base, File dir) {
		try {
			println(Utils.execute(new String[] {"git", "rebase", "-s", "recursive", "-X", "theirs", base}, dir));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void println(String msg) {
		if (allowPrintStatements) {
			System.out.println(msg);
		}
	}
}
