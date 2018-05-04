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

import edu.iastate.sdmay1809.shared.DiffConfig;
import edu.iastate.sdmay1809.shared.Utils;

public class DiffMapper {

	private boolean allowPrintStatements = true;
	private DiffConfig config;

	public DiffMapper(DiffConfig config, boolean allowPrintStatements) {
		this.allowPrintStatements = allowPrintStatements;
		this.config = config;
	}

	public int run(String inputMapFilename) throws JSONException, IOException, Exception {
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

	private void trackMetaData(JSONObject changes, JSONObject object) throws JSONException, IOException {
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

	private int applyChanges(JSONObject changes) throws JSONException, IOException {
		int changesApplied = 0;
		Iterator<String> iter = changes.keys();
		while (iter.hasNext()) {
			String fname = iter.next();
			JSONArray sorted = sort(changes.getJSONArray(fname));
			int numChanges = sorted.length();

			insert(Paths.get(config.KERNEL_DIR, fname).toString(), sorted);

			println("Applied " + String.format("%3d", numChanges) + " changes to: " + fname);
			changesApplied += numChanges;
		}

		return changesApplied;
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

	private void insert(String filename, JSONArray instances) throws JSONException, IOException {
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

			int len = r.read(whitespaceBuffer, 0, 40);
			buffer = new String(whitespaceBuffer);

			r.seek(r.getFilePointer() - len);
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

	private void performGitSetup() throws Exception {
		File kernel_dir = new File(config.KERNEL_DIR);

		try {
			// git checkout <old_tag>
			execGitCheckout(config.OLD_TAG, kernel_dir, false);

			// git clean -xdfq
			execGitClean(kernel_dir);

			// git checkout -b diff_map
			execGitCheckout("diff_map", kernel_dir, true);
		} catch (Exception e) {
			println("[ERROR] Couldn't complete git setup!");
			println(e.getMessage());
			throw new Exception("Couldn't complete git setup!");
		}
	}

	private void performGitCleanup() throws Exception {
		File kernel_dir = new File(config.KERNEL_DIR);

		try {
			// git commit -am "add metadata"
			execGitCommitAll("add metadata", kernel_dir);

			// git checkout <new_tag>
			execGitCheckout(config.NEW_TAG, kernel_dir, false);

			// git clean -xdfq
			execGitClean(kernel_dir);

			// git reset <old_version>
			execGitReset(config.OLD_TAG, kernel_dir);

			// git commit -am "upgrade to <new version>"
			execGitCommitAll("upgrade to " + config.NEW_TAG, kernel_dir);

			// git checkout -b diff_map_upgraded
			execGitCheckout("diff_map_upgraded", kernel_dir, true);

			// git rebase -s recursive -X theirs diff_map
			execGitRebase("diff_map", kernel_dir);
			
			// git tag <new_tag>-mapped
			execGitTag(config.NEW_TAG + "-mapped", kernel_dir);
			
			// git checkout <new_tag>-mapped
			execGitCheckout(config.NEW_TAG + "-mapped", kernel_dir, false);
			
			// git branch -D diff_map
			execGitDeleteBranch("diff_map", kernel_dir);
			
			// git branch -D diff_map_upgraded
			execGitDeleteBranch("diff_map_upgraded", kernel_dir);
		} catch (Exception e) {
			println("[ERROR] Couldn't complete git cleanup!");
			println(e.getMessage());
			throw new Exception("Couldn't complete git cleanup!");
		}
	}

	private void execGitClean(File dir) throws IOException, InterruptedException {
		Utils.execute(new String[] { "git", "clean", "-xdfq" }, dir, allowPrintStatements);
	}
	
	private void execGitCheckout(String branchName, File dir, boolean create) throws IOException, InterruptedException {
		if(create) {
			Utils.execute(new String[] { "git", "checkout", "-b", branchName }, dir, allowPrintStatements);
		} else {
			Utils.execute(new String[] { "git", "checkout", branchName }, dir, allowPrintStatements);
		}
		
	}
	
	private void execGitDeleteBranch(String branchName, File dir) throws IOException, InterruptedException {
		Utils.execute(new String[] {"git", "branch", "-D", branchName}, dir, allowPrintStatements);
	}
	
	private void execGitTag(String tagName, File dir) throws IOException, InterruptedException {
		Utils.execute(new String[] {"git", "tag", tagName}, dir, allowPrintStatements);
	}

	private void execGitReset(String commit, File dir) throws IOException, InterruptedException {
		Utils.execute(new String[] { "git", "reset", commit }, dir, allowPrintStatements);
	}

	private void execGitCommitAll(String message, File dir) throws IOException, InterruptedException {
		Utils.execute(new String[] { "git", "commit", "-am", message }, dir, allowPrintStatements);
	}

	private void execGitRebase(String base, File dir) throws IOException, InterruptedException {
		int ret = Utils.execute(new String[] { "git", "rebase", "-s", "recursive", "-X", "theirs", base }, dir, allowPrintStatements);
		int i = 0;
		while(ret != 0 && ++i < 10) {
			Utils.execute(new String[] { "git", "add", "." }, dir, allowPrintStatements);
			ret = Utils.execute(new String[] { "git", "rebase", "--continue" }, dir, allowPrintStatements);
		}
		if(i >= 10) {
			println("[ERROR]: Max Rebase Recovery Attempts Reached, exiting...");
			return;
		}
	}

	private void println(String msg) {
		if (allowPrintStatements) {
			System.out.println(msg);
		}
	}
}
