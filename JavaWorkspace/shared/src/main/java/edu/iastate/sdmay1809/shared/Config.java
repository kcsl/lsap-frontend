package edu.iastate.sdmay1809.shared;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

public class Config {

	Map<String, Object> configOptions;
	
	public Config(Builder<?> builder) {
		configOptions = new HashMap<String, Object>();
		configOptions.putAll(builder.configOptions);
	}
	
	public static <T extends Config> Builder<T> builder() {
		return new Builder<T>();
	}
	
	public static <T extends Config> Builder<T> builder(File file) throws IllegalArgumentException, IOException {
		if(!file.exists() || file.isDirectory()) {
			throw new IllegalArgumentException("file must be an existing file and not a directory");
		}
		
		String contents = String.join("\n", Files.readAllLines(Paths.get(file.getAbsolutePath()), Charset.forName("UTF-8")));
		JSONObject contentObject = new JSONObject(contents);
		Builder<T> b = new Builder<T>();
		
		Map<String, Object> map = Utils.convertToMap(contentObject);
		
		map.forEach((key, val) -> b.setConfig(key, val));
		
		return b;
	}
	
	public static <T extends Config> Builder<T> builder(String[] args) throws IllegalArgumentException {
		if(args == null) {
			throw new IllegalArgumentException("args must not be null");
		}
		
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
		
		Builder<T> b;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println("[FATAL]: " + e.getMessage());
			System.exit(0);
		}

		if (cmd.hasOption("h")) {
			HelpFormatter helpMenu = new HelpFormatter();
			helpMenu.printHelp("java -jar <jarfile> [OPTIONS]", options);
			System.exit(0);
		}

		Properties cliProps = cmd.getOptionProperties("D");

		// Start with configs from a file 
		if (cmd.hasOption("c")) {
			b = new Builder<T>(new File(cmd.getOptionValue("c")));
		} else {
			b = new Builder<T>();
		}
		
		for(String key : cliProps.stringPropertyNames()) {
			String[] parts = key.split("\\.", 2);
			if(parts.length > 1) {
				// TODO: recursively insert config option into b
				if(b.configOptions.containsKey(parts[0])) {
					Object value = b.configOptions.get(parts[0]);
					if(value instanceof Map) {
						// TODO: parse and insert String recursively
					} else {
						// TODO: Warn user of trying to replace existing value with nested object!
					}
				} else {
					
				}
			} else {
				if(b.configOptions.containsKey(parts[0])) {
					// TODO: Warn user of trying to replace existing top level object!
				} else {
					b.setConfig(key, cliProps.getProperty(parts[0]));
				}
			}
		}
		
		return b;
	}
	
	public static class Builder<T extends Config> {
		
		Map<String, Object> configOptions;
		
		public Builder() {
			configOptions = new HashMap<String, Object>();
		}
		
		public Builder(Builder<?> base) {
			configOptions = new HashMap<String, Object>();
			configOptions.putAll(base.configOptions);
		}
		
		public Builder(File file) {
			
		}
		
		public Builder<?> setConfig(String config, Object value) {
			configOptions.put(config, value);
			return this;
		}
		
		public Config build() {
			return new Config(this);
		}
	}
	
}
