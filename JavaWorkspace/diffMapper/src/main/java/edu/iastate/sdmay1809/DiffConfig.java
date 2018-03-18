package edu.iastate.sdmay1809;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

public class DiffConfig {

	public String OLD_TAG;
	public String NEW_TAG;
	public String DIFF_TEST_DIR;
	public String KERNEL_DIR;
	public String RESULT_DIR;
	public String[] TYPES;

	public static class DiffConfigBuilder {
		private String OLD_TAG;
		private String NEW_TAG;
		private String DIFF_TEST_DIR;
		private String KERNEL_DIR;
		private String RESULT_DIR;
		private String[] TYPES;

		DiffConfigBuilder() {
			String cwd = System.getProperty("user.dir");
			if (!cwd.endsWith("/")) {
				cwd = cwd + "/";
			}
			this.OLD_TAG = "v3.17-rc1";
			this.NEW_TAG = "v3.18-rc1";
			this.DIFF_TEST_DIR = cwd + "diffmap/";
			this.KERNEL_DIR = cwd + "kernel/";
			this.RESULT_DIR = cwd + "diffmap/prev_result/";
			this.TYPES = new String[] { "mutex", "spin" };
		}

		DiffConfigBuilder(DiffConfigBuilder base) {
			this.OLD_TAG = base.OLD_TAG;
			this.NEW_TAG = base.NEW_TAG;
			this.DIFF_TEST_DIR = base.DIFF_TEST_DIR;
			this.KERNEL_DIR = base.KERNEL_DIR;
			this.RESULT_DIR = base.RESULT_DIR;
			this.TYPES = base.TYPES;
		}

		public DiffConfig build() {
			return new DiffConfig(this);
		}

		public DiffConfigBuilder setOldTag(String old_tag) {
			this.OLD_TAG = Utils.coalesce(old_tag, OLD_TAG);
			return this;
		}

		public DiffConfigBuilder setNewTag(String new_tag) {
			this.NEW_TAG = Utils.coalesce(new_tag, NEW_TAG);
			return this;
		}

		public DiffConfigBuilder setDiffTestDir(String diff_test_dir) {
			this.DIFF_TEST_DIR = Utils.coalesce(diff_test_dir, DIFF_TEST_DIR);
			return this;
		}

		public DiffConfigBuilder setKernelDir(String kernel_dir) {
			this.KERNEL_DIR = Utils.coalesce(kernel_dir, KERNEL_DIR);
			return this;
		}

		public DiffConfigBuilder setResultDir(String result_dir) {
			this.RESULT_DIR = Utils.coalesce(result_dir, RESULT_DIR);
			return this;
		}

		public DiffConfigBuilder setTypes(String[] types) {
			this.TYPES = Utils.coalesce(types, TYPES);
			return this;
		}
	}

	private DiffConfig(DiffConfigBuilder builder) {
		this.OLD_TAG = builder.OLD_TAG;
		this.NEW_TAG = builder.NEW_TAG;
		this.DIFF_TEST_DIR = builder.DIFF_TEST_DIR;
		this.KERNEL_DIR = builder.KERNEL_DIR;
		this.RESULT_DIR = builder.RESULT_DIR;
		this.TYPES = builder.TYPES;
	}

	public static DiffConfigBuilder builder() {
		return new DiffConfigBuilder();
	}

	public static DiffConfigBuilder builder(DiffConfigBuilder base) {
		return new DiffConfigBuilder(base);
	}

	public static DiffConfigBuilder builder(String fileName) {
		Charset utf8;
		Path configFilePath;
		String cwd;
		String configFileContent;
		String[] keys = { "old_tag", "new_tag", "diff_test_dir", "kernel_dir", "result_dir", "types" };
		String[] values = new String[5];
		String[] types_val;
		JSONObject configObject;

		fileName = Utils.coalesce(fileName, "config.json");
		cwd = System.getProperty("user.dir");
		configFilePath = Paths.get(cwd, fileName);
		utf8 = Charset.forName("UTF-8");
		configFileContent = "{}";
		try {
			configFileContent = String.join("\n", Files.readAllLines(configFilePath, utf8));
			configObject = new JSONObject(configFileContent);
		} catch (IOException e) {
			System.err.println("WARN: DiffConfig file could not be read, using default values");
			return new DiffConfigBuilder();
		} catch (JSONException e) {
			System.err.println("WARN: DiffConfig file was not a valid JSON object, using default values");
			return new DiffConfigBuilder();
		}

		// Loop through all the String values and get them from their keys
		// If an exception is thrown reading the string, set it to null so the
		// config builder will use the default value.
		for (int i = 0; i < values.length; i++) {
			try {
				values[i] = configObject.getString(keys[i]);
			} catch (Exception e) {
				values[i] = null;
			}
		}

		try {
			// Get a json array for the "types" key and convert it to a String array
			Object[] types_objs = configObject.getJSONArray(keys[5]).toList().toArray();
			types_val = new String[types_objs.length];
			
			for(int i = 0; i < types_objs.length; i++) {
				types_val[i] = Objects.toString(types_objs[i], null);
			}
		} catch (Exception e) {
			types_val = null;
		}

		// Return a DiffConfigBuilder using the values from the config file.
		return new DiffConfigBuilder().setOldTag(values[0]).setNewTag(values[1]).setDiffTestDir(values[2])
				.setKernelDir(values[3]).setResultDir(values[4]).setTypes(types_val);
	}
	
	public static DiffConfigBuilder builder(String[] args) {
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
			helpMenu.printHelp("java -jar <jarfile> [OPTIONS]", options);
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

		return configBuilder;
	}
}
