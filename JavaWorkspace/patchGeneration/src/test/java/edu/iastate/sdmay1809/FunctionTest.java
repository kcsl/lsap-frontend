package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

public class FunctionTest {
	Function function;
	Function otherFunction;
	String exampleFunction;
	String otherExampleFunction;
	
	@Before
	public void setUp() throws Exception
	{
		function = new Function("int aFunction(int parameter1, struct mutex *lock) {");
		otherFunction = new Function("extern __must_check void function2(struct mutex *lock) {");
	}
	
	@Test
	public void functionConstructor()
	{
		assertThat(function, instanceOf(Function.class));
		assertThat(otherFunction, instanceOf(Function.class));
		
		try
		{
			Function f = new Function("something that's not a function");
			fail("No exception thrown on malformatted function: " + f.getName());
		}
		
		catch(Exception e)
		{
			
		}
	}
	
	@Test
	public void functionReturnType()
	{
		assertEquals(function.getType(), "int");
		assertEquals(otherFunction.getType(), "void");
	}
	
	@Test
	public void functionName()
	{
		assertEquals(function.getName(), "aFunction");
		assertEquals(otherFunction.getName(), "function2");
	}
	
	@Test
	public void functionParameters()
	{
		assertEquals(function.getParameter(-1), null);
		assertEquals(function.getParameter(2), null);
		
		assertEquals(function.getParameter(0).getType(), "int");
		assertEquals(function.getParameter(0).getName(), "parameter1");
		
		assertEquals(function.getParameter(1).getType(), "struct mutex *");
		assertEquals(function.getParameter(1).getName(), "lock");
		
		ArrayList<Parameter> parameters = function.getParameters();
		
		assertEquals(parameters.get(0).getType(), "int");
		assertEquals(parameters.get(0).getName(), "parameter1");
		
		assertEquals(parameters.get(1).getType(), "struct mutex *");
		assertEquals(parameters.get(1).getName(), "lock");
	}
	
	@Test
	public void functionContains() throws Exception
	{
		HashSet<Function> functions = new HashSet<Function>();
		
		functions.add(function);
		functions.add(otherFunction);
		functions.add(new Function("int anotherFunction() {"));
		
		assertTrue(Function.contains(functions, "aFunction"));
		assertTrue(Function.contains(functions, "function2"));
		assertTrue(Function.contains(functions, "anotherFunction"));

		assertFalse(Function.contains(functions, "bFunction"));
		assertFalse(Function.contains(functions, "function3"));
		assertFalse(Function.contains(functions, "yetAnotherFunction"));
	}
	
	@Test
	public void functionCompateTo()
	{
		assertTrue(function.compareTo(otherFunction) < 0);
		assertTrue(otherFunction.compareTo(function) > 0);
	}
	
	@Test
	public void functionConvertToStaticInline() throws Exception
	{
		assertEquals(function.convertToStaticInline(), "static inline int aFunction(int parameter1, struct mutex *lock){return 0;}");
		assertEquals(otherFunction.convertToStaticInline(), "static inline void function2(struct mutex *lock){}");
		assertEquals((new Function("static inline struct mutex *func3() {return NULL;}")).convertToStaticInline(), "static inline struct mutex * func3(){return NULL;}");
	}
	
	@Test
	public void functionEquals() throws Exception
	{
		assertTrue(function.equals(function));
		assertTrue(function.equals(new Function("int aFunction(int parameter1, struct mutex *lock) {")));
		assertFalse(function.equals(new Criteria("something", true)));
	}
	
	@Test
	public void functionGetFunctionName()
	{
		assertTrue(Function.getFunctionName(" extern int func(int someParam)").equals("func"));
		assertTrue(Function.getFunctionName("extern void func(double someParam)").equals("func"));
		assertTrue(Function.getFunctionName(" extern int __must_check func(struct mutex *lock)").equals("func"));
		assertTrue(Function.getFunctionName("extern void __must_check func()").equals("func"));
		assertTrue(Function.getFunctionName("int func(int someParam)").equals("func"));
		assertTrue(Function.getFunctionName(" void func(double someParam)").equals("func"));
		assertTrue(Function.getFunctionName("int __must_check func(struct mutex *lock)").equals("func"));
		assertTrue(Function.getFunctionName(" void __must_check func()").equals("func"));
		
		assertEquals(Function.getFunctionName(" extern int func;"), null);
		assertEquals(Function.getFunctionName("extern void func;"), null);
		assertEquals(Function.getFunctionName(" extern int __must_check func;"), null);
		assertEquals(Function.getFunctionName("extern void __must_check func;"), null);
		assertEquals(Function.getFunctionName(" int func;"), null);
		assertEquals(Function.getFunctionName("void func;"), null);
		assertEquals(Function.getFunctionName(" int __must_check func;"), null);
		assertEquals(Function.getFunctionName("void __must_check func;"), null);
		assertEquals(Function.getFunctionName(" return var != NULL;"), null);
		assertEquals(Function.getFunctionName(null), null);
	}

}
