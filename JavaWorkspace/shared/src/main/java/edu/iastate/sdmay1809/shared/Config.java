package edu.iastate.sdmay1809.shared;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
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
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

	Map<String, Object> configOptions;

	public Config(Builder<? extends Config> builder) {
		configOptions = new HashMap<String, Object>();
		configOptions.putAll(builder.configOptions);
	}

	public static <C extends Config, T extends Builder<C>> T builder(Class<T> builder) {
		try {
			return builder.newInstance();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	public static <C extends Config, T extends Builder<C>> T builder(Class<T> builder, File file) {
		if (file == null) {
			return Config.builder(builder);
		}
		try {
			Constructor<T> ctor = builder.getDeclaredConstructor(File.class);
			return ctor.newInstance(file);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	public static <C extends Config, T extends Builder<C>> T builder(Class<T> builder, T base) {
		try {
			return builder.getConstructor(base.getClass()).newInstance(base);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	public static <C extends Config, T extends Builder<C>> T builder(Class<T> builder, String[] args) {
		if (args == null) {
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
		File f = null;
		if (cmd.hasOption("c")) {
			f = new File(cmd.getOptionValue("c"));
		}

		T b = Config.builder(builder, f);

		Map<String, Object> propsMap = new HashMap<String, Object>();

		for (String key : cliProps.stringPropertyNames()) {
			propsMap = Utils.<Map<String, Object>>insert(key, cliProps.getProperty(key), propsMap);
		}

		propsMap.forEach((key, val) -> b.setConfig(key, val));

		if (cmd.hasOption("n")) {
			System.out.println("Config File: " + Utils.coalesce(cmd.getOptionValue("c"), "N/A"));
			System.out.println("Configuration");
			System.out.println(b.configOptions.toString());
			System.exit(0);
		}

		return b;
	}

	public static class Builder<T extends Config> {

		Map<String, Object> configOptions;

		public Builder() {
			this.configOptions = new HashMap<String, Object>();
		}

		public Builder(Builder<T> base) {
			this();
			if (base != null) {
				configOptions.putAll(base.configOptions);
			}
		}

		public Builder(File file) {
			this();
			if (file == null) {
				return;
			}
			if (!file.exists() || file.isDirectory()) {
				// TODO: WARN user about illegal argument
				// throw new IllegalArgumentException("file must be an existing
				// file and not a directory");
				return;
			}

			String contents;
			JSONObject contentObject;
			try {
				contents = String.join("\n",
						Files.readAllLines(Paths.get(file.getAbsolutePath()), Charset.forName("UTF-8")));
				contentObject = new JSONObject(contents);
			} catch (IOException e) {
				System.err.println("WARN: DiffConfig file could not be read, using default values");
				return;
			} catch (JSONException e) {
				System.err.println("WARN: DiffConfig file was not a valid JSON object, using default values");
				return;
			}

			Map<String, Object> map = Utils.convertToMap(contentObject);

			map.forEach((key, val) -> this.setConfig(key, val));
		}

		public Builder<T> setConfig(String config, Object value) {
			if (value != null) {
				configOptions.put(config, value);
			}
			return this;
		}

		@SuppressWarnings("unchecked")
		public T build() {
			Class<?> subclass = getClass();
			while(subclass != getClass()) {
				subclass = subclass.getSuperclass();
			}
			ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
			Class<?> configType = (Class<?>) type.getActualTypeArguments()[0];
			
			try {
				return (T) configType.getDeclaredConstructor(getClass()).newInstance(this);
			} catch (Exception e) {
				return (T) Config.builder(Config.Builder.class).build();
			}
		}
	}

}
