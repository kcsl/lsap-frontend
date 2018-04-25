package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import org.javatuples.Pair;
import org.junit.Before;
import org.junit.Test;

public class PatcherTest {
	final String CONFIG_PATH = "resources/testing/patchConfigTest.json";
	final String KERNEL_PATH = "resources/testing/testKernel/";
	final String OUTPUT_PATH = "resources/testing/testKernel/patchOutput/";
	final String LSAP_MUTEX_CONTENT = "/**************************************************************/\n" +
									  "// MUTEX LOCK\n" + 
									  "/**************************************************************/\n\n" +
									  "#ifndef __LINUX_L_SAP_MUTEX_H\n" + 
									  "#define __LINUX_L_SAP_MUTEX_H\n" +
									  "#include <linux/spinlock_types.h>\n\n" +
									  "// Definition for \"mutex\" related functions with empty bodies.\n" +
									  "static inline int function(void* lock) {return 0;}\n" +
									  "static inline void multi_line_mutex_lock_function_to_remove(struct mutex* lock) {}\n" +
									  "static inline int mutex_lock_function_implementation_to_remove(struct mutex* lock) {return 0;}\n\n" + 
									  "// Define a macro wrapper for extra functions for query unification.\n" +
									  "#define macro(lock) function(NULL)\n" +
									  "#define mutexSingleLineMacroToRemove(lock) multi_line_mutex_lock_function_to_remove(NULL)\n" +
									  "#define mutexMultiLineMacroToRemove(lock) multi_line_mutex_lock_function_to_remove(NULL)\n" +
									  "#define single_line_mutex_lock_function_to_remove(lock) multi_line_mutex_lock_function_to_remove(NULL)\n\n" +
									  "#endif /* __LINUX_L_SAP_MUTEX_H */\n";
	final String MUTEX_INCLUDE_CONTENT = "#ifndef __LINUX_MUTEX_H\n" + 
										 "#define __LINUX_MUTEX_H\n" + 
										 "\n" + 
										 "#include <someFile.h>\n" + 
										 "\n" + 
										 "#define noRemoveSingleLineMacro(lock) macroBody(lock)\n" + 
										 "#define noRemoveMultiLineMacro(lock)	\\\n" + 
										 "		do { 							\\\n" + 
										 "			lock->doSomething();		\\\n" + 
										 "		} while (0);\n" + 
										 "\n" + 
										 "#ifdef SOMETAG\n" +
										 "static inline void noRemoveSingleLineFunctionPrototype(struct mutex *lock);\n" + 
										 "static inline\n" + 
										 "	void noRemoveMultiLineFunctionPrototype(struct mutex *lock)\n" + 
										 "	__acquires(lock);\n" + 
										 "static inline int noRemoveMultiLineFunctionImplementation(struct mutex *lock)\n" + 
										 "{\n" + 
										 "	return lock->doSomething();\n" + 
										 "}\n" +
										 "#endif\n" +
										 "\n" + 
										 "/*\n" + 
										 " * This is a comment.\n" + 
										 " */\n" + 
										 "#include <linux/lsap_mutex_lock.h>\n" +
										 "#ifdef SOMETAG\n" +
										 "static inline void single_line_mutex_lock_function_to_remove(struct mutex *lock);\n" + 
										 "#endif\n\n" +
										 "#define single_line_mutex_lock_function_to_remove(lock) macroBody(lock)\n" + 
										 "#define mutexMultiLineMacroToRemove(lock) 	\\\n" + 
										 "		do {								\\\n" + 
										 "			lock->doSomething();			\\\n" + 
										 "		} while (0);\n" + 
										 "\n" + 
										 "static inline\n" + 
										 "	void multi_line_mutex_lock_function_to_remove(struct mutex *lock)\n" + 
										 "	__acquires(lock);\n" + 
										 "static inline int mutex_lock_function_implementation_to_remove(struct mutex *lock)\n" + 
										 "{\n" + 
										 "	return lock->doSomething();\n" + 
										 "}\n" + 
										 "EXPORT_SYMBOL(mutex_lock_function_implementation_to_remove);\n" + 
										 "\n" + 
										 "static inline struct patchTest *function(void *lock)\n" + 
										 "{\n" + 
										 "	return NULL;\n" + 
										 "}\n" + 
										 "\n" + 
										 "#define macro(lock) function(lock)\n" + 
										 "\n" + 
										 "#endif /* __LINUX_MUTEX_H */";
	final String MUTEX_PATCHED_CONTENT = "#ifndef __LINUX_MUTEX_H\n" + 
										 "#define __LINUX_MUTEX_H\n" + 
										 "\n" + 
										 "#include <someFile.h>\n" + 
										 "\n" + 
										 "#define noRemoveSingleLineMacro(lock) macroBody(lock)\n" + 
										 "#define noRemoveMultiLineMacro(lock)	\\\n" + 
										 "		do { 							\\\n" + 
										 "			lock->doSomething();		\\\n" + 
										 "		} while (0);\n" + 
										 "\n" + 
										 "#ifdef SOMETAG\n" +
										 "static inline void noRemoveSingleLineFunctionPrototype(struct mutex *lock);\n" + 
										 "static inline\n" + 
										 "	void noRemoveMultiLineFunctionPrototype(struct mutex *lock)\n" + 
										 "	__acquires(lock);\n" + 
										 "static inline int noRemoveMultiLineFunctionImplementation(struct mutex *lock)\n" + 
										 "{\n" + 
										 "	return lock->doSomething();\n" + 
										 "}\n" + 
										 "#endif\n" +
										 "\n" + 
										 "/*\n" + 
										 " * This is a comment.\n" + 
										 " */\n" + 
										 "#include <linux/lsap_mutex_lock.h>\n" +
										 "#ifdef SOMETAG\n" +
										 "//static inline void single_line_mutex_lock_function_to_remove(struct mutex *lock);\n" + 
										 "#endif\n\n" +
										 "//#define single_line_mutex_lock_function_to_remove(lock) macroBody(lock)\n" + 
										 "//#define mutexMultiLineMacroToRemove(lock) 	\\\n" + 
										 "//		do {								\\\n" + 
										 "//			lock->doSomething();			\\\n" + 
										 "//		} while (0);\n" + 
										 "\n" + 
										 "//static inline\n" + 
										 "//	void multi_line_mutex_lock_function_to_remove(struct mutex *lock)\n" + 
										 "//	__acquires(lock);\n" + 
										 "//static inline int mutex_lock_function_implementation_to_remove(struct mutex *lock)\n" + 
										 "//{\n" + 
										 "//	return lock->doSomething();\n" + 
										 "//}\n" + 
										 "//EXPORT_SYMBOL(mutex_lock_function_implementation_to_remove);\n" + 
										 "\n" + 
										 "//static inline struct patchTest *function(void *lock)\n" + 
										 "//{\n" + 
										 "//	return NULL;\n" + 
										 "//}\n" + 
										 "\n" + 
										 "//#define macro(lock) function(lock)\n" + 
										 "\n" + 
										 "#endif /* __LINUX_MUTEX_H */";
	final String LSAP_SPIN_CONTENT = "/**************************************************************/\n" +
									 "// SPIN LOCK\n" + 
									 "/**************************************************************/\n\n" +
									 "#ifndef __LINUX_L_SAP_SPIN_H\n" + 
									 "#define __LINUX_L_SAP_SPIN_H\n" +
									 "#include <linux/spinlock_types.h>\n\n" +
									 "// Definition for \"spin\" related functions with empty bodies.\n" +
									 "static inline int function(void* lock) {return 0;}\n" +
									 "static inline void single_line_spin_lock_function_to_remove(struct mutex* lock) {}\n" +
									 "static inline void multi_line_spin_lock_function_to_remove(struct mutex* lock) {}\n" +
									 "static inline int spin_lock_function_implementation_to_remove(struct mutex* lock) {return 0;}\n\n" + 
									 "// Define a macro wrapper for extra functions for query unification.\n" +
									 "#define macro(lock) function(NULL)\n" +
									 "#define spinlockSingleLineMacroToRemove(lock) function(NULL)\n" +
									 "#define spinlockMultiLineMacroToRemove(lock) function(NULL)\n\n" +
									 "#endif /* __LINUX_L_SAP_SPIN_H */\n";
	final String SPIN_INCLUDE_CONTENT = "#ifndef __LINUX_SPINLOCK_H\n" + 
										"#define __LINUX_SPINLOCK_H\n" + 
										"\n" + 
										"#include <someFile.h>\n" + 
										"\n" + 
										"#define noRemoveSingleLineMacro(lock) macroBody(lock)\n" + 
										"#define noRemoveMultiLineMacro(lock)	\\\n" + 
										"		do { 							\\\n" + 
										"			lock->doSomething();		\\\n" + 
										"		} while (0);\n" + 
										"\n" + 
										"static inline void noRemoveSingleLineFunctionPrototype(struct mutex *lock);\n" + 
										"static inline\n" + 
										"	void noRemoveMultiLineFunctionPrototype(struct mutex *lock)\n" + 
										"	__acquires(lock);\n" + 
										"static inline int noRemoveMultiLineFunctionImplementation(struct mutex *lock)\n" + 
										"{\n" + 
										"	return lock->doSomething();\n" + 
										"}\n" + 
										"\n" + 
										"/*\n" + 
										" * This is a comment.\n" + 
										" */\n" + 
										"#include <linux/lsap_spin_lock.h>\n" +
										"#define spinlockSingleLineMacroToRemove(lock) macroBody(lock)\n" + 
										"#define spinlockMultiLineMacroToRemove(lock) 	\\\n" + 
										"		do {									\\\n" + 
										"			lock->doSomething();				\\\n" + 
										"		} while (0);\n" + 
										"\n" + 
										"static inline void single_line_spin_lock_function_to_remove(struct mutex *lock);\n" + 
										"static inline\n" + 
										"	void multi_line_spin_lock_function_to_remove(struct mutex *lock)\n" + 
										"	__acquires(lock);\n" + 
										"static inline int spin_lock_function_implementation_to_remove(struct mutex *lock)\n" + 
										"{\n" + 
										"	return lock->doSomething();\n" + 
										"}\n" + 
										"EXPORT_SYMBOL(spin_lock_function_implementation_to_remove);\n" + 
										"\n" + 
										"static inline struct patchTest *function(void *lock)\n" + 
										"{\n" + 
										"	return NULL;\n" + 
										"}\n" + 
										"\n" + 
										"#define macro(lock) function(lock)\n" + 
										"\n" + 
										"#endif /* __LINUX_MUTEX_H */";
	final String SPIN_PATCHED_CONTENT = "#ifndef __LINUX_SPINLOCK_H\n" + 
										"#define __LINUX_SPINLOCK_H\n" + 
										"\n" + 
										"#include <someFile.h>\n" + 
										"\n" + 
										"#define noRemoveSingleLineMacro(lock) macroBody(lock)\n" + 
										"#define noRemoveMultiLineMacro(lock)	\\\n" + 
										"		do { 							\\\n" + 
										"			lock->doSomething();		\\\n" + 
										"		} while (0);\n" + 
										"\n" + 
										"static inline void noRemoveSingleLineFunctionPrototype(struct mutex *lock);\n" + 
										"static inline\n" + 
										"	void noRemoveMultiLineFunctionPrototype(struct mutex *lock)\n" + 
										"	__acquires(lock);\n" + 
										"static inline int noRemoveMultiLineFunctionImplementation(struct mutex *lock)\n" + 
										"{\n" + 
										"	return lock->doSomething();\n" + 
										"}\n" + 
										"\n" + 
										"/*\n" + 
										" * This is a comment.\n" + 
										" */\n" + 
										"#include <linux/lsap_spin_lock.h>\n" +
										"//#define spinlockSingleLineMacroToRemove(lock) macroBody(lock)\n" + 
										"//#define spinlockMultiLineMacroToRemove(lock) 	\\\n" + 
										"//		do {									\\\n" + 
										"//			lock->doSomething();				\\\n" + 
										"//		} while (0);\n" + 
										"\n" + 
										"//static inline void single_line_spin_lock_function_to_remove(struct mutex *lock);\n" + 
										"//static inline\n" + 
										"//	void multi_line_spin_lock_function_to_remove(struct mutex *lock)\n" + 
										"//	__acquires(lock);\n" + 
										"//static inline int spin_lock_function_implementation_to_remove(struct mutex *lock)\n" + 
										"//{\n" + 
										"//	return lock->doSomething();\n" + 
										"//}\n" + 
										"//EXPORT_SYMBOL(spin_lock_function_implementation_to_remove);\n" + 
										"\n" + 
										"//static inline struct patchTest *function(void *lock)\n" + 
										"//{\n" + 
										"//	return NULL;\n" + 
										"//}\n" + 
										"\n" + 
										"//#define macro(lock) function(lock)\n" + 
										"\n" + 
										"#endif /* __LINUX_MUTEX_H */";
	Patcher patcher;
	
