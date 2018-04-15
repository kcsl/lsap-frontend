package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MacroTest {
	Macro macro;
	Macro otherMacro;
	
	@Before
	public void setUp() throws Exception
	{
		macro = new Macro("#define aMacro(lock, otherParam) body(lock)");
		otherMacro = new Macro("#define macro2() otherBody(lock)");
	}
	
	@Test
	public void macroConstructor()
	{
		assertThat(macro, instanceOf(Macro.class));
		assertThat(otherMacro, instanceOf(Macro.class));
		
		try
		{
			Macro m = new Macro("something that's not a macro");
			fail("No exception thrown on malformatted macro: " + m.getName());
		}
		
		catch(Exception e)
		{
			
		}
	}
	
	@Test
	public void macroGetName()
	{
		assertEquals(macro.getName(), "aMacro");
		assertEquals(otherMacro.getName(), "macro2");
	}
	
	@Test
	public void macroGetParameter()
	{
		assertEquals(macro.getParameter(-1), null);
		assertEquals(macro.getParameter(0), "lock");
		assertEquals(macro.getParameter(1), "otherParam");
		assertEquals(macro.getParameter(2), null);
		
		assertEquals(otherMacro.getParameter(-1), null);
		assertEquals(otherMacro.getParameter(0), null);
		
		List<String> macroParams = macro.getParameters();
		List<String> otherMacroParams = otherMacro.getParameters();
		
		assertEquals(macroParams.get(0), "lock");
		assertEquals(macroParams.get(1), "otherParam");
		
		assertEquals(otherMacroParams.size(), 0);
	}
		
	@Test
	public void macroContains() throws Exception
	{
		HashSet<Macro> macros = new HashSet<Macro>();
		
		macros.add(macro);
		macros.add(otherMacro);
		macros.add(new Macro("#define anotherMacro(lock, param3) func(lock)"));
		
		assertTrue(Macro.contains(macros, "aMacro"));
		assertTrue(Macro.contains(macros, "macro2"));
		assertTrue(Macro.contains(macros, "anotherMacro"));
		
		assertFalse(Macro.contains(macros, "bMacro"));
		assertFalse(Macro.contains(macros, "macro3"));
		assertFalse(Macro.contains(macros, "yetAnotherMacro"));
	}
	
	@Test
	public void macroCompareTo()
	{
		assertTrue(macro.compareTo(otherMacro) < 0);
		assertTrue(otherMacro.compareTo(macro) > 0);
	}
	
	@Test
	public void macroEquals() throws Exception
	{
		assertTrue(macro.equals(macro));
		assertTrue(macro.equals(new Macro("#define aMacro(lock, otherParam) body(lock)")));
		assertFalse(macro.equals(new Criteria("something", false)));
	}
	
	@Test
	public void macroGetMacroName()
	{
		assertTrue(Macro.getMacroName("# define macro(lock)").equals("macro"));
		assertTrue(Macro.getMacroName("#  define macro (lock)").equals("macro"));
		assertEquals(Macro.getMacroName(" #define macro( lock )"), "macro");
		assertTrue(Macro.getMacroName("# define macro (lock)").equals("macro"));
		
		assertEquals(Macro.getMacroName("# define macro 1"), null);
		assertEquals(Macro.getMacroName("#  define macro lock"), null);
		assertEquals(Macro.getMacroName(" #define macro something_else"), null);
		assertEquals(Macro.getMacroName("# define macro"), null);
		assertEquals(Macro.getMacroName(null), null);
	}
	
	@Test
	public void macroPrintAsDefine() throws Exception
	{
		assertEquals(macro.printAsDefine(null), null);
		assertEquals(macro.printAsDefine(new Function("static inline void func(struct mutex *lock, int otherParam)")),
					 "#define aMacro(lock, otherParam) func(NULL, 0)");
		assertEquals(macro.printAsDefine(new Function("static inline void func(struct mutex *lock, int otherParam, struct mutex *otherLock)")),
				 "#define aMacro(lock, otherParam) func(NULL, 0, NULL)");
		assertEquals(macro.printAsDefine(new Function("static inline void func(int otherParam, struct mutex *lock)")),
				 "#define aMacro(lock, otherParam) func(0, NULL)");
		assertEquals(macro.printAsDefine(new Function("static inline void func()")),
				 "#define aMacro(lock, otherParam) func()");
	}
}