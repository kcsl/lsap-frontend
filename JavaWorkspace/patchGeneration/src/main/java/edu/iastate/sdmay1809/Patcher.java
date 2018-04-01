package edu.iastate.sdmay1809;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.cli.ParseException;

public class Patcher 
{
	private static String pathToKernelDirectory;
	private static FileContentBuffer fcb;

	// Collections of functions and macros for a given patched header file
	private static ArrayList<Function> functions;
	private static ArrayList<Macro> macros;
	
	// Collections of criteria for whether or not to accept a particular function or macro into the patch
	private static Set<Criteria> mutexFunctionCriteria;
	private static Set<Criteria> mutexMacroCriteria;
	private static Set<Criteria> spinFunctionCriteria;
	private static Set<Criteria> spinMacroCriteria;
	
	// Lists of files to check for mutex and spin locking functions and macros
	private static Set<String> mutexPaths;
	private static Set<String> spinPaths;
	
	// Debugging variables
	private static boolean doDebug;
	private static String debugPath;
	private static boolean verbose;
	
	// Instance of a command line argument parser
	private static CLIParser parser;
	
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
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException
	{
		// Read the patcher .ini file
		IniReader ini = new IniReader("resources/patcher.ini");
		
		// Initialize and parse the command line arguments
		parser = new CLIParser(args);
		
		// Initialize CLI arguments
		pathToKernelDirectory = parser.getKPVal();
		if (pathToKernelDirectory.length() > 0 && pathToKernelDirectory.charAt(pathToKernelDirectory.length() - 1) != '/') pathToKernelDirectory += "/";
		doDebug = parser.dProvided();
		debugPath = parser.getDVal();
		verbose = parser.vProvided();
					
		verbose = true;
		doDebug = true;
		
		if (verbose)
		{
			System.out.println("Verbose output is ON.\n");
			System.out.println("Patcher information:");
			System.out.println("\tPath to kernel: " + (pathToKernelDirectory.length() > 0 ? pathToKernelDirectory : "."));
			System.out.println("\tDebugging: " + (doDebug ? "ON\n\t\tDebug file: " + debugPath : "OFF") + "\n");
		}
		
		// Initialize the lists of functions and macros
		functions = new ArrayList<Function>();
		macros = new ArrayList<Macro>();
		
		// Initialize the lists of paths
		mutexPaths = ini.getMutexPaths();
		spinPaths = ini.getSpinPaths();
		
		// Initialize the lists of criteria for function/macro selection
		mutexFunctionCriteria = ini.getMutexFunctionCriteria();
		mutexMacroCriteria = ini.getMutexMacroCriteria();
		spinFunctionCriteria = ini.getSpinFunctionCriteria();
		spinMacroCriteria = ini.getSpinMacroCriteria();
		
		// Print mutex locking function and macro criteria if in verbose mode
		if (verbose)
		{
			System.out.println("Mutex Lock Files:");
			
			for (String s : mutexPaths)
			{
				System.out.println("\t" + s);
			}
			
			System.out.println("\nMutex Lock Function Criteria:");
			
			for (Criteria c : mutexFunctionCriteria)
			{
				System.out.println("\t" + c.getNameComponent() + ": " + c.mustHave());
			}
			
			System.out.println("\nMutex Lock Macro Criteria:");
			
			for (Criteria c : mutexMacroCriteria)
			{
				System.out.println("\t" + c.getNameComponent() + ": " + c.mustHave());
			}

			System.out.println("\nSpin Lock Files:");
			
			for (String s : spinPaths)
			{
				System.out.println("\t" + s);
			}
			
			System.out.println("\nSpin Lock Function Criteria:");
			
			for (Criteria c : spinFunctionCriteria)
			{
				System.out.println("\t" + c.getNameComponent() + ": " + c.mustHave());
			}
			
			System.out.println("\nSpin Lock Macro Criteria:");
			
			for (Criteria c : spinMacroCriteria)
			{
				System.out.println("\t" + c.getNameComponent() + ": " + c.mustHave());
			}

			System.out.println();
		}
		
		// Generate the LSAP Mutex Lock file
		generateLSAPMutexLockFile();

		// Initialize the lists of functions and macros
		functions = new ArrayList<Function>();
		macros = new ArrayList<Macro>();

		// Generate the LSAP Spin Lock file
		generateLSAPSpinlockFile();
	}
	