	@Before
	public void setUp() throws Exception
	{
		patcher = new Patcher(PatchConfig.builder(new String[] {"-c", CONFIG_PATH, "-k", KERNEL_PATH, "-o", OUTPUT_PATH}).build());
	}
	
	@Test
	public void patcherConstructor() throws Exception
	{
		deleteFiles();
		
		assertThat(patcher, instanceOf(Patcher.class));
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.h")));
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.h")));
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/lsap_mutex_lock.h")));
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/lsap_spin_lock.h")));
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.txt")));
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.txt")));
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/lsap_mutex_lock.txt")));
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/lsap_spin_lock.txt")));				
	}
	
	@Test
	public void patcherGenerateLSAPMutexHeaderFile() throws Exception
	{
		deleteFiles();		 
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.h")));
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.txt")));
		
		Pair<Set<Function>, Set<Macro>> locks = patcher.generateLSAPMutexHeaderFile();
		
		assertNotNull(locks);
		assertEquals(locks.getValue0().size(), 3);
		assertEquals(locks.getValue1().size(), 4);
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.h")));
		
		String fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.h")).toArray(new String[0]));
		assertEquals(fileContent, LSAP_MUTEX_CONTENT);
		
		patcher = new Patcher(PatchConfig.builder(new String[] {"-c", CONFIG_PATH, "-k", KERNEL_PATH, "-d", OUTPUT_PATH}).build());
		locks = patcher.generateLSAPMutexHeaderFile();
		
		assertNotNull(locks);
		assertEquals(locks.getValue0().size(), 3);
		assertEquals(locks.getValue1().size(), 4);
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.txt")));
		
		fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.txt")).toArray(new String[0]));
		assertEquals(fileContent, LSAP_MUTEX_CONTENT);
		
		deleteFiles();
		patcher = new Patcher(PatchConfig.builder(new String[] {"-c", CONFIG_PATH, "-k", KERNEL_PATH, "-o", OUTPUT_PATH}).build());
	}

