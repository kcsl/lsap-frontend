package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class PatchConfigTest
{
	@Before
	public void resetSingleton() throws Exception
	{
		Field instance = PatchConfig.class.getDeclaredField("instance");
		instance.setAccessible(true);
		instance.set(null, null);
		instance.setAccessible(false);
	}
	
	@Test
	public void patchConfigNoInit() throws Exception
	{
		resetSingleton();
		PatchConfig.setJSONPath(null);
		
		try
		{
			PatchConfig.getInstance();
			fail("PatchConfig.getInstance() should fail without a config file specified");
		} catch(Exception e) {}		
	}
	
	@Test
	public void patchConfigConstructor() throws Exception
	{
		resetSingleton();
		PatchConfig.setJSONPath("resources/patcherConfig.json");

		assertThat(PatchConfig.getInstance(), instanceOf(PatchConfig.class));
	}
	
	@Test
	public void patchConfigGetCriteria() throws Exception
	{
		resetSingleton();
		PatchConfig.setJSONPath("resources/patcherConfig.json");

		Map<String, Boolean> criteria = PatchConfig.getInstance().getCriteria(PatchConfig.MUTEX_FUNCTION_CRITERIA);
		
		assertEquals(criteria.size(), 4);
		assertTrue(criteria.keySet().contains("mutex"));
		assertTrue(criteria.get("mutex"));
		assertTrue(criteria.keySet().contains("lock"));
		assertTrue(criteria.get("lock"));
		assertTrue(criteria.keySet().contains("mutex_lock_io_nested"));
		assertFalse(criteria.get("mutex_lock_io_nested"));
		assertTrue(criteria.keySet().contains("_is_"));
		assertFalse(criteria.get("_is_"));
		
		criteria = PatchConfig.getInstance().getCriteria(PatchConfig.MUTEX_MACRO_CRITERIA);
		
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
		resetSingleton();
		PatchConfig.setJSONPath("resources/patcherConfig.json");

		Set<String> paths = PatchConfig.getInstance().getPaths(PatchConfig.MUTEX_PATHS_TO_READ);
		
		assertEquals(paths.size(), 1);
		assertTrue(paths.contains("include/linux/mutex.h"));
		
		paths = PatchConfig.getInstance().getPaths(PatchConfig.SPIN_PATHS_TO_READ);
		
		assertEquals(paths.size(), 2);
		assertTrue(paths.contains("include/linux/spinlock.h"));
		assertTrue(paths.contains("include/linux/spinlock_api_smp.h"));
	}
}
