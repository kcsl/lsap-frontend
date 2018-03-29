package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class MacroTest {
	Macro macro;
	Macro otherMacro;
	
	@Before
	public void setUp()
	{
		macro = new Macro("#define aMacro(lock, otherParam) body(lock)");
		otherMacro = new Macro("#define macro2(lock) otherBody(lock)");
	}
	
	@Test
	public void macroConstructor()
	{
		assertThat(macro, instanceOf(Macro.class));
		assertThat(otherMacro, instanceOf(Macro.class));
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
		assertEquals(otherMacro.getParameter(0), "lock");
		assertEquals(otherMacro.getParameter(1), null);
		
		ArrayList<String> macroParams = macro.getParameters();
		ArrayList<String> otherMacroParams = otherMacro.getParameters();
		
		assertEquals(macroParams.get(0), "lock");
		assertEquals(macroParams.get(1), "otherParam");
		
		assertEquals(otherMacroParams.get(0), "lock");
	}
	
	@Test
	public void macroPrintAsDefine()
	{
		macro.setMacroBody(new Function("int func(struct mutex *lock, int otherParam) {"));
		otherMacro.setMacroBody(new Function("int func(struct mutex *lock) {"));
		
		assertEquals(macro.printAsDefine(), "#define aMacro(lock, otherParam) func(lock, otherParam)");
		assertEquals(otherMacro.printAsDefine(), "#define macro2(lock) func(lock)");
		
		macro.setMacroBody(null);
		otherMacro.setMacroBody(null);
		
		assertEquals(macro.printAsDefine(), null);
		assertEquals(otherMacro.printAsDefine(), null);
	}
	
	@Test
	public void macroContains()
	{
		ArrayList<Macro> macros = new ArrayList<Macro>();
		
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
}