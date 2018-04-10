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
		patcher.setKernelDirectory("");
	}
	
	@Test
	public void patcherConstructor()
	{
		assertThat(patcher, instanceOf(Patcher.class));
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
	public void patcherGenerateLSAPMutexHeaderFile() throws Exception
	{
		System.setOut(new PrintStream(outContent = new ByteArrayOutputStream()));
		assertTrue(outContent.toString().length() == 0);

		Files.deleteIfExists(Paths.get("resources/testing/lsap_mutex_lock.txt"));
		
		patcher.setVerbose(true);
		patcher.setDebugPath("resources/testing/");
		patcher.generateLSAPMutexLockFile();
		
		assertTrue(outContent.toString().length() > 0);
		assertTrue(Files.exists(Paths.get("resources/testing/lsap_mutex_lock.txt")));
		
		Files.deleteIfExists(Paths.get("include/linux/lsap_mutex_lock.h"));		

		patcher.setVerbose(false);
		patcher.setDebugPath(null);
		patcher.generateLSAPMutexLockFile();
		
		assertTrue(Files.exists(Paths.get("include/linux/lsap_mutex_lock.h")));
	}

	@Test
	public void patcherGenerateLSAPSpinLockHeaderFile() throws Exception
	{
		System.setOut(new PrintStream(outContent = new ByteArrayOutputStream()));
		assertTrue(outContent.toString().length() == 0);

		Files.deleteIfExists(Paths.get("resources/lsap_spinlock.txt"));
		
		patcher.setVerbose(true);
		patcher.setDebugPath("resources/testing/");
		patcher.generateLSAPSpinlockFile();
		
		assertTrue(outContent.toString().length() > 0);
		assertTrue(Files.exists(Paths.get("resources/testing/lsap_spinlock.txt")));
		
		Files.deleteIfExists(Paths.get("include/linux/lsap_spinlock.h"));		

		patcher.setVerbose(false);
		patcher.setDebugPath(null);
		patcher.generateLSAPSpinlockFile();
		
		assertTrue(Files.exists(Paths.get("include/linux/lsap_spinlock.h")));
	}
	
	@Test
	public void patcherPatch() throws Exception
	{
		System.setOut(new PrintStream(outContent = new ByteArrayOutputStream()));
		assertTrue(outContent.toString().length() == 0);

		Files.deleteIfExists(Paths.get("resources/testing/lsap_spinlock.txt"));
		Files.deleteIfExists(Paths.get("resources/testing/lsap_mutex_lock.txt"));
		
		patcher.setVerbose(true);
		patcher.setDebugPath("resources/testing/");
		patcher.patch();
		
		assertTrue(outContent.toString().length() > 0);
		assertTrue(Files.exists(Paths.get("resources/testing/lsap_spinlock.txt")));
		assertTrue(Files.exists(Paths.get("resources/testing/lsap_mutex_lock.txt")));
		
		Files.deleteIfExists(Paths.get("include/linux/lsap_spinlock.h"));		
		Files.deleteIfExists(Paths.get("include/linux/lsap_mutex_lock.h"));

		patcher.setVerbose(false);
		patcher.setDebugPath(null);
		patcher.patch();
		
		assertTrue(Files.exists(Paths.get("include/linux/lsap_spinlock.h")));
		assertTrue(Files.exists(Paths.get("include/linux/lsap_mutex_lock.h")));
	}
	
	@Test
	public void patcherGetSetKernelPath()
	{
		patcher.setKernelDirectory("somepath");
		assertEquals(patcher.getKernelDirectory(), "somepath/");
		
		patcher.setKernelDirectory("anotherPath/");
		assertEquals(patcher.getKernelDirectory(), "anotherPath/");

		patcher.setKernelDirectory(null);
		assertEquals(patcher.getKernelDirectory(), "");

		patcher.setKernelDirectory("");
		assertEquals(patcher.getKernelDirectory(), "");
	}
	
	@Test
	public void patcherGetSetDebugPath()
	{
		patcher.setDebugPath("somepath");
		assertEquals(patcher.getDebugPath(), "somepath/");

		patcher.setDebugPath("anotherPath/");
		assertEquals(patcher.getDebugPath(), "anotherPath/");
		
		patcher.setDebugPath("");
		assertEquals(patcher.getDebugPath(), "");

		patcher.setDebugPath(null);
		assertEquals(patcher.getDebugPath(), null);
	}
	
	@Test
	public void patcherGetSetVerbose()
	{
		patcher.setVerbose(true);
		assertTrue(patcher.getVerbose());
		
		patcher.setVerbose(false);
		assertFalse(patcher.getVerbose());
	}
		
	@After
	public void restoreStream()
	{
		System.setOut(System.out);
	}
}
