package patchGeneration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main 
{
	private static final String pathToKernelDirectory = "";
	private static FileContentBuffer fcb;

	// Collections of functions and macros for a given patched header file
	private static ArrayList<Function> functions;
	private static ArrayList<Macro> macros;
	
	// Collections of criteria for whether or not to accept a particular function or macro into the patch
	private static ArrayList<Criteria> functionCriteria;
	private static ArrayList<Criteria> macroCriteria;
	
	// Print to text file or header file
	private static final boolean DEBUG = true;															// REMOVE UPON COMPLETION

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
	 */
	public static void main(String[] args) throws IOException
	{
		// Initialize the lists of functions and macros
		functions = new ArrayList<Function>();
		macros = new ArrayList<Macro>();
		
		// Initialize the lists of criteria for function/macro selection
		functionCriteria = new ArrayList<Criteria>();
		macroCriteria = new ArrayList<Criteria>();
		
		// List the function criteria for mutex locks
		functionCriteria.add(new Criteria("mutex", true));
		functionCriteria.add(new Criteria("lock", true));
		functionCriteria.add(new Criteria("mutex_lock_io_nested", false));
		
		// List the macro criteria for mutex locks
		macroCriteria.add(new Criteria("mutex", true));
		macroCriteria.add(new Criteria("lock", true));

		if (DEBUG)
		{
			System.out.println(""																		);
			System.out.println(""																		);
			System.out.println("/**************************************************************/"		);
			System.out.println("// MUTEX LOCK"															);
			System.out.println("/**************************************************************/"		);
			System.out.println(""																		);
			System.out.println(""																		);
		}
		
		generateLSAPMutexLockFile();

		// Initialize the lists of functions and macros
		functions = new ArrayList<Function>();
		macros = new ArrayList<Macro>();
		
		// Initialize the lists of criteria for function/macro selection
		functionCriteria = new ArrayList<Criteria>();
		macroCriteria = new ArrayList<Criteria>();
		
		// List the function criteria for spinlocks
		
		// List the macro criteria for spinlocks
		macroCriteria.add(new Criteria("lock", true));
		macroCriteria.add(new Criteria("_is_", false));
		macroCriteria.add(new Criteria("_can_", false));
		macroCriteria.add(new Criteria("_before_", false));
		macroCriteria.add(new Criteria("assert", false));
		macroCriteria.add(new Criteria("init", false));
		macroCriteria.add(new Criteria("_raw_spin_unlock_bh", false));
		macroCriteria.add(new Criteria("_raw_spin_unlock_irq", false));
		macroCriteria.add(new Criteria("_raw_spin_unlock_irqrestore", false));
		macroCriteria.add(new Criteria("attribute", false));
		
		if (DEBUG)
		{
			System.out.println(""																		);
			System.out.println(""																		);
			System.out.println("/**************************************************************/"		);
			System.out.println("// SPIN LOCK"															);
			System.out.println("/**************************************************************/"		);
			System.out.println(""																		);
			System.out.println(""																		);
		}

		generateLSAPSpinlockFile();
	}
	
	/**
	 *  Generate the header file source code for lsap_mutex_lock.h
	 */
	private static void generateLSAPMutexLockFile() throws IOException
	{
		// Set the location of the file to write (basically decide if we're writing to a text or header file)
		if (DEBUG)																						// REMOVE UPON COMPLETION
			fcb = new FileContentBuffer(pathToKernelDirectory + "include/linux/lsap_mutex_lock.txt");	// REMOVE UPON COMPLETION
		else																							// REMOVE UPON COMPLETION
			fcb = new FileContentBuffer(pathToKernelDirectory + "include/linux/lsap_mutex_lock.h");
			
		// Start scanning through the unpatched mutex.h header file
		Scanner s = new Scanner(new File(pathToKernelDirectory + "include/linux/mutex.h"));
				
		while (s.hasNextLine())
		{
			String line = s.nextLine();
			
			// Only bother with analysis if the line is a locking function
			if (isLockingFunction(line))
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
			else if (isLockingMacro(line)) 
			{
				// Macros only span one line. Add the locking macro to our collection if it doesn't exist
				Macro m = new Macro(line);
				if (!Macro.contains(macros, m.getName())) macros.add(m);
			}
		}
		
		// We've scanned the entire file, so close the scanner
		s.close();
		
		// If there are functions and macros with the same name, we want to use the macro, so remove the function
		for (int i = 0; i < functions.size(); i++)
		{
			if (Macro.contains(macros, functions.get(i).getName()))
			{
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
		
		// Generate the headings for the patched header file
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
		
		// Print the contents of the patched header file to the header file itself
		fcb.print(DEBUG);		
	}
	
	/**
	 *  Generate the header file source code for lsap_spinlock.h
	 */
	private static void generateLSAPSpinlockFile() throws IOException
	{
		if (DEBUG)																						// REMOVE UPON COMPLETION
			fcb = new FileContentBuffer(pathToKernelDirectory + "include/linux/lsap_spinlock.txt");		// REMOVE UPON COMPLETION
		else																							// REMOVE UPON COMPLETION
			fcb = new FileContentBuffer(pathToKernelDirectory + "include/linux/lsap_spinlock.h");
		
		Function lock = new Function("void __raw_spin_lock(void *lock)");
		Function trylock = new Function("int __raw_spin_trylock(void *lock)");
		Function unlock = new Function("void __raw_spin_unlock(void *lock)");
		
		functions.add(lock);
		functions.add(trylock);
		functions.add(unlock);
		
		for (int i = 0; i < 2; i++)
		{
			// Start scanning through the unpatched mutex.h header file
			Scanner s;
			
			if (i == 0) s = new Scanner(new File(pathToKernelDirectory + "include/linux/spinlock.h"));
			else s = new Scanner(new File(pathToKernelDirectory + "include/linux/spinlock_api_smp.h"));

			while (s.hasNextLine())
			{
				String line = s.nextLine();
				
				// We want to analyze the line if it's a locking macro
				if (isLockingMacro(line)) 
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
		
		fcb.writeln( "#ifndef __LINUX_L_SAP_SPINLOCK_H"										);
		fcb.writeln( "#define __LINUX_L_SAP_SPINLOCK_H"										);
		fcb.writeln( "#include <linux/spinlock_types.h>"									);
		fcb.writeln( ""																		);
		fcb.writeln( ""																		);
		fcb.writeln("/**************************************************************/"		);
		fcb.writeln("// SPIN LOCK"															);
		fcb.writeln("/**************************************************************/"		);

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

		fcb.print(DEBUG);
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
	
	private static boolean isLockingFunction(String line)
	{
		// All locking functions must (obviously) be functions
		if (!isFunction(line)) return false;		
		
		// Get the function name
		String functionName = getFunctionName(line);
		
		// Check each of the defined criteria for functions. If all criteria are met, return true
		for (Criteria c : functionCriteria)
		{
			if (functionName.contains(c.getNameComponent()) != c.mustHave())
			{
				return false;
			}
		}
		
		return true;
	}
	
	private static boolean isLockingMacro(String line)
	{
		// All locking macros are preprocessor macro definitions
		if (!isPreprocDefineMacro(line)) return false;
		
		// Get the macro name
		String macroName = getFunctionName(line);
		
		// Check each of the defined criteria for macros. If all criteria are met, return true
		for (Criteria c : macroCriteria)
		{
			if (macroName.contains(c.getNameComponent()) != c.mustHave())
			{
				return false;
			}
		}
		
		return true;
	}
}