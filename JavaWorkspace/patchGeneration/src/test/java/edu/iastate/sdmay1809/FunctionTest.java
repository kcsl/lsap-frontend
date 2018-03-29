package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class FunctionTest {
	Function function;
	Function otherFunction;
	String exampleFunction;
	String otherExampleFunction;
	
	@Before
	public void setUp()
	{
		exampleFunction = "int aFunction(int parameter1, struct mutex *lock) {";
		otherExampleFunction = "void function2(struct mutex *lock) {";
		function = new Function(exampleFunction);
		otherFunction = new Function(otherExampleFunction);
	}
	
	@Test
	public void functionConstructor()
	{
		assertThat(function, instanceOf(Function.class));
		assertThat(otherFunction, instanceOf(Function.class));
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
	public void functionContains()
	{
		ArrayList<Function> functions = new ArrayList<Function>();
		
		functions.add(new Function(exampleFunction));
		functions.add(new Function(otherExampleFunction));
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
	public void functionConvertToStaticInline()
	{
		assertEquals(function.convertToStaticInline(), "static inline int aFunction(int parameter1, struct mutex *lock){return 0;}");
		assertEquals(otherFunction.convertToStaticInline(), "static inline void function2(struct mutex *lock){}");
	}
}
