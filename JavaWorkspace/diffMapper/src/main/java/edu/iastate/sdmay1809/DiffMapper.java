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
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.iastate.sdmay1809.DiffConfig.DiffConfigBuilder;

public class DiffMapper {
	public static void main(String[] args) throws JSONException, IOException {
		ArrayList<Long> timings = new ArrayList<Long>();

		DiffConfig config = parseConfig(args);

		timings.add(System.nanoTime());
		InstanceTracker it = new InstanceTracker(config.RESULT_DIR);
		it.run(config.DIFF_TEST_DIR, false);

		timings.add(System.nanoTime());
		DiffMapper dm = new DiffMapper(config, true);
		int changesApplied = dm.run("oldInstanceMap.json");
		if (changesApplied < 0) {
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

	public static DiffConfig parseConfig(String[] args) {
		Options options = new Options();
		Option configFileOption = Option.builder("c").longOpt("config-file").hasArg().numberOfArgs(1).argName("file")
				.desc("The JSON configuration file to use").build();
		Option helpMenuOption = Option.builder("h").longOpt("help").hasArg(false).desc("Display this help menu")
				.build();
		Option propertyOption = Option.builder("D").argName("property=value").hasArgs().valueSeparator().numberOfArgs(2)
				.desc("use value for given property").build();
		Option dryRunOption = Option.builder("n").longOpt("dry-run").hasArg(false)
				.desc("See a summary of what the DiffMapper will do. No action will be performed.").build();
		options.addOption(configFileOption);
		options.addOption(propertyOption);
		options.addOption(dryRunOption);
		options.addOption(helpMenuOption);
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println("[ERROR]: " + e.getMessage());
			System.exit(0);
		}

		if (cmd.hasOption("h")) {
			HelpFormatter helpMenu = new HelpFormatter();
			helpMenu.printHelp("java -jar DiffMapper.jar [OPTIONS]", options);
			System.exit(0);
		}

		Properties cliProps = cmd.getOptionProperties("D");
		DiffConfigBuilder configBuilder = DiffConfig.builder();

		if (cmd.hasOption("c")) {
			String configFilename = cmd.getOptionValue("c");
			configBuilder = DiffConfig.builder(configFilename);
		}

		ArrayList<String> cliTypes = new ArrayList<String>();
		for (int i = 0; cliProps.getProperty("types." + i) != null; i++) {
			cliTypes.add(cliProps.getProperty("types." + i));
		}
		String[] cliTypesArray = new String[cliTypes.size()];
		cliTypes.toArray(cliTypesArray);

		DiffConfig config = configBuilder.setOldTag(cliProps.getProperty("old_tag"))
				.setNewTag(cliProps.getProperty("new_tag")).setDiffTestDir(cliProps.getProperty("diff_test_dir"))
				.setKernelDir(cliProps.getProperty("kernel_dir")).setResultDir(cliProps.getProperty("result_dir"))
				.setTypes(cliTypesArray.length > 0 ? cliTypesArray : null).build();

		if (cmd.hasOption("n")) {
			String typesString = "[" + config.TYPES[0];
			for (int i = 1; i < config.TYPES.length; i++) {
				typesString += ", " + config.TYPES[i];
			}
			typesString += "]";

			System.out.println("Config File: " + Utils.coalesce(cmd.getOptionValue("c"), "N/A"));
			System.out.println("DiffMapper Configuration");
			System.out.println("old_tag: " + config.OLD_TAG);
			System.out.println("new_tag: " + config.NEW_TAG);
			System.out.println("diff_test_dir: " + config.DIFF_TEST_DIR);
			System.out.println("kernel_dir: " + config.KERNEL_DIR);
			System.out.println("result_dir: " + config.RESULT_DIR);
			System.out.println("types: " + typesString);
			System.exit(0);
		}

		return config;
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

	public void println(String msg) {
		if (allowPrintStatements) {
			System.out.println(msg);
		}
	}
}
