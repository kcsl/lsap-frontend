package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PatcherTest {
	Patcher patcher;
	ByteArrayOutputStream outContent;
	
	@Before
	public void setUp() throws IOException, ParseException
	{
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
		patcher = new Patcher("resources/testing/patcherTest.ini");
	}
	
	@Test
	public void patcherConstructor()
	{
		assertThat(patcher, instanceOf(Patcher.class));
	}
	
	@Test
	public void patcherIsFunction()
	{
		assertTrue(patcher.isFunction(" extern int func(int someParam)"));
		assertTrue(patcher.isFunction("extern void func(double someParam)"));
		assertTrue(patcher.isFunction(" extern int __must_check func(struct mutex *lock)"));
		assertTrue(patcher.isFunction("extern void __must_check func()"));
		assertTrue(patcher.isFunction("int func(int someParam)"));
		assertTrue(patcher.isFunction(" void func(double someParam)"));
		assertTrue(patcher.isFunction("int __must_check func(struct mutex *lock)"));
		assertTrue(patcher.isFunction(" void __must_check func()"));
		
		assertFalse(patcher.isFunction(" extern int func;"));
		assertFalse(patcher.isFunction("extern void func;"));
		assertFalse(patcher.isFunction(" extern int __must_check func;"));
		assertFalse(patcher.isFunction("extern void __must_check func;"));
		assertFalse(patcher.isFunction(" int func;"));
		assertFalse(patcher.isFunction("void func;"));
		assertFalse(patcher.isFunction(" int __must_check func;"));
		assertFalse(patcher.isFunction("void __must_check func;"));
		assertFalse(patcher.isFunction(" return var != NULL;"));
		assertFalse(patcher.isFunction(null));
	}
	
	@Test
	public void patcherGetFunctionName()
	{
		assertEquals(patcher.getFunctionName(" extern int func(int someParam)"), "func");
		assertEquals(patcher.getFunctionName(" extern int test(int someParam)"), "test");
		assertEquals(patcher.getFunctionName(" extern int apple(int someParam)"), "apple");
		assertEquals(patcher.getFunctionName(" extern int windows(int someParam)"), "windows");
		assertEquals(patcher.getFunctionName(" extern int micro(int someParam)"), "micro");
		assertEquals(patcher.getFunctionName(" extern int jimmy(int someParam)"), "jimmy");
		assertEquals(patcher.getFunctionName(null), null);
		assertEquals(patcher.getFunctionName(" extern int jimmy;"), null);
	}
	
	@Test
	public void patcherIsLockingFunction()
	{
		Set<Criteria> criteria = new HashSet<Criteria>();
		
		criteria.add(new Criteria("spin", true));
		criteria.add(new Criteria("lock", true));
		criteria.add(new Criteria("cabbage", false));
		
		assertTrue(patcher.isLockingFunction("extern int spin_something_lock (struct mutex *lock)", criteria));
		assertTrue(patcher.isLockingFunction("extern void __must_check _old_spin_lock()", criteria));
		assertTrue(patcher.isLockingFunction(" void spinlock()", criteria));
		assertTrue(patcher.isLockingFunction(" int __must_check locking_spin(lock)", criteria));
		
		assertFalse(patcher.isLockingFunction("# int spinning_cabbage()", criteria));
		assertFalse(patcher.isLockingFunction("extern void __must_check spinlock_cabbage (struct mutex *lock)", criteria));
		assertFalse(patcher.isLockingFunction(" extern void lock_the_cabbage_up (int lock)", criteria));
		assertFalse(patcher.isLockingFunction(" extern int spinlock;", criteria));
		assertFalse(patcher.isLockingFunction(null, criteria));
	}

	@Test
	public void patcherIsMacro()
	{
		assertTrue(patcher.isPreprocDefineMacro("# define macro(lock)"));
		assertTrue(patcher.isPreprocDefineMacro("#  define macro (lock)"));
		assertTrue(patcher.isPreprocDefineMacro(" #define macro( lock )"));
		assertTrue(patcher.isPreprocDefineMacro("# define macro (lock)"));
		
		assertFalse(patcher.isPreprocDefineMacro("# define macro 1"));
		assertFalse(patcher.isPreprocDefineMacro("#  define macro lock"));
		assertFalse(patcher.isPreprocDefineMacro(" #define macro something_else"));
		assertFalse(patcher.isPreprocDefineMacro("# define macro"));
		assertFalse(patcher.isPreprocDefineMacro(null));
	}
	
	@Test
	public void patcherGetMacroName()
	{
		assertEquals(patcher.getMacroName("# define macro(lock)"), "macro");
		assertEquals(patcher.getMacroName("# define test(lock)"), "test");
		assertEquals(patcher.getMacroName("# define apple(lock)"), "apple");
		assertEquals(patcher.getMacroName("# define windows(lock)"), "windows");
		assertEquals(patcher.getMacroName("# define micro(lock)"), "micro");
		assertEquals(patcher.getMacroName("# define jimmy(lock)"), "jimmy");
		assertEquals(patcher.getMacroName(null), null);
		assertEquals(patcher.getMacroName("# define jimmy"), null);
	}
	
	@Test
	public void patcherIsLockingMacro()
	{
		Set<Criteria> criteria = new HashSet<Criteria>();
		
		criteria.add(new Criteria("spin", true));
		criteria.add(new Criteria("lock", true));
		criteria.add(new Criteria("cabbage", false));
		
		assertTrue(patcher.isLockingMacro("# define spin_something_lock (lock)", criteria));
		assertTrue(patcher.isLockingMacro("#  define _old_spin_lock(lock)", criteria));
		assertTrue(patcher.isLockingMacro(" #define spinlock( lock )", criteria));
		assertTrue(patcher.isLockingMacro("# define locking_spin(lock)", criteria));
		
		assertFalse(patcher.isLockingMacro("# define spinning_cabbage(lock)", criteria));
		assertFalse(patcher.isLockingMacro("#  define spinlock_cabbage(lock)", criteria));
		assertFalse(patcher.isLockingMacro(" #define lock_the_cabbage_up (lock)", criteria));
		assertFalse(patcher.isLockingMacro("# define spinlock true", criteria));
		assertFalse(patcher.isLockingMacro(null, criteria));
	}
	
	@Test
	public void patcherGenerateLSAPMutexHeaderFile() throws IOException, ParseException
	{
		System.setOut(new PrintStream(outContent = new ByteArrayOutputStream()));
		assertTrue(outContent.toString().length() == 0);

		Files.deleteIfExists(Paths.get("resources/testing/lsap_mutex_lock.txt"));
		
		patcher.parseArgs("-v -d resources/testing/".split("\\s"));
		patcher.generateLSAPMutexLockFile();
		
		assertTrue(outContent.toString().length() > 0);
		assertTrue(Files.exists(Paths.get("resources/testing/lsap_mutex_lock.txt")));
		
		Files.deleteIfExists(Paths.get("include/linux/lsap_mutex_lock.h"));		

		patcher.parseArgs("".split("\\s"));
		patcher.generateLSAPMutexLockFile();
		
		assertTrue(Files.exists(Paths.get("include/linux/lsap_mutex_lock.h")));
	}

	@Test
	public void patcherGenerateLSAPSpinLockHeaderFile() throws IOException, ParseException
	{
		System.setOut(new PrintStream(outContent = new ByteArrayOutputStream()));
		assertTrue(outContent.toString().length() == 0);

		Files.deleteIfExists(Paths.get("resources/lsap_spinlock.txt"));
		
		patcher.parseArgs("-v -d resources/testing/".split("\\s"));
		patcher.generateLSAPSpinlockFile();
		
		assertTrue(outContent.toString().length() > 0);
		assertTrue(Files.exists(Paths.get("resources/testing/lsap_spinlock.txt")));
		
		Files.deleteIfExists(Paths.get("include/linux/lsap_spinlock.h"));		

		patcher.parseArgs("".split("\\s"));
		patcher.generateLSAPSpinlockFile();
		
		assertTrue(Files.exists(Paths.get("include/linux/lsap_spinlock.h")));
	}
	
	@Test
	public void patcherPatch() throws IOException, ParseException
	{
		System.setOut(new PrintStream(outContent = new ByteArrayOutputStream()));
		assertTrue(outContent.toString().length() == 0);

		Files.deleteIfExists(Paths.get("resources/testing/lsap_spinlock.txt"));
		Files.deleteIfExists(Paths.get("resources/testing/lsap_mutex_lock.txt"));
		
		patcher.patch("-v -d resources/testing/".split("\\s"));
		
		assertTrue(outContent.toString().length() > 0);
		assertTrue(Files.exists(Paths.get("resources/testing/lsap_spinlock.txt")));
		assertTrue(Files.exists(Paths.get("resources/testing/lsap_mutex_lock.txt")));
		
		Files.deleteIfExists(Paths.get("include/linux/lsap_spinlock.h"));		
		Files.deleteIfExists(Paths.get("include/linux/lsap_mutex_lock.h"));

		patcher.patch("-v".split("\\s"));
		
		assertTrue(Files.exists(Paths.get("include/linux/lsap_spinlock.h")));
		assertTrue(Files.exists(Paths.get("include/linux/lsap_mutex_lock.h")));
	}
	
	@Test
	public void patcherHelp() throws IOException, ParseException
	{
		System.setOut(new PrintStream(outContent = new ByteArrayOutputStream()));
		assertTrue(outContent.toString().length() == 0);

		patcher.patch("-h".split("\\s"));
		
		assertTrue(outContent.toString().length() > 0);
		
		System.setOut(new PrintStream(outContent = new ByteArrayOutputStream()));
		assertTrue(outContent.toString().length() == 0);

		patcher.patch("-h -v".split("\\s"));
		
		assertTrue(outContent.toString().length() > 0);
		assertFalse(outContent.toString().contains("Verbose output is ON."));
	}
	
	@Test
	public void patcherParseArgs() throws IOException, ParseException
	{		
		patcher.parseArgs("-v".split("\\s"));
		assertTrue(patcher.getCommandLine().hasOption('v'));
		
		patcher.parseArgs("-d".split("\\s"));
		assertTrue(patcher.getCommandLine().hasOption('d'));
		assertTrue(patcher.getCommandLine().getOptionValue("debug", "resources/PatcherDebug/").equals("resources/PatcherDebug/"));

		patcher.parseArgs("--debug somepath/".split("\\s"));
		assertTrue(patcher.getCommandLine().hasOption('d'));
		assertTrue(patcher.getCommandLine().getOptionValue("debug", "resources/PatcherDebug/").equals("somepath/"));

		patcher.parseArgs("-kp".split("\\s"));
		assertTrue(patcher.getCommandLine().hasOption("kp"));
		assertTrue(patcher.getCommandLine().getOptionValue("kernel-path", "./").equals("./"));

		patcher.parseArgs("--kernel-path somepath/".split("\\s"));
		assertTrue(patcher.getCommandLine().hasOption("kp"));
		assertTrue(patcher.getCommandLine().getOptionValue("kernel-path", "./").equals("somepath/"));

		patcher.parseArgs("-v -d".split("\\s"));
		assertTrue(patcher.getCommandLine().hasOption('v'));
		assertTrue(patcher.getCommandLine().hasOption('d'));
		assertTrue(patcher.getCommandLine().getOptionValue("debug", "resources/PatcherDebug/").equals("resources/PatcherDebug/"));

		patcher.parseArgs("-v --debug somepath/".split("\\s"));
		assertTrue(patcher.getCommandLine().hasOption('v'));		
		assertTrue(patcher.getCommandLine().hasOption('d'));
		assertTrue(patcher.getCommandLine().getOptionValue("debug", "resources/PatcherDebug/").equals("somepath/"));

		patcher.parseArgs("-v -kp".split("\\s"));
		assertTrue(patcher.getCommandLine().hasOption('v'));
		assertTrue(patcher.getCommandLine().hasOption("kp"));
		assertTrue(patcher.getCommandLine().getOptionValue("kernel-path", "./").equals("./"));

		patcher.parseArgs("-v --kernel-path somepath/".split("\\s"));
		assertTrue(patcher.getCommandLine().hasOption('v'));		
		assertTrue(patcher.getCommandLine().hasOption("kp"));
		assertTrue(patcher.getCommandLine().getOptionValue("kernel-path", "./").equals("somepath/"));

		patcher.parseArgs("-d --kernel-path somepath/".split("\\s"));
		assertTrue(patcher.getCommandLine().hasOption('d'));
		assertTrue(patcher.getCommandLine().getOptionValue("debug", "resources/PatcherDebug/").equals("resources/PatcherDebug/"));
		assertTrue(patcher.getCommandLine().hasOption("kp"));
		assertTrue(patcher.getCommandLine().getOptionValue("kernel-path", "./").equals("somepath/"));

		patcher.parseArgs("-d somepath/ --kernel-path somepath/".split("\\s"));
		assertTrue(patcher.getCommandLine().hasOption('d'));
		assertTrue(patcher.getCommandLine().getOptionValue("debug", "resources/PatcherDebug/").equals("somepath/"));
		assertTrue(patcher.getCommandLine().hasOption("kp"));
		assertTrue(patcher.getCommandLine().getOptionValue("kernel-path", "./").equals("somepath/"));

		patcher.parseArgs("-d somepath/ --kernel-path".split("\\s"));
		assertTrue(patcher.getCommandLine().hasOption('d'));
		assertTrue(patcher.getCommandLine().getOptionValue("debug", "resources/PatcherDebug/").equals("somepath/"));
		assertTrue(patcher.getCommandLine().hasOption("kp"));
		assertTrue(patcher.getCommandLine().getOptionValue("kernel-path", "./").equals("./"));

		patcher.parseArgs("-d --kernel-path".split("\\s"));
		assertTrue(patcher.getCommandLine().hasOption('d'));
		assertTrue(patcher.getCommandLine().getOptionValue("debug", "resources/PatcherDebug/").equals("resources/PatcherDebug/"));
		assertTrue(patcher.getCommandLine().hasOption("kp"));
		assertTrue(patcher.getCommandLine().getOptionValue("kernel-path", "./").equals("./"));

		patcher.parseArgs("-d somepath/ -v --kernel-path somepath".split("\\s"));
		assertTrue(patcher.getCommandLine().hasOption('d'));
		assertTrue(patcher.getCommandLine().getOptionValue("debug", "resources/PatcherDebug/").equals("somepath/"));
		assertTrue(patcher.getCommandLine().hasOption('v'));
		assertTrue(patcher.getCommandLine().hasOption("kp"));
		assertTrue(patcher.getCommandLine().getOptionValue("kernel-path", "./").equals("somepath"));
		
		patcher.parseArgs("-d somepath -v --kernel-path somepath/ -h".split("\\s"));
		assertTrue(patcher.getCommandLine().hasOption('d'));
		assertTrue(patcher.getCommandLine().getOptionValue("debug", "resources/PatcherDebug/").equals("somepath"));
		assertTrue(patcher.getCommandLine().hasOption('v'));
		assertTrue(patcher.getCommandLine().hasOption("kp"));
		assertTrue(patcher.getCommandLine().getOptionValue("kernel-path", "./").equals("somepath/"));
		assertTrue(patcher.getCommandLine().hasOption('h'));
	}

	@After
	public void restoreStream()
	{
		System.setOut(System.out);
	}
}