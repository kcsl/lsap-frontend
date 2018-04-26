package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

public class ParameterTest {
	private Parameter p1;
	private Parameter p2;
	private Parameter p3;
	private Parameter p4;
	private Parameter p5;
	private Parameter p6;
	private Parameter p7;
	private Parameter p8;
	
	@Before
	public void setUp() throws Exception
	{
		p1 = new Parameter("void *name");
		p2 = new Parameter("void* name");
		p3 = new Parameter("void * name");
		p4 = new Parameter("struct mutex *name");
		p5 = new Parameter("struct mutex* name");
		p6 = new Parameter("struct mutex * name");
		p7 = new Parameter("int name");
		p8 = new Parameter("struct mutex otherName");
	}
	
	@Test
	public void parameterConstructor()
	{
		assertThat(p1, instanceOf(Parameter.class));
		assertThat(p2, instanceOf(Parameter.class));
		assertThat(p3, instanceOf(Parameter.class));
		assertThat(p4, instanceOf(Parameter.class));
		assertThat(p5, instanceOf(Parameter.class));
		assertThat(p6, instanceOf(Parameter.class));
		assertThat(p7, instanceOf(Parameter.class));
		assertThat(p8, instanceOf(Parameter.class));
	}
	
	@Test
	public void parameterGetName()
	{
		assertEquals(p1.getName(), "name");
		assertEquals(p2.getName(), "name");
		assertEquals(p3.getName(), "name");
		assertEquals(p4.getName(), "name");
		assertEquals(p5.getName(), "name");
		assertEquals(p6.getName(), "name");
		assertEquals(p7.getName(), "name");
		assertEquals(p8.getName(), "otherName");
	}
	
	@Test
	public void parameterGetType()
	{
		assertEquals(p1.getType(), "void*");
		assertEquals(p2.getType(), "void*");
		assertEquals(p3.getType(), "void*");
		assertEquals(p4.getType(), "struct mutex*");
		assertEquals(p5.getType(), "struct mutex*");
		assertEquals(p6.getType(), "struct mutex*");
		assertEquals(p7.getType(), "int");
		assertEquals(p8.getType(), "struct mutex");
	}
	
	@Test
	public void parameterImproperConstructor()
	{
		String[] thingsToTry = { "notParam", "struct mutex *", null };
		
		for (String thing : thingsToTry)
		{
			try
			{
				new Parameter(thing);
				fail("No exception was thrown on improper input \"" + thing + "\"!");
			}
			
			catch (Exception e)
			{
				continue;
			}
		}
	}
}
