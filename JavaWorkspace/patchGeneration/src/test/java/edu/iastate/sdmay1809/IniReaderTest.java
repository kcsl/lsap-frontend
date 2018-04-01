package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class IniReaderTest {
	IniReader ini;
	
	@Before
	public void setUp() throws FileNotFoundException
	{
		ini = new IniReader("resources/IniReaderTest.ini");
	}
	
	@Test
	public void iniReaderConstructor() throws FileNotFoundException
	{
		assertThat(ini, instanceOf(IniReader.class));
	}
	
	@Test
	public void iniReaderGetMutexPaths() throws FileNotFoundException
	{
		Set<String> paths = ini.getMutexPaths();
		
		assertEquals(paths.size(), 1);
		assertTrue(paths.contains("include/linux/mutex.h"));
	}
	
	@Test
	public void iniReaderGetSpinPaths() throws FileNotFoundException
	{
		Set<String> paths = ini.getSpinPaths();
		
		assertEquals(paths.size(), 2);
		assertTrue(paths.contains("include/linux/spinlock.h"));
		assertTrue(paths.contains("include/linux/spinlock_api_smp.h"));
	}
	
	@Test
	public void iniReaderGetMutexFunctionCriteria()
	{
		Set<Criteria> criteria = ini.getMutexFunctionCriteria();
		
		assertEquals(criteria.size(), 3);

		for (Criteria c : criteria)
		{
			switch(c.getNameComponent())
			{
				case "mutex":
				case "lock":
					assertTrue(c.mustHave());
					break;
					
				case "mutex_lock_io_nested":
					assertFalse(c.mustHave());
					break;
				
				default:
					fail("Criteria list contains an unknown criteria!");
			}
		}
	}
	
	@Test
	public void iniReaderGetMutexMacroCriteria()
	{
		Set<Criteria> criteria = ini.getMutexMacroCriteria();
		
		assertEquals(criteria.size(), 2);
		
		for (Criteria c : criteria)
		{
			switch(c.getNameComponent())
			{
				case "mutex":
				case "lock":
					assertTrue(c.mustHave());
					break;
			
				default:
					fail("Criteria list contains an unknown criteria!");
			}
		}
	}
	
	@Test
	public void iniReaderGetSpinFunctionCriteria()
	{
		Set<Criteria> criteria = ini.getSpinFunctionCriteria();
		
		assertEquals(criteria.size(), 3);
		
		for (Criteria c : criteria)
		{
			switch(c.getNameComponent())
			{
				case "spin":
				case "lock":
					assertTrue(c.mustHave());
					break;
					
				case "_is_":
					assertFalse(c.mustHave());
					break;
				
				default:
					fail("Criteria list contains an unknown criteria!");
			}
		}
	}
	
	@Test
	public void iniReaderSpinMacroCriteria()
	{
		Set<Criteria> criteria = ini.getSpinMacroCriteria();
		
		assertEquals(criteria.size(), 2);
		
		for (Criteria c : criteria)
		{
			switch(c.getNameComponent())
			{
				case "lock":
					assertTrue(c.mustHave());
					break;
					
				case "_is_":
					assertFalse(c.mustHave());
					break;
				
				default:
					fail("Criteria list  contains an unknown criteria!");
			}
		}
	}
}
