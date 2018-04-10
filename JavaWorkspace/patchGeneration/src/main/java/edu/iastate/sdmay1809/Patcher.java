package edu.iastate.sdmay1809;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.cli.ParseException;

public class Patcher 
{
	private String pathToKernelDirectory;
	private FileContentBuffer fcb;

	// Collections of functions and macros for a given patched header file
	private TreeSet<Function> functions;
	private TreeSet<Macro> macros;
		
	// Debugging variables
	private String debugPath;
	private boolean verbose;
		
	// The iniReader
	private IniReader ini;
		
	public Patcher(String iniPath) throws IOException, ParseException
	{		
		// Read the patcher .ini file
		ini = new IniReader(iniPath);		
				
		// Initialize the lists of functions and macros
		functions = new TreeSet<Function>();
		macros = new TreeSet<Macro>();	
		
		pathToKernelDirectory = "";
		debugPath = null;
		verbose = false;
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
	 * @throws Exception 
	 */
	public void patch() throws Exception
	{			
		if (verbose)
		{
			System.out.println("Verbose output is ON.\n");
			System.out.println("Patcher information:");
			System.out.println("\tPath to kernel: " + pathToKernelDirectory);
			System.out.println("\tDebugging: " + (debugPath != null ? "ON\n\t\tDebug file: " + debugPath : "OFF") + "\n");
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
	 * @throws Exception 
	 */
	public void generateLSAPMutexLockFile() throws Exception
	{
		functions.clear();
		macros.clear();
		
		// Set the location of the file to write (basically decide if we're writing to a text or header file)
		if (debugPath != null)
			fcb = new FileContentBuffer(Paths.get(debugPath, "lsap_mutex_lock.txt").toString());
		else
			fcb = new FileContentBuffer(Paths.get(pathToKernelDirectory, "include/linux/lsap_mutex_lock.h").toString());
			
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
				
		if (verbose)
		{
			System.out.println("Some macros and functions have the same name. Removing duplicates:");
		}
		
		// If there are functions and macros with the same name, we want to use the macro, so remove the function
		for (Iterator<Function> iter = functions.iterator(); iter.hasNext();)
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
			boolean printed = false;
				
			// See if there's a function that we should use instead. If so, use it
			for (Function f : functions)
			{
				if (f.getName().contains(m.getName()))
				{
					fcb.writeln(m.printAsDefine(f));
					printed = true;
					break;
				}
			}

			if (printed) continue;
			
			fcb.writeln(m.printAsDefine(functions.first()));
		}
		
		// End the patched header file
		fcb.writeln( ""																		);
		fcb.writeln( "#endif /* __LINUX_L_SAP_MUTEX_H */"									);
		fcb.writeln( ""																		);
		
		// Print the contents of the patched header file to the header file itself
		fcb.print(debugPath != null);		
	}
	
	/**
	 *  Generate the header file source code for lsap_spinlock.h
	 * @throws Exception 
	 */
	public void generateLSAPSpinlockFile() throws Exception
	{
		functions.clear();
		macros.clear();
		
		// Set the location of the file to write (basically decide if we're writing to a text or header file)
		if (debugPath != null)
			fcb = new FileContentBuffer(Paths.get(debugPath, "lsap_spinlock.txt").toString());
		else
			fcb = new FileContentBuffer(Paths.get(pathToKernelDirectory, "include/linux/lsap_spinlock.h").toString());
		
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
						
		if (verbose)
		{
			System.out.println("\nSpinlocking Functions Matching Criteria:");
			
			for (Function f : functions)
			{
				System.out.println("\t" + f.getName());
			}
			
			System.out.println("\nSpinlocking Macros Matching Criteria:");
			
			for (Macro m : macros)
			{
				System.out.println("\t" + m.getName());
			}
			
			System.out.println();
		}
		
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
			if (m.getName().contains("try")) fcb.writeln(m.printAsDefine(trylock));
			else if (m.getName().contains("unlock")) fcb.writeln(m.printAsDefine(unlock));
			else fcb.writeln(m.printAsDefine(lock));
		}
		
		fcb.writeln( "" 																	);
		fcb.writeln( "#endif /* __LINUX_L_SAP_SPINLOCK_H */" 								);
		fcb.writeln( ""																		);

		fcb.print(debugPath != null);
	}
	
	public boolean isLockingFunction(String line, Set<Criteria> criteria)
	{
		// Get the function name
		String functionName = Function.getFunctionName(line);

		if (functionName == null) return false;
		
		// Check each of the defined criteria for functions. If all criteria are met, return true
		for (Criteria c : criteria)
		{
			if (functionName.contains(c.getNameComponent()) != c.mustHave()) return false;
		}
		
		return true;
	}
	
	public boolean isLockingMacro(String line, Set<Criteria> criteria)
	{
		// Get the macro name
		String macroName = Macro.getMacroName(line);

		if (macroName == null) return false;
		
		// Check each of the defined criteria for macros. If all criteria are met, return true
		for (Criteria c : criteria)
		{
			if (macroName.contains(c.getNameComponent()) != c.mustHave()) return false;
		}
		
		return true;
	}	
	
	public String getKernelDirectory()
	{
		return pathToKernelDirectory;
	}
	
	public void setKernelDirectory(String kernelPath)
	{
		if (kernelPath == null) pathToKernelDirectory = "";
		else pathToKernelDirectory = kernelPath.trim() + (kernelPath.trim().endsWith("/") ? "" : "/");
		if (pathToKernelDirectory.length() == 1) pathToKernelDirectory = "";
	}
	
	public String getDebugPath()
	{
		return debugPath;
	}
	
	public void setDebugPath(String debugFolder)
	{
		if (debugFolder == null) debugPath = null;
		else
		{
			debugPath = debugFolder.trim() + (debugFolder.trim().endsWith("/") ? "" : "/");
			if (debugPath.length() == 1) debugPath = "";
		}
	}
	
	public boolean getVerbose()
	{
		return verbose;
	}
	
	public void setVerbose(boolean isVerbose)
	{
		verbose = isVerbose;
	}
}