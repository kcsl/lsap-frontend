package edu.iastate.sdmay1809;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Patcher 
{
	private String pathToKernelDirectory;
	private FileContentBuffer fcb;

	// Collections of functions and macros for a given patched header file
	private ArrayList<Function> functions;
	private ArrayList<Macro> macros;
		
	// Debugging variables
	private boolean doDebug;
	private String debugPath;
	private boolean verbose;
		
	// The iniReader
	private IniReader ini;
	
	// CLI Parsing
	private CommandLineParser parser;
	private Options options;
	private CommandLine line;
	
	public Patcher(String iniPath) throws IOException, ParseException
	{		
		// Read the patcher .ini file
		ini = new IniReader(iniPath);		
				
		// Initialize the lists of functions and macros
		functions = new ArrayList<Function>();
		macros = new ArrayList<Macro>();				
	}
	
	/**
	 * This main method generates the patch for a given version of the linux kernel located at
	 * the path specified in the pathToKernelDirectory constant variable.
	 * 
	 * TODO:
	 * 		LSAP_MUTEX_LOCK:
	 * 			Comment out original function implementation
	 * 
	 * 		LSAP_SPINLOCK:
	 * 			Comment out original function implementation
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public void patch(String[] cliArgs) throws IOException, ParseException
	{
		parseArgs(cliArgs);

		if (line.hasOption('h'))
		{
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("patcher", "Options available", options, "", true);
			return;
		}
						
		if (verbose)
		{
			System.out.println("Verbose output is ON.\n");
			System.out.println("Patcher information:");
			System.out.println("\tPath to kernel: " + pathToKernelDirectory);
			System.out.println("\tDebugging: " + (doDebug ? "ON\n\t\tDebug file: " + debugPath : "OFF") + "\n");
			System.out.println("Mutex Lock Files:");
			
			for (String s : ini.getPaths("MutexPaths"))
			{
				System.out.println("\t" + s);
			}
			
			System.out.println("\nMutex Lock Function Criteria:");
			
			for (Criteria c : ini.getCriteria("MutexFunctionCriteria"))
			{
				System.out.println("\t" + c.getNameComponent() + ": " + c.mustHave());
			}
			
			System.out.println("\nMutex Lock Macro Criteria:");
			
			for (Criteria c : ini.getCriteria("MutexMacroCriteria"))
			{
				System.out.println("\t" + c.getNameComponent() + ": " + c.mustHave());
			}

			System.out.println("\nSpin Lock Files:");
			
			for (String s : ini.getPaths("SpinPaths"))
			{
				System.out.println("\t" + s);
			}
			
			System.out.println("\nSpin Lock Function Criteria:");
			
			for (Criteria c : ini.getCriteria("SpinFunctionCriteria"))
			{
				System.out.println("\t" + c.getNameComponent() + ": " + c.mustHave());
			}
			
			System.out.println("\nSpin Lock Macro Criteria:");
			
			for (Criteria c : ini.getCriteria("SpinMacroCriteria"))
			{
				System.out.println("\t" + c.getNameComponent() + ": " + c.mustHave());
			}

			System.out.println();
		}		
		
		// Generate the LSAP Mutex Lock file
		generateLSAPMutexLockFile();

		// Generate the LSAP Spin Lock file
		generateLSAPSpinlockFile();
	}
	
	/**
	 *  Generate the header file source code for lsap_mutex_lock.h
	 */
	public void generateLSAPMutexLockFile() throws IOException
	{
		functions.clear();
		macros.clear();
		
		// Set the location of the file to write (basically decide if we're writing to a text or header file)
		if (doDebug)
			fcb = new FileContentBuffer(debugPath + "lsap_mutex_lock.txt");
		else
			fcb = new FileContentBuffer(pathToKernelDirectory + "include/linux/lsap_mutex_lock.h");
			
		for (String path : ini.getPaths("MutexPaths"))
		{		
			// Start scanning through the unpatched mutex file
			Scanner s;
			
			try
			{
				s = new Scanner(new File(pathToKernelDirectory + path));
			}
			
			catch(FileNotFoundException e)
			{
				System.out.println("ERROR: Could not locate file \"" + path + "\". Skipping it.");
				continue;
			}
						
			while (s.hasNextLine())
			{
				String line = s.nextLine();
				
				// Only bother with analysis if the line is a locking function
				if (isLockingFunction(line, ini.getCriteria("MutexFunctionCriteria")))
				{
					// Some functions span multiple lines. This adds the next line.
					// If a function with multiple lines (more than 2) is found, this will need to be reworked
					if (!line.contains(");"))
					{
						line += s.nextLine().trim();
					}
									
					// Create a new function and add it to our collection
					functions.add(new Function(line));
				}
	
				// We also want to analyze the line if it's a locking macro
				else if (isLockingMacro(line, ini.getCriteria("MutexMacroCriteria"))) 
				{
					// Macros only span one line. Add the locking macro to our collection
					macros.add(new Macro(line));
				}
			}
			
			// We've scanned the entire file, so close the scanner
			s.close();
		}
		
		// Remove duplicates
		Set<Function> tempFunctions = new HashSet<Function>();
		Set<Macro> tempMacros = new HashSet<Macro>();
		
		tempFunctions.addAll(functions);
		functions.clear();
		functions.addAll(tempFunctions);
		
		tempMacros.addAll(macros);
		macros.clear();
		macros.addAll(tempMacros);
		
		if (verbose)
		{
			System.out.println("Some macros and functions have the same name. Removing duplicates:");
		}
		
		// If there are functions and macros with the same name, we want to use the macro, so remove the function
		for (Iterator<Function> iter = functions.listIterator(); iter.hasNext();)
		{
			Function f = iter.next();
			
			if (Macro.contains(macros, f.getName()))
			{
				if (verbose)
				{
					System.out.println("\t" + f.getName());					
				}
				
				iter.remove();
			}
		}

		// For macros, we need to define which function they'll end up calling.
		// It seems like the name of the function we want to call contains the name of the macro
		for (Macro m : macros)
		{
			// Set a default function to call for the macro
			m.setMacroBody(functions.get(0));
			
			// See if there's a function that we should use instead. If so, use it
			for (Function f : functions)
			{
				if (f.getName().contains(m.getName()))
				{
					m.setMacroBody(f);
					break;
				}
			}
		}
		
		Collections.sort(functions);
		Collections.sort(macros);
		
		if (verbose)
		{
			System.out.println("\nMutex Locking Functions Matching Criteria:");
			
			for (Function f : functions)
			{
				System.out.println("\t" + f.getName());
			}
			
			System.out.println("\nMutex Locking Macros Matching Criteria:");
			
			for (Macro m : macros)
			{
				System.out.println("\t" + m.getName());
			}
			
			System.out.println();
		}
		
		// Generate the headings for the patched header file
		fcb.writeln("/**************************************************************/"		);
		fcb.writeln("// MUTEX LOCK"															);
		fcb.writeln("/**************************************************************/"		);
		fcb.writeln( "" 																	);
		fcb.writeln( "#ifndef __LINUX_L_SAP_MUTEX_H"										);
		fcb.writeln( "#define __LINUX_L_SAP_MUTEX_H"										);
		fcb.writeln( "#include <linux/spinlock_types.h>"									);
		fcb.writeln( ""																		);
		fcb.writeln( "// Definition for \"mutex\" related functions with empty bodies."		);
		
		// Write all of the functions to the patched header file
		for (Function f : functions)
		{
				fcb.writeln(f.convertToStaticInline());
		}

		// Comment denotes the start of the macro definitions
		fcb.writeln( ""																		);
		fcb.writeln( "// Define a macro wrapper for extra functions for query unification." );		
		
		// Write all of the macros to the patched header file
		for (Macro m : macros)
		{
			fcb.writeln(m.printAsDefine());
		}
		
		// End the patched header file
		fcb.writeln( ""																		);
		fcb.writeln( "#endif /* __LINUX_L_SAP_MUTEX_H */"									);
		fcb.writeln( ""																		);
		
		// Print the contents of the patched header file to the header file itself
		fcb.print(doDebug);		
	}
	
	/**
	 *  Generate the header file source code for lsap_spinlock.h
	 * @throws IOException 
	 */
	public void generateLSAPSpinlockFile() throws IOException
	{
		functions.clear();
		macros.clear();
		
		// Set the location of the file to write (basically decide if we're writing to a text or header file)
		if (doDebug)
			fcb = new FileContentBuffer(debugPath + "lsap_spinlock.txt");
		else
			fcb = new FileContentBuffer(pathToKernelDirectory + "include/linux/lsap_spinlock.h");
		
		Function lock = new Function("void __raw_spin_lock(void *lock)");
		Function trylock = new Function("int __raw_spin_trylock(void *lock)");
		Function unlock = new Function("void __raw_spin_unlock(void *lock)");
		
		functions.add(lock);
		functions.add(trylock);
		functions.add(unlock);
		
		for (String path : ini.getPaths("SpinPaths"))
		{
			Scanner s;
			
			// Start scanning through the unpatched spinlock header file. Skip if we can't find it
			try
			{
				s = new Scanner(new File(pathToKernelDirectory + path));
			}
			
			catch(FileNotFoundException e)
			{
				System.out.println("ERROR: Could not locate file \"" + path + "\". Skipping it.");
				continue;
			}
			
			while (s.hasNextLine())
			{
				String line = s.nextLine();
				
				// We want to analyze the line if it's a locking macro
				if (isLockingMacro(line, ini.getCriteria("SpinMacroCriteria"))) 
				{
					// Macros only span one line. Add the locking macro to our collection if it doesn't exist
					Macro m = new Macro(line);
					if (!Macro.contains(macros, m.getName())) macros.add(m);
				}
			}
			
			// We've scanned the entire file, so close the scanner
			s.close();
		}
		
		for (Macro m : macros)
		{
			if (m.getName().contains("try")) m.setMacroBody(trylock);
			else if (m.getName().contains("unlock")) m.setMacroBody(unlock);
			else m.setMacroBody(lock);
		}
				
		Collections.sort(functions);
		Collections.sort(macros);
		
		fcb.writeln("/**************************************************************/"		);
		fcb.writeln("// SPIN LOCK"															);
		fcb.writeln("/**************************************************************/"		);
		fcb.writeln( "" 																	);
		fcb.writeln( "#ifndef __LINUX_L_SAP_SPINLOCK_H"										);
		fcb.writeln( "#define __LINUX_L_SAP_SPINLOCK_H"										);
		fcb.writeln( "#include <linux/spinlock_types.h>"									);
		fcb.writeln( "" 																	);

		for (Function f : functions)
		{
			fcb.writeln(f.convertToStaticInline());
		}
		
		fcb.writeln( "" 																	);
		fcb.writeln( "// Define a macro wrapper for extra functions for query unification." );

		for (Macro m : macros)
		{
			fcb.writeln(m.printAsDefine());
		}
		
		fcb.writeln( "" 																	);
		fcb.writeln( "#endif /* __LINUX_L_SAP_SPINLOCK_H */" 								);
		fcb.writeln( ""																		);

		fcb.print(doDebug);
	}
	
	public boolean isFunction(String line)
	{
		if (line == null) return false;
				
		// All functions we're interested in have a return type of int or void
		// Of course, to be a function, it must accept parameters, so we check for the start of a list with "("
		return line.matches("^\\s*(extern)?\\s*(int|void){1}\\s*(__must_check)?\\s*[A-Za-z0-9_]+\\s*\\(.*$");
	}
	
	public boolean isPreprocDefineMacro(String line)
	{
		if (line == null) return false;
		
		// To be a preprocessor macro definition, the line must contain "#define" but must also accept parameters.
		// We check for the parameters with "("
		return line.matches("^\\s*#\\s*define\\s+[A-Za-z0-9_]+\\s*\\(.*$");
	}
	
	public String getFunctionName(String line)
	{
		if (line == null || !isFunction(line)) return null;
		
		return line.replaceFirst("^\\s*(extern)?\\s*(int|void){1}\\s*(__must_check)?\\s*", "").replaceFirst("\\s*\\(.*$", "").trim();		
	}
	
	public String getMacroName(String line)
	{
		if (line == null || !isPreprocDefineMacro(line)) return null;
		
		return line.replaceFirst("^\\s*#\\s*define\\s+", "").replaceFirst("\\(.*$", "").trim();
	}
	
	public boolean isLockingFunction(String line, Set<Criteria> criteria)
	{
		// All locking functions must (obviously) be functions
		if (!isFunction(line)) return false;		
		
		// Get the function name
		String functionName = getFunctionName(line);
		
		// Check each of the defined criteria for functions. If all criteria are met, return true
		for (Criteria c : criteria)
		{
			if (functionName.contains(c.getNameComponent()) != c.mustHave()) return false;
		}
		
		return true;
	}
	
	public boolean isLockingMacro(String line, Set<Criteria> criteria)
	{
		// All locking macros are preprocessor macro definitions
		if (!isPreprocDefineMacro(line)) return false;
		
		// Get the macro name
		String macroName = getMacroName(line);
		
		// Check each of the defined criteria for macros. If all criteria are met, return true
		for (Criteria c : criteria)
		{
			if (macroName.contains(c.getNameComponent()) != c.mustHave()) return false;
		}
		
		return true;
	}
	
	public void parseArgs(String[] args) throws ParseException
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
		
		// Initialize CLI arguments
		pathToKernelDirectory = line.getOptionValue("kernel-path", "./").trim();
		pathToKernelDirectory += (pathToKernelDirectory.charAt(pathToKernelDirectory.length() - 1) != '/') ? "/" : "";
		doDebug = line.hasOption('d');
		debugPath = line.getOptionValue("debug", "resources/PatcherDebug/").trim();
		debugPath += (debugPath.charAt(debugPath.length() - 1) != '/') ? "/" : "";
		verbose = line.hasOption('v');
	}
	
	public CommandLine getCommandLine()
	{
		return line;
	}
}