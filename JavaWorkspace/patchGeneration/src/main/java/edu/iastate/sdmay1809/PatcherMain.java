package edu.iastate.sdmay1809;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PatcherMain 
{
	public static void main(String[] args) throws Exception
	{
		/**
		 * TODO:
		 * 		Implement patched header file "#include" statements
		 */
		
		CommandLineParser parser = new DefaultParser();
		Options options = new Options();
		
		options.addOption("v", "verbose", false, "Print additional information to the console");
		options.addOption(Option.builder("kp")
								.longOpt("kernel-path")
								.hasArg()
								.numberOfArgs(1)
								.argName("path")
								.desc("The path to the root directory of the kernel being patched. By default, it is the directory \"resources/kernel/\"")
								.build());
		options.addOption(Option.builder("d")
								.longOpt("debug")
								.optionalArg(true)
								.numberOfArgs(1)
								.argName("path")
								.desc("The directory in which to store the Patcher output in .txt form. By default, it is \"resources/patch/debug/\"")
								.build());
		options.addOption(Option.builder("o")
								.longOpt("output")
								.optionalArg(true)
								.numberOfArgs(1)
								.argName("path")
								.desc("The directory in which to store the Patcher output in .h form. By default, it is \"resources/patch/real/\"\n"
										+ "If the -o and -d tags are both present, -d will override -o")
								.build());
		options.addOption(Option.builder("c")
							    .longOpt("config-file")
							    .hasArg()
							    .numberOfArgs(1)
							    .argName("path")
							    .desc("The config file containing configuration options for the Patcher. By default, it is \"resources/patcherConfig.json\"")
							    .build());
		options.addOption("h", "help", false, "Prints additional help");
		
		args = "-kp resources/414Kernel/ -v -o resources/patch/414Patch/".split("\\s");
		
		CommandLine cmd = parser.parse(options, args);
		
		if (cmd.hasOption('h'))
		{
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("patcher", "Options available", options, "", true);
			return;
		}
		
		Patcher patcher = new Patcher(
										cmd.getOptionValue("config", "resources/patcherConfig.json"), 														// Config location
										cmd.getOptionValue("kernel-path", "resources/kernel/"), 															// Kernel Path
										cmd.hasOption("debug") ? cmd.getOptionValue("debug", cmd.getOptionValue("output", "resources/patch/real/")) 
															   : cmd.getOptionValue("output", "resources/patch/real"), 										// Output Path
										cmd.hasOption("debug"), 																							// Enable debugging?
										cmd.hasOption("verbose")																							// Enable verbose output?
									 );
		patcher.patch();				
	}
}
