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
		ini = new IniReader("resources/iniReaderTest.ini");
	}
	
	@Test
	public void iniReaderConstructor()
	{
		assertThat(ini, instanceOf(IniReader.class));
	}
	
	@Test
	public void iniReaderGetMutexPaths()
	{
		Set<String> paths = ini.getPaths("MutexPaths");
		
		assertEquals(paths.size(), 1);
		assertTrue(paths.contains("include/linux/mutex.h"));
	}
	
	@Test
	public void iniReaderGetSpinPaths()
	{
		Set<String> paths = ini.getPaths("SpinPaths");
		
		assertEquals(paths.size(), 2);
		assertTrue(paths.contains("include/linux/spinlock.h"));
		assertTrue(paths.contains("include/linux/spinlock_api_smp.h"));
	}
	
	@Test
	public void iniReaderGetNullPaths()
	{
		Set<String> paths = ini.getPaths("SomeUndefinedTag");
		
		assertEquals(paths, null);
	}
	
	@Test
	public void iniReaderGetMalformattedPaths()
	{
		Set<String> paths = ini.getPaths("MutexFunctionCriteria");
		
		assertFalse(paths == null);
		assertEquals(paths.size(), 3);
		
		assertTrue(paths.contains("mutex=true"));
		assertTrue(paths.contains("lock=true"));
		assertTrue(paths.contains("mutex_lock_io_nested=false"));
	}
	
	@Test
	public void iniReaderGetMalformattedCriteria()
	{
		Set<Criteria> criteria = ini.getCriteria("MutexPaths");
		
		assertFalse(criteria == null);
		assertEquals(criteria.size(), 0);
	}
	
	@Test
	public void iniReaderGetNullCriteria()
	{
		Set<Criteria> criteria = ini.getCriteria("SomeUndefinedTag");
		
		assertEquals(criteria, null);
	}
	
	@Test
	public void iniReaderGetMutexFunctionCriteria()
	{
		Set<Criteria> criteria = ini.getCriteria("MutexFunctionCriteria");
		
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
		Set<Criteria> criteria = ini.getCriteria("MutexMacroCriteria");
		
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
		Set<Criteria> criteria = ini.getCriteria("SpinFunctionCriteria");
		
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
		Set<Criteria> criteria = ini.getCriteria("SpinMacroCriteria");
		
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