	@Test
	public void patcherGenerateLSAPSpinlockHeaderFile() throws Exception
	{
		deleteFiles();
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.h")));
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.txt")));
		 
		Pair<Set<Function>, Set<Macro>> locks = patcher.generateLSAPSpinHeaderFile();
		 
		assertNotNull(locks);
		assertEquals(locks.getValue0().size(), 4);
		assertEquals(locks.getValue1().size(), 3);
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.h")));
		 
		String fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.h")).toArray(new String[0]));
		assertEquals(fileContent, LSAP_SPIN_CONTENT);
		
		patcher = new Patcher(PatchConfig.builder(new String[] {"-c", CONFIG_PATH, "-k", KERNEL_PATH, "-d", OUTPUT_PATH}).build());
		locks = patcher.generateLSAPSpinHeaderFile();
		
		assertNotNull(locks);
		assertEquals(locks.getValue0().size(), 4);
		assertEquals(locks.getValue1().size(), 3);
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.txt")));
		
		fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.txt")).toArray(new String[0]));
		assertEquals(fileContent, LSAP_SPIN_CONTENT);
		 
		deleteFiles();
		patcher = new Patcher(PatchConfig.builder(new String[] {"-c", CONFIG_PATH, "-k", KERNEL_PATH, "-o", OUTPUT_PATH}).build());
	}
	
