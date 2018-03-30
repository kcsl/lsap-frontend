package edu.iastate.sdmay1809;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CLIParser {
	private CommandLineParser parser;
	private Options options;
	private CommandLine line;
	
	public CLIParser(String[] args) throws ParseException
	{
		parser = new DefaultParser();
		options = new Options();
		
		options.addOption("v", "verbose", false, "Print additional information to the console");
		options.addOption(Option.builder("kp")
								.longOpt("kernel-path")
								.optionalArg(true)
								.numberOfArgs(1)
								.argName("path")
								.desc("The path to the root directory of the kernel being patched. By default, it is the current directory (\".\")")
								.build());
		options.addOption(Option.builder("d")
								.longOpt("debug")
								.optionalArg(true)
								.numberOfArgs(1)
								.argName("path")
								.desc("The file in which to store the patched header file output. By default, it is \"./PatcherDebug.txt\"")
								.build());
		options.addOption("h", "help", false, "Prints additional help");
		
		line = parser.parse(options, args);
		
		if (line.hasOption('h'))
		{
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("patcher", "Options available", options, "", true);
		}
	}
	
	public boolean vProvided()
	{
		return line.hasOption('v');
	}
	
	public boolean kpProvided()
	{
		return line.hasOption("kp");
	}
	
	public boolean dProvided()
	{
		return line.hasOption('d');
	}
	
	public String getKPVal()
	{
		return line.getOptionValue("kernel-path", "");
	}
	
	public String getDVal()
	{
		return line.getOptionValue("debug", "resources/PatcherDebug.txt");
	}
}
