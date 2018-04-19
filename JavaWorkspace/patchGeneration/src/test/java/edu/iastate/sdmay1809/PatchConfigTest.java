package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.util.Iterator;
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
		config = new PatchConfig("resources/testing/patchConfigTest.json");
	}
		
	@Test
	public void patchConfigNoFile() throws Exception
	{
		try
		{
			config = new PatchConfig("fileThatDoesntExist.goodLuckFindingThis");
			config = new PatchConfig("resources/kernel/include/linux/mutex.h");
		}
		
		catch (Exception e) 
		{
			fail("Exception thrown on improper/no file found.");
		}
		
		config = new PatchConfig("resources/testing/patchConfigTest.json");
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
		
		assertEquals(criteria.size(), 3);
		assertTrue(criteria.keySet().contains("ignore_all_locks"));
		assertFalse(criteria.get("ignore_all_locks"));
		assertTrue(criteria.keySet().contains("mutex"));
		assertTrue(criteria.get("mutex"));
		assertTrue(criteria.keySet().contains("lock"));
		assertTrue(criteria.get("lock"));
		
		criteria = config.getCriteria(PatchConfig.MUTEX_MACRO_CRITERIA);
		
		assertEquals(criteria.size(), 2);
		assertTrue(criteria.keySet().contains("ignore_all_locks"));
		assertFalse(criteria.get("ignore_all_locks"));
		assertTrue(criteria.keySet().contains("mutex"));
		assertTrue(criteria.get("mutex"));

		criteria = config.getCriteria(PatchConfig.SPIN_FUNCTION_CRITERIA);
		
		assertEquals(criteria.size(), 1);
		assertTrue(criteria.keySet().contains("ignore_all_locks"));
		assertTrue(criteria.get("ignore_all_locks"));
		
		criteria = config.getCriteria(PatchConfig.SPIN_MACRO_CRITERIA);
		
		assertEquals(criteria.size(), 2);
		assertTrue(criteria.keySet().contains("ignore_all_locks"));
		assertFalse(criteria.get("ignore_all_locks"));
		assertTrue(criteria.keySet().contains("lock"));
		assertTrue(criteria.get("lock"));
	}
	
	@Test
	public void patchConfigGetPaths() throws Exception
	{
		Set<String> paths = config.getPaths(PatchConfig.MUTEX_PATHS_TO_READ);
		
		assertEquals(paths.size(), 1);
		assertTrue(paths.contains("include/linux/mutex.h"));
		
		paths = config.getPaths(PatchConfig.MUTEX_PATHS_TO_CHANGE);
		
		assertEquals(paths.size(), 1);
		assertTrue(paths.contains("include/linux/mutex.h"));
		
		paths = config.getPaths(PatchConfig.SPIN_PATHS_TO_READ);
		
		assertEquals(paths.size(), 1);
		assertTrue(paths.contains("include/linux/spinlock.h"));
		
		paths = config.getPaths(PatchConfig.SPIN_PATHS_TO_CHANGE);
		
		assertEquals(paths.size(), 1);
		assertTrue(paths.contains("include/linux/spinlock.h"));
	}
	
	@Test
	public void patchConfigGetFunctionReturnTypes() throws Exception
	{
		Set<String> types = config.getFunctionReturnTypes();
		
		assertEquals(types.size(), 3);
		assertTrue(types.contains("int"));
		assertTrue(types.contains("void"));
		assertTrue(types.contains("struct patchTest *"));
	}
	
	@Test
	public void patchConfigGetFunctions() throws Exception
	{
		Set<Function> functions = config.getFunctions(PatchConfig.MUTEX_FUNCTIONS_TO_INCLUDE);
		
		assertEquals(functions.size(), 1);
		assertEquals(functions.iterator().next().getName(), "function");
		
		functions = config.getFunctions(PatchConfig.SPIN_FUNCTIONS_TO_INCLUDE);
		
		Iterator<Function> iterator = functions.iterator();
		Function f = iterator.next();
		
		assertEquals(functions.size(), 2);
		assertTrue(f.getName().equals("__raw_spin_lock") || f.getName().equals("__raw_spin_trylock"));
		
		f = iterator.next();
		
		assertTrue(f.getName().equals("__raw_spin_lock") || f.getName().equals("__raw_spin_trylock"));
	}
	
	@Test
	public void patchConfigGetMacros() throws Exception
	{
		Set<Macro> macros = config.getMacros(PatchConfig.MUTEX_MACROS_TO_INCLUDE);
		
		assertEquals(macros.size(), 1);
		assertEquals(macros.iterator().next().getName(), "macro");
		
		macros = config.getMacros(PatchConfig.SPIN_MACROS_TO_INCLUDE);
		
		assertEquals(macros.size(), 1);
		assertEquals(macros.iterator().next().getName(), "macro");
	}
}