	@Test
	public void patcherIncludeMutexHeaders() throws Exception
	{
		deleteFiles();
		
		Pair<Set<Function>, Set<Macro>> locks = patcher.generateLSAPMutexHeaderFile();
		
		patcher.addMutexIncludeStatements(locks);
		
		assertNotNull(locks);
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.h")));

		String fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.h")).toArray(new String[0]));
		assertEquals(fileContent, MUTEX_INCLUDE_CONTENT);
		
		deleteFiles();
		patcher = new Patcher(PatchConfig.builder(new String[] {"-c", CONFIG_PATH, "-k", KERNEL_PATH, "-d", OUTPUT_PATH}).build());
		locks = patcher.generateLSAPMutexHeaderFile();
		patcher.addMutexIncludeStatements(locks);
		
		assertNotNull(locks);
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.txt")));
		
		fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.txt")).toArray(new String[0]));
		assertEquals(fileContent, MUTEX_INCLUDE_CONTENT);
		 
		deleteFiles();
		patcher = new Patcher(PatchConfig.builder(new String[] {"-c", CONFIG_PATH, "-k", KERNEL_PATH, "-o", OUTPUT_PATH}).build());
	}
	
	@Test
	public void patcherIncludeSpinHeaders() throws Exception
	{
		deleteFiles();
		
		Pair<Set<Function>, Set<Macro>> locks = patcher.generateLSAPSpinHeaderFile();
		
		patcher.addSpinIncludeStatements(locks);
		
		assertNotNull(locks);
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.h")));

		String fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.h")).toArray(new String[0]));
		assertEquals(fileContent, SPIN_INCLUDE_CONTENT);
		
		deleteFiles();
		patcher = new Patcher(PatchConfig.builder(new String[] {"-c", CONFIG_PATH, "-k", KERNEL_PATH, "-d", OUTPUT_PATH}).build());
		locks = patcher.generateLSAPSpinHeaderFile();
		patcher.addSpinIncludeStatements(locks);
		
		assertNotNull(locks);
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.txt")));
		
		fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.txt")).toArray(new String[0]));
		assertEquals(fileContent, SPIN_INCLUDE_CONTENT);
		 
		deleteFiles();
		patcher = new Patcher(PatchConfig.builder(new String[] {"-c", CONFIG_PATH, "-k", KERNEL_PATH, "-o", OUTPUT_PATH}).build());
	}

