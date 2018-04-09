package edu.iastate.sdmay1809;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class PatcherMain 
{
	public static void main(String[] args) throws IOException, ParseException
	{
		CommandLineParser parser = new DefaultParser();
		Options options = new Options();
		String iniFile = "resources/patcher.ini";
		
		options.addOption("v", "verbose", false, "Print additional information to the console");
		options.addOption(Option.builder("kp")
								.longOpt("kernel-path")
								.hasArg()
								.numberOfArgs(1)
								.argName("path")
								.desc("The path to the root directory of the kernel being patched. By default, it is the current directory (\".\")")
								.build());
		options.addOption(Option.builder("d")
								.longOpt("debug")
								.optionalArg(true)
								.numberOfArgs(1)
								.argName("path")
								.desc("The directory in which to store the patched header files' output. By default, it is \"./PatcherDebug/\"")
								.build());
		options.addOption(Option.builder("i")
							    .longOpt("ini-file")
							    .hasArg()
							    .numberOfArgs(1)
							    .argName("path")
							    .desc("The .ini file containing configuration options for the Patcher. By default, it is \"./resources/patcher.ini\"")
							    .build());
		options.addOption("h", "help", false, "Prints additional help");
		
		CommandLine cmd = parser.parse(options, args);
		
		if (cmd.hasOption('h'))
		{
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("patcher", "Options available", options, "", true);
			return;
		}

		iniFile = Paths.get(cmd.getOptionValue('i', iniFile)).toString();
		
		Patcher patcher = new Patcher(iniFile);
		
		patcher.setVerbose(cmd.hasOption('v'));
		patcher.setDebugPath(cmd.hasOption('d') ? cmd.getOptionValue('d', "PatcherDebug/") : null);
		patcher.setKernelDirectory(cmd.getOptionValue("kp", null));
		
		patcher.patch();
	}
}
