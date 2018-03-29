package edu.iastate.sdmay1809;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
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
								.hasArg()
								.valueSeparator(' ')
								.desc("The path to the root directory of the kernel being patched. If not present, the path will defualt to the current directory.")
								.build());
		options.addOption(Option.builder("d")
								.longOpt("debug")
								.hasArg()
								.valueSeparator(' ')
								.desc("If present, the Patcher will write the content of the patched header files to text files. These text files will be found in the location provided, or in the current directory if no directory is provided.")
								.build());
		
		line = parser.parse(options, args);
	}
	
	public boolean vProvided()
	{
		return line.hasOption('v') || line.hasOption("verbose");
	}
	
	public boolean kpProvided()
	{
		return line.hasOption("kp") || line.hasOption("kernel-path");
	}
	
	public boolean dProvided()
	{
		return line.hasOption('d') || line.hasOption("debug");
	}
	
	public String getKpVal()
	{
		return (kpProvided()) ? line.getOptionValue("kernel-path") : "";
	}
	
	public String getDVal()
	{
		return (dProvided()) ? line.getOptionValue("debug") : "";
	}
}