	@Test
	public void patcherRemoveMutexDefinitions() throws Exception
	{
		deleteFiles();
		
		Pair<Set<Function>, Set<Macro>> locks = patcher.generateLSAPMutexHeaderFile();
		
		patcher.addMutexIncludeStatements(locks);
		patcher.removeMutexDefinitions(locks);
		
		assertNotNull(locks);
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.h")));

		String fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.h")).toArray(new String[0]));
		assertEquals(fileContent, MUTEX_PATCHED_CONTENT);
		
		deleteFiles();
		patcher = new Patcher(PatchConfig.builder(new String[] {"-c", CONFIG_PATH, "-k", KERNEL_PATH, "-d", OUTPUT_PATH}).build());
		locks = patcher.generateLSAPMutexHeaderFile();
		patcher.addMutexIncludeStatements(locks);
		patcher.removeMutexDefinitions(locks);
		
		assertNotNull(locks);
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.txt")));
		
		fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.txt")).toArray(new String[0]));
		assertEquals(fileContent, MUTEX_PATCHED_CONTENT);
		 
		deleteFiles();
		patcher = new Patcher(PatchConfig.builder(new String[] {"-c", CONFIG_PATH, "-k", KERNEL_PATH, "-o", OUTPUT_PATH}).build());
	}

