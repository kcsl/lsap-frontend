package edu.iastate.sdmay1809;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PatcherMainTest {
	ByteArrayOutputStream outContent;
	
	@Before
	public void setUp()
	{
		System.setOut(new PrintStream((outContent = new ByteArrayOutputStream())));
	}
	
	@Test
	public void patcherMainDebug() throws IOException, ParseException
	{
		Files.deleteIfExists(Paths.get("resources/testing/lsap_mutex_lock.txt"));
		Files.deleteIfExists(Paths.get("resources/testing/lsap_spinlock.txt"));
		Files.deleteIfExists(Paths.get("include/linux/lsap_mutex_lock.h"));
		Files.deleteIfExists(Paths.get("include/linux/lsap_spinlock.h"));
		
		PatcherMain.main("-h".split("\\s"));
		
		assertFalse(Files.exists(Paths.get("resources/testing/lsap_mutex_lock.txt")));
		assertFalse(Files.exists(Paths.get("resources/testing/lsap_spinlock.txt")));
		assertFalse(Files.exists(Paths.get("include/linux/lsap_mutex_lock.h")));
		assertFalse(Files.exists(Paths.get("include/linux/lsap_spinlock.h")));
		assertTrue(outContent.toString().length() > 0);
		
		System.setOut(new PrintStream((outContent = new ByteArrayOutputStream())));
		Files.deleteIfExists(Paths.get("resources/testing/lsap_mutex_lock.txt"));
		Files.deleteIfExists(Paths.get("resources/testing/lsap_spinlock.txt"));
		Files.deleteIfExists(Paths.get("include/linux/lsap_mutex_lock.h"));
		Files.deleteIfExists(Paths.get("include/linux/lsap_spinlock.h"));
		
		PatcherMain.main("--debug resources/testing".split("\\s"));
		
		assertTrue(Files.exists(Paths.get("resources/testing/lsap_mutex_lock.txt")));
		assertTrue(Files.exists(Paths.get("resources/testing/lsap_spinlock.txt")));
		assertFalse(Files.exists(Paths.get("include/linux/lsap_mutex_lock.h")));
		assertFalse(Files.exists(Paths.get("include/linux/lsap_spinlock.h")));
		assertTrue(outContent.toString().length() > 0);
		
		System.setOut(new PrintStream((outContent = new ByteArrayOutputStream())));
		Files.deleteIfExists(Paths.get("resources/testing/lsap_mutex_lock.txt"));
		Files.deleteIfExists(Paths.get("resources/testing/lsap_spinlock.txt"));
		Files.deleteIfExists(Paths.get("include/linux/lsap_mutex_lock.h"));
		Files.deleteIfExists(Paths.get("include/linux/lsap_spinlock.h"));
		
		PatcherMain.main("-v -i resources/testing/patcherTest.ini".split("\\s"));
		
		assertFalse(Files.exists(Paths.get("resources/testing/lsap_mutex_lock.txt")));
		assertFalse(Files.exists(Paths.get("resources/testing/lsap_spinlock.txt")));
		assertTrue(Files.exists(Paths.get("include/linux/lsap_mutex_lock.h")));
		assertTrue(Files.exists(Paths.get("include/linux/lsap_spinlock.h")));
		assertTrue(outContent.toString().length() > 0);
	}
	
	@After
	public void restoreStreams()
	{
		System.setOut(System.out);
	}
}