	/**
	 *  Generate the header file source code for lsap_mutex_lock.h
	 */
	private static void generateLSAPMutexLockFile() throws IOException
	{
		// Set the location of the file to write (basically decide if we're writing to a text or header file)
		if (doDebug)
			fcb = new FileContentBuffer(debugPath);
		else
			fcb = new FileContentBuffer(pathToKernelDirectory + "include/linux/lsap_mutex_lock.h");
			
		for (String path : mutexPaths)
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
				if (isLockingFunction(line, mutexFunctionCriteria))
				{
					// Some functions span multiple lines. This adds the next line.
					// If a function with multiple lines (more than 2) is found, this will need to be reworked
					if (!line.contains(");") && s.hasNextLine())
					{
						line += s.nextLine().trim();
					}
									
					// Create a new function and add it to our collection if it doesn't exist
					Function f = new Function(line);
					if (!Function.contains(functions, f.getName())) functions.add(f);
				}
	
				// We also want to analyze the line if it's a locking macro
				else if (isLockingMacro(line, mutexMacroCriteria)) 
				{
					// Macros only span one line. Add the locking macro to our collection if it doesn't exist
					Macro m = new Macro(line);
					if (!Macro.contains(macros, m.getName())) macros.add(m);
				}
			}
			
			// We've scanned the entire file, so close the scanner
			s.close();
		}
		
		if (verbose)
		{
			System.out.println("Some macros and functions have the same name. Removing duplicates:");
		}
		
		// If there are functions and macros with the same name, we want to use the macro, so remove the function
		for (int i = 0; i < functions.size(); i++)
		{
			if (Macro.contains(macros, functions.get(i).getName()))
			{
				if (verbose)
				{
					System.out.println("\t" + functions.get(i).getName());					
				}
				
				functions.remove(functions.get(i--));
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
		if (!doDebug) fcb.print();		
	}
	
	/**
	 *  Generate the header file source code for lsap_spinlock.h
	 * @throws IOException 
	 */
	private static void generateLSAPSpinlockFile() throws IOException
	{
		if (!doDebug)
			fcb = new FileContentBuffer(pathToKernelDirectory + "include/linux/lsap_spinlock.h");
		
		Function lock = new Function("void __raw_spin_lock(void *lock)");
		Function trylock = new Function("int __raw_spin_trylock(void *lock)");
		Function unlock = new Function("void __raw_spin_unlock(void *lock)");
		
		functions.add(lock);
		functions.add(trylock);
		functions.add(unlock);
		
		for (String path : spinPaths)
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
				if (isLockingMacro(line, spinMacroCriteria)) 
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
	
	private static boolean isFunction(String line)
	{
		if (line == null) return false;
		
		// "extern int" is the shortest phrase used by a function we're interested in
		if (line.length() < "extern int".length()) return false;
		
		// All functions we're interested in have a return type of int or void, both are extern as well
		// Of course, to be a function, it must accept parameters, so we check for the start of a list with "("
		return (line.contains("extern int") || line.contains("extern void")) && line.contains("(");
	}
	
	// To be a preprocessor macro definition, the line must contain "#define" but must also accept parameters.
	// We check for the parameters with "("
	private static boolean isPreprocDefineMacro(String line)
	{
		if (line == null) return false;
		
		return line.contains("#define") && line.contains("(");
	}
	
	private static String getFunctionName(String line)
	{
		String functionName = "";
		
		// String.split uses a regex as the first parameter. Since I want to split on "(", I must escape the
		// character so the regex uses it properly. Only interested in the line up to the first "(".
		String[] functionLineParts = line.split("\\(", 2);
		
		functionName = functionLineParts[0];
		
		// The line at this point contains the type of the function or the preprocessor directive, so remove them
		if (functionName.contains("extern int")) functionName = functionName.replace("extern int", "");
		if (functionName.contains("extern void")) functionName = functionName.replace("extern void", "");
		if (functionName.contains("#define")) functionName = functionName.replace("#define", "");
		if (functionName.contains("//")) functionName = functionName.replace("//", "");
		
		// Be sure to remove extra space as well
		return functionName.trim();
	}
	
	private static boolean isLockingFunction(String line, Set<Criteria> criteria)
	{
		// All locking functions must (obviously) be functions
		if (!isFunction(line)) return false;		
		
		// Get the function name
		String functionName = getFunctionName(line);
		
		// Check each of the defined criteria for functions. If all criteria are met, return true
		for (Criteria c : criteria)
		{
			if (functionName.contains(c.getNameComponent()) != c.mustHave())
			{
				return false;
			}
		}
		
		return true;
	}
	
	private static boolean isLockingMacro(String line, Set<Criteria> criteria)
	{
		// All locking macros are preprocessor macro definitions
		if (!isPreprocDefineMacro(line)) return false;
		
		// Get the macro name
		String macroName = getFunctionName(line);
		
		// Check each of the defined criteria for macros. If all criteria are met, return true
		for (Criteria c : criteria)
		{
			if (macroName.contains(c.getNameComponent()) != c.mustHave())
			{
				return false;
			}
		}
		
		return true;
	}
}