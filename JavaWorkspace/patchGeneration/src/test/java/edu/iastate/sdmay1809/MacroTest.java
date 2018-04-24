package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MacroTest {
	Macro m1;
	Macro m2;
	Macro m3;
	PatchConfig config;
	
	@Before
	public void setUp() throws Exception
	{
		config = new PatchConfig("resources/testing/patchConfigTest.json");
		m1 = new Macro("# define macro( lock, otherParam ) something(lock)");
		m2 = new Macro("#define macro (lock, otherParam) something");
		m3 = new Macro("#DEFINE macro ( lockname ) \\\n" + 
				"		{ .owner = ATOMIC_LONG_INIT(0) \\\n" + 
				"		, .wait_lock = __SPIN_LOCK_UNLOCKED(lockname.wait_lock) \\\n" + 
				"		, .wait_list = LIST_HEAD_INIT(lockname.wait_list) \\\n" + 
				"		__DEBUG_MUTEX_INITIALIZER(lockname) \\\n" + 
				"		__DEP_MAP_MUTEX_INITIALIZER(lockname) }");
	}
	
	@Test
	public void macroConstructor()
	{
		assertThat(m1, instanceOf(Macro.class));
		assertThat(m2, instanceOf(Macro.class));
		assertThat(m3, instanceOf(Macro.class));
		
		assertTrue(Locker.class.isInstance(m1));
		assertTrue(Locker.class.isInstance(m2));
		assertTrue(Locker.class.isInstance(m3));
	}
	
	@Test
	public void macroGetName()
	{
		assertEquals(m1.getName(), "macro");
		assertEquals(m2.getName(), "macro");
		assertEquals(m3.getName(), "macro");
	}
	
	@Test
	public void macroGetParameter()
	{
		assertEquals(m1.getParameter(-1), null);
		assertEquals(m1.getParameter(0), "lock");
		assertEquals(m1.getParameter(1), "otherParam");
		assertEquals(m1.getParameter(2), null);

		assertEquals(m2.getParameter(-1), null);
		assertEquals(m2.getParameter(0), "lock");
		assertEquals(m2.getParameter(1), "otherParam");
		assertEquals(m2.getParameter(2), null);

		assertEquals(m3.getParameter(-1), null);
		assertEquals(m3.getParameter(0), "lockname");
		assertEquals(m3.getParameter(1), null);
	}
	
	@Test
	public void macroGetParameters()
	{
		List<String> params = m1.getParameters();
		
		assertEquals(params.size(), 2);
		assertEquals(params.get(0), "lock");
		assertEquals(params.get(1), "otherParam");
		
		params = m2.getParameters();
		
		assertEquals(params.size(), 2);
		assertEquals(params.get(0), "lock");
		assertEquals(params.get(1), "otherParam");
		
		params = m3.getParameters();
		
		assertEquals(params.size(), 1);
		assertEquals(params.get(0), "lockname");
	}
	
	@Test
	public void macroEquals() throws Exception
	{
		assertTrue(m1.equals(m1));
		assertTrue(m1.equals(m2));
		assertFalse(m1.equals(new Macro("#define otherMacro(someParam) doThing(someParam)")));
		assertFalse(m1.equals(null));
		assertTrue(m1.equals(new Function(config, "int macro(int param);")));
		assertFalse(m1.equals(new Parameter("int param")));
		assertFalse(m1.equals(new Macro("#define macro(lock, notOtherParam)")));
	}
	
	@Test
	public void macroHashCode() throws Exception
	{
		assertEquals(m1.hashCode(), m1.hashCode());
		assertEquals(m1.hashCode(), m2.hashCode());
		assertThat(m1.hashCode(), not(equals((new Macro("#define otherMacro(someParam) doThing(someParam)")).hashCode())));	
		assertThat(m1.hashCode(), not(equals(null)));
		assertEquals(m1.hashCode(), (new Function(config, "int macro(int param);")).hashCode());
	}
	
	@Test
	public void macroIsLockingMacro() throws Exception
	{
		HashMap<String, Boolean> criteria = new HashMap<String, Boolean>();
		
		criteria.put("mac", true);
		criteria.put("ro", true);
		criteria.put("no", false);
		
		assertTrue(Macro.isLockingMacro(m1, criteria));
		assertTrue(Macro.isLockingMacro(m2, criteria));
		assertTrue(Macro.isLockingMacro(m3, criteria));
		assertFalse(Macro.isLockingMacro(new Macro("#define noMacro(someParam) doThing(someParam)"), criteria));
	}
	
	@Test
	public void macroToString() throws Exception
	{
		assertEquals(m1.toString(), "#define macro(lock, otherParam)");
		assertEquals(m2.toString(), "#define macro(lock, otherParam)");
		assertEquals(m3.toString(), "#define macro(lockname)");
		
		Function f = new Function(config, "int function(int lock, struct mutex *lock2);");
		
		m1.setBodyFunction(f);
		m2.setBodyFunction(f);
		m3.setBodyFunction(f);
		
		assertEquals(m1.toString(), "#define macro(lock, otherParam) function(0, NULL)");
		assertEquals(m2.toString(), "#define macro(lock, otherParam) function(0, NULL)");
		assertEquals(m3.toString(), "#define macro(lockname) function(0, NULL)");
		assertEquals((new Macro("#define macro()", new Function(config, "int function(int lock, struct mutex *lock2);"))).toString(), "#define macro() function(0, NULL)");
	}
	
	@Test
	public void macroMalformattedConstructor() throws Exception
	{
		try
		{
			new Macro(null);
			fail("Macro should fail on null input");
		} catch (Exception e){}
		
		try
		{
			new Macro("Not a macro");
			fail("Macro should fail on non-macro input");
		} catch (Exception e){}

	}
}
