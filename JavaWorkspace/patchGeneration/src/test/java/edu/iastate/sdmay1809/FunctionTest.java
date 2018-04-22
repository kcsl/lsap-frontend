package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FunctionTest {
	Function f1;
	Function f2;
	Function f3;
	
	@Before
	public void setUp() throws Exception
	{
		f1 = new Function("void __lockfunc\n" + 
				"_raw_spin_lock_nest_lock(raw_spinlock_t *lock, struct lockdep_map *map)\n" + 
				"								__acquires(lock);");
		f2 = new Function("void __lockfunc _raw_spin_lock_nest_lock(raw_spinlock_t *lock)\n" + 
				"								__acquires(lock);");
		f3 = new Function("void __lockfunc _raw_spin_lock_bh(raw_spinlock_t *lock)		__acquires(lock);");
		
		Function.config = new PatchConfig("resources/testing/patchConfigTest.json");
	}
	
	@Test
	public void functionImproperFormat() throws Exception
	{
		try
		{
			new Function(null);
			fail("Function should throw exception on null input.");
		} catch (Exception e) {}
		
		try
		{
			new Function("notafunction");
			fail("Function should throw exception on malformatted input.");
		} catch (Exception e) {}
	}
	
	@Test
	public void functionConstructor()
	{
		assertThat(f1, instanceOf(Function.class));
		assertThat(f2, instanceOf(Function.class));
		assertThat(f3, instanceOf(Function.class));
		
		assertTrue(Locker.class.isInstance(f1));
		assertTrue(Locker.class.isInstance(f2));
		assertTrue(Locker.class.isInstance(f3));
	}
	
	@Test
	public void functionGetName()
	{
		assertEquals(f1.getName(), "_raw_spin_lock_nest_lock");
		assertEquals(f2.getName(), "_raw_spin_lock_nest_lock");
		assertEquals(f3.getName(), "_raw_spin_lock_bh");
	}
	
	@Test
	public void functionGetParameter()
	{
		assertEquals(f1.getParameter(-1), null);
		assertEquals(f1.getParameter(0).getType(), "raw_spinlock_t*");
		assertEquals(f1.getParameter(0).getName(), "lock");
		assertEquals(f1.getParameter(1).getType(), "struct lockdep_map*");
		assertEquals(f1.getParameter(1).getName(), "map");
		assertEquals(f1.getParameter(2), null);

		assertEquals(f2.getParameter(-1), null);
		assertEquals(f2.getParameter(0).getType(), "raw_spinlock_t*");
		assertEquals(f2.getParameter(0).getName(), "lock");
		assertEquals(f2.getParameter(1), null);
		
		assertEquals(f3.getParameter(-1), null);
		assertEquals(f3.getParameter(0).getType(), "raw_spinlock_t*");
		assertEquals(f3.getParameter(0).getName(), "lock");
		assertEquals(f3.getParameter(1), null);
	}
	
	@Test
	public void functionGetParameters()
	{
		List<Parameter> params = f1.getParameters();

		assertEquals(params.size(), 2);
		assertEquals(params.get(0).getType(), "raw_spinlock_t*");
		assertEquals(params.get(0).getName(), "lock");
		assertEquals(params.get(1).getType(), "struct lockdep_map*");
		assertEquals(params.get(1).getName(), "map");
		
		params = f2.getParameters();
		
		assertEquals(params.size(), 1);
		assertEquals(params.get(0).getType(), "raw_spinlock_t*");
		assertEquals(params.get(0).getName(), "lock");
		
		params = f3.getParameters();

		assertEquals(params.size(), 1);
		assertEquals(params.get(0).getType(), "raw_spinlock_t*");
		assertEquals(params.get(0).getName(), "lock");
	}
	
	@Test
	public void functionGetModifiers()
	{
		assertEquals(f1.getModifiers(), "void __lockfunc");
		assertEquals(f2.getModifiers(), "void __lockfunc");
		assertEquals(f3.getModifiers(), "void __lockfunc");
	}
	
	@Test
	public void functionEquals() throws Exception
	{
		assertTrue(f1.equals(f1));
		assertFalse(f1.equals(f2));
		assertFalse(f1.equals(f3));
		assertFalse(f1.equals(null));
		assertTrue(f1.equals(new Macro("#define _raw_spin_lock_nest_lock(lock) func(lock)")));
		assertFalse(f1.equals(new Parameter("int param")));
		assertTrue(f1.equals(new Function("int __must_check\n" + 
				"_raw_spin_lock_nest_lock(raw_spinlock_t *lock, struct lockdep_map *map)\n" + 
				"								__acquires(lock);")));
		assertFalse(f1.equals(new Function("int __must_check\n" + 
				"_raw_spin_lock_nest_lock(int lock, struct lockdep_map *map)\n" + 
				"								__acquires(lock);")));
	}
	
	@Test
	public void functionHashCode() throws Exception
	{
		assertEquals(f1.hashCode(), f1.hashCode());
		assertEquals(f1.hashCode(), f2.hashCode());
		assertThat(f1.hashCode(), not(equals(f3.hashCode())));
		assertThat(f1.hashCode(), not(equals(null)));
		assertEquals(f1.hashCode(), (new Macro("#define _raw_spin_lock_nest_lock(lock) func(lock)")).hashCode());
	}
	
	@Test
	public void functionIsLockingFunction() throws Exception
	{
		HashMap<String, Boolean> criteria = new HashMap<String, Boolean>();
		
		criteria.put("spin", true);
		criteria.put("lock", true);
		criteria.put("no", false);
		
		assertTrue(Function.isLockingFunction(f1, criteria));
		assertTrue(Function.isLockingFunction(f2, criteria));
		assertTrue(Function.isLockingFunction(f3, criteria));
		assertFalse(Function.isLockingFunction(new Function("int noFunc(struct mutex *lock);"), criteria));
		assertFalse(Function.isLockingFunction(new Function("define int func(int lock);"), criteria));
		assertFalse(Function.isLockingFunction(new Function("return func(int lock);"), criteria));
	}
	
	@Test
	public void functionHasValidReturnType() throws Exception
	{
		assertTrue(f1.hasValidReturnType());
		assertTrue(f2.hasValidReturnType());
		assertTrue(f3.hasValidReturnType());
		assertFalse((new Function("return notfunc func(int param);")).hasValidReturnType());
	}
	
	@Test
	public void functionToString() throws Exception
	{
		assertEquals(f1.toString(), "static inline void _raw_spin_lock_nest_lock(raw_spinlock_t* lock, struct lockdep_map* map) {}");
		assertEquals(f2.toString(), "static inline void _raw_spin_lock_nest_lock(raw_spinlock_t* lock) {}");
		assertEquals(f3.toString(), "static inline void _raw_spin_lock_bh(raw_spinlock_t* lock) {}");
		assertEquals((new Function("int function();")).toString(), "static inline int function() {return 0;}");
		assertEquals((new Function("struct patchTest *function();")).toString(), "static inline struct patchTest * function() {return NULL;}");
		assertEquals((new Function("struct wrongType *function();")).toString(), "");
		
		PatchConfig tmp = Function.config;
		Function.config = null;
		
		assertEquals(f1.toString(), "");
		assertEquals(f2.toString(), "");
		assertEquals(f3.toString(), "");
		assertEquals((new Function("int function();")).toString().toString(), "");
		assertEquals((new Function("struct mutex *function();")).toString(), "");
		assertEquals((new Function("struct wrongType *function();")).toString(), "");
		
		Function.config = tmp;
	}
}
