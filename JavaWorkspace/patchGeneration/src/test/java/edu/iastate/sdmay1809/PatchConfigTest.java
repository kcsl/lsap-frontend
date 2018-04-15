package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class PatchConfigTest
{
	PatchConfig config;
	
	@Before
	public void setUp() throws Exception
	{
		config = new PatchConfig("resources/patcherConfig.json");
	}
		
	@Test
	public void patchConfigConstructor() throws Exception
	{
		assertThat(config, instanceOf(PatchConfig.class));
	}
	
	@Test
	public void patchConfigGetCriteria() throws Exception
	{
		Map<String, Boolean> criteria = config.getCriteria(PatchConfig.MUTEX_FUNCTION_CRITERIA);
		
		assertEquals(criteria.size(), 4);
		assertTrue(criteria.keySet().contains("mutex"));
		assertTrue(criteria.get("mutex"));
		assertTrue(criteria.keySet().contains("lock"));
		assertTrue(criteria.get("lock"));
		assertTrue(criteria.keySet().contains("mutex_lock_io_nested"));
		assertFalse(criteria.get("mutex_lock_io_nested"));
		assertTrue(criteria.keySet().contains("_is_"));
		assertFalse(criteria.get("_is_"));
		
		criteria = config.getCriteria(PatchConfig.MUTEX_MACRO_CRITERIA);
		
		assertEquals(criteria.size(), 3);
		assertTrue(criteria.keySet().contains("mutex"));
		assertTrue(criteria.get("mutex"));
		assertTrue(criteria.keySet().contains("lock"));
		assertTrue(criteria.get("lock"));
		assertTrue(criteria.keySet().contains("nested"));
		assertFalse(criteria.get("nested"));
	}
	
	@Test
	public void patchConfigGetPaths() throws Exception
	{
		Set<String> paths = config.getPaths(PatchConfig.MUTEX_PATHS_TO_READ);
		
		assertEquals(paths.size(), 1);
		assertTrue(paths.contains("include/linux/mutex.h"));
		
		paths = config.getPaths(PatchConfig.SPIN_PATHS_TO_READ);
		
		assertEquals(paths.size(), 2);
		assertTrue(paths.contains("include/linux/spinlock.h"));
		assertTrue(paths.contains("include/linux/spinlock_api_smp.h"));
	}
}
