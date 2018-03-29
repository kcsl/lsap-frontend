package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ParameterTest {
	Parameter p1;
	Parameter p2;
	Parameter p3;
	
	@Before
	public void setUp()
	{
		p1 = new Parameter("int anInteger");
		p2 = new Parameter("struct mutex *lock");
		p3 = new Parameter("void *aVoidPointer");
	}
	
	@Test
	public void parameterConstructor()
	{
		assertThat(p1, instanceOf(Parameter.class));
		assertThat(p2, instanceOf(Parameter.class));
		assertThat(p3, instanceOf(Parameter.class));
	}
	
	@Test
	public void parameterGetType()
	{
		assertEquals(p1.getType(), "int");
		assertEquals(p2.getType(), "struct mutex *");
		assertEquals(p3.getType(), "void *");
	}
	
	@Test
	public void parameterGetName()
	{
		assertEquals(p1.getName(), "anInteger");
		assertEquals(p2.getName(), "lock");
		assertEquals(p3.getName(), "aVoidPointer");
	}
}
