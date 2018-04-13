package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;

import org.javatuples.Pair;
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
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void functionEquals() throws Exception
	{
		assertTrue(f1.equals(f1));
		assertTrue(f1.equals(f2));
		assertFalse(f1.equals(f3));
		assertFalse(f1.equals(null));
		assertTrue(f1.equals(new Macro("#define _raw_spin_lock_nest_lock(lock) func(lock)")));
	}
	
	@Test
	public void functionIsLockingFunction() throws Exception
	{
		HashSet<Pair<String, Boolean>> criteria = new HashSet<Pair<String, Boolean>>();
		
		criteria.add(new Pair<String, Boolean>("spin", true));
		criteria.add(new Pair<String, Boolean>("lock", true));
		criteria.add(new Pair<String, Boolean>("no", false));
		
		assertTrue(Function.isLockingFunction(f1, criteria));
		assertTrue(Function.isLockingFunction(f2, criteria));
		assertTrue(Function.isLockingFunction(f3, criteria));
		assertFalse(Function.isLockingFunction(new Function("int noFunc(struct mutex *lock);"), criteria));
	}
}
