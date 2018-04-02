package edu.iastate.sdmay1809;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CCodeParserTest {
	CCodeParser parser;
	
	@Before
	public void setUp() throws IOException
	{
		parser = new CCodeParser("include/linux/mutex.h");
	}
	
	@Test
	public void cCodeParserDefaultConstructor()
	{
		assertThat(parser, instanceOf(CCodeParser.class));
	}
}