	@Test
	public void patcherRemoveSpinDefinitions() throws Exception
	{
		deleteFiles();
		
		Pair<Set<Function>, Set<Macro>> locks = patcher.generateLSAPSpinHeaderFile();
		
		patcher.addSpinIncludeStatements(locks);
		patcher.removeSpinDefinitions(locks);
		
		assertNotNull(locks);
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.h")));

		String fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.h")).toArray(new String[0]));
		assertEquals(fileContent, SPIN_PATCHED_CONTENT);
		
		deleteFiles();
		patcher = new Patcher(PatchConfig.builder(new String[] {"-c", CONFIG_PATH, "-k", KERNEL_PATH, "-d", OUTPUT_PATH}).build());
		locks = patcher.generateLSAPSpinHeaderFile();
		patcher.addSpinIncludeStatements(locks);
		patcher.removeSpinDefinitions(locks);
		
		assertNotNull(locks);
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.txt")));
		
		fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.txt")).toArray(new String[0]));
		assertEquals(fileContent, SPIN_PATCHED_CONTENT);
		 
		deleteFiles();
		patcher = new Patcher(PatchConfig.builder(new String[] {"-c", CONFIG_PATH, "-k", KERNEL_PATH, "-o", OUTPUT_PATH}).build());
	}
	
	@Test
	public void patcherPath() throws Exception
	{
		deleteFiles();
		
		patcher.patch();
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.h")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.h")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.h")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.h")));		
		String fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.h")).toArray(new String[0]));
		assertEquals(fileContent, SPIN_PATCHED_CONTENT);
		fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.h")).toArray(new String[0]));
		assertEquals(fileContent, MUTEX_PATCHED_CONTENT);
		fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.h")).toArray(new String[0]));
		assertEquals(fileContent, LSAP_MUTEX_CONTENT);
		fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.h")).toArray(new String[0]));
		assertEquals(fileContent, LSAP_SPIN_CONTENT);

		deleteFiles();
		patcher = new Patcher(PatchConfig.builder(new String[] {"-c", CONFIG_PATH, "-k", KERNEL_PATH, "-d", OUTPUT_PATH}).build());

		patcher.patch();
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.txt")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.txt")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.txt")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.txt")));		
		fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.txt")).toArray(new String[0]));
		assertEquals(fileContent, SPIN_PATCHED_CONTENT);
		fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.txt")).toArray(new String[0]));
		assertEquals(fileContent, MUTEX_PATCHED_CONTENT);
		fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.txt")).toArray(new String[0]));
		assertEquals(fileContent, LSAP_MUTEX_CONTENT);
		fileContent = String.join("\n", Files.readAllLines(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.txt")).toArray(new String[0]));
		assertEquals(fileContent, LSAP_SPIN_CONTENT);
		
		deleteFiles();
		patcher = new Patcher(PatchConfig.builder(new String[] {"-c", CONFIG_PATH, "-k", KERNEL_PATH, "-o", OUTPUT_PATH}).build());
	}

	private void deleteFiles() throws IOException
	{
		Files.deleteIfExists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.h"));
		Files.deleteIfExists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex2.h"));
		Files.deleteIfExists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.h"));
		Files.deleteIfExists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock2.h"));
		Files.deleteIfExists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.h"));
		Files.deleteIfExists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.h"));

		Files.deleteIfExists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.txt"));
		Files.deleteIfExists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex2.txt"));
		Files.deleteIfExists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.txt"));
		Files.deleteIfExists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock2.txt"));
		Files.deleteIfExists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.txt"));
		Files.deleteIfExists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.txt"));
		
		Files.deleteIfExists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/"));
		Files.deleteIfExists(Paths.get("resources/testing/testKernel/patchOutput/include/"));
		Files.deleteIfExists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/"));
		Files.deleteIfExists(Paths.get("resources/testing/testKernel/patchOutput/"));
	}
}
