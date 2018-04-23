package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

public class PatcherMainTest 
{
	@Test
	public void patcherMainConstructor() throws Exception
	{
		PatcherMain pm = new PatcherMain();
		
		assertThat(pm, instanceOf(PatcherMain.class));
	}
	
	@Test
	public void patcherMainHelp() throws Exception
	{
		deleteFiles();
		
		PatcherMain.main("-c resources/testing/patchConfigTest.json -kp resources/testing/testKernel/ -o resources/testing/testKernel/patchOutput/ -h".split("\\s"));
		
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.h")));
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.h")));
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.h")));
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.h")));
		
		deleteFiles();
	}
	
	@Test
	public void patcherMainStandardOutput() throws Exception
	{
		deleteFiles();
		
		PatcherMain.main("-c resources/testing/patchConfigTest.json -kp resources/testing/testKernel/ -o resources/testing/testKernel/patchOutput/".split("\\s"));
		
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.h")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.h")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.h")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.h")));	
		
		deleteFiles();
	}
	
	@Test
	public void patcherMainDebugOutput() throws Exception
	{
		deleteFiles();
		
		PatcherMain.main("-c resources/testing/patchConfigTest.json -kp resources/testing/testKernel/ -o resources/testing/testKernel/patchOutput/ -d".split("\\s"));
		
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.txt")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.txt")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.txt")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.txt")));		
		
		deleteFiles();
		
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.txt")));
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.txt")));
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.txt")));
		assertFalse(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.txt")));		
		
		PatcherMain.main("-c resources/testing/patchConfigTest.json -kp resources/testing/testKernel/ -d resources/testing/testKernel/patchOutput/".split("\\s"));
		
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.txt")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.txt")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.txt")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.txt")));		
		
		deleteFiles();
	}
	
	@Test
	public void patcherMainVerboseOutput() throws Exception
	{
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		System.setErr(new PrintStream(errContent));
		deleteFiles();
		
		PatcherMain.main("-c resources/testing/patchConfigTest.json -kp resources/testing/testKernel/ -d resources/testing/testKernel/patchOutput/ -v".split("\\s"));
		
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/mutex.txt")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/patchTest/spinlock.txt")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_mutex_lock.txt")));
		assertTrue(Files.exists(Paths.get("resources/testing/testKernel/patchOutput/include/linux/lsap_spin_lock.txt")));		
		assertTrue(errContent.toString().length() > 0);
		
		deleteFiles();
		System.setErr(System.err);
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
