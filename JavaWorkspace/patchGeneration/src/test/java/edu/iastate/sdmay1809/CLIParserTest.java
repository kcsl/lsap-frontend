package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CLIParserTest {
	CLIParser parser;
	ByteArrayOutputStream outContent;
	
	@Before
	public void setUpStream()
	{
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}
	
	@After
	public void restoreStream()
	{
		System.setOut(System.out);
	}
	
	@Test
	public void cliParserConstructor() throws ParseException
	{
		String[] args = {""};
		parser = new CLIParser(args);
		assertThat(parser, instanceOf(CLIParser.class));
	}
	
	@Test
	public void cliParserVProvided() throws ParseException
	{
		String[] args = {"-v"};
		parser = new CLIParser(args);
		assertTrue(parser.vProvided());

		args[0] = "--verbose";
		parser = new CLIParser(args);
		assertTrue(parser.vProvided());
	}
	
	@Test
	public void cliParserDProvided() throws ParseException
	{
		String[] args = {"-d", "path"};
		parser = new CLIParser(args);
		assertTrue(parser.dProvided());

		args[0] = "--debug";
		parser = new CLIParser(args);
		assertTrue(parser.dProvided());
	}
	
	@Test
	public void cliParserPProvided() throws ParseException
	{
		String[] args = {"-kp", "path"};
		parser = new CLIParser(args);
		assertTrue(parser.kpProvided());

		args[0] = "--kernel-path";
		parser = new CLIParser(args);
		assertTrue(parser.kpProvided());
	}
	
	@Test
	public void cliParserAllArgsProvided() throws ParseException
	{
		String[] args = {"--verbose", "-kp", "path", "--debug", "path"};
		parser = new CLIParser(args);
		assertTrue(parser.vProvided());
		assertTrue(parser.kpProvided());
		assertTrue(parser.dProvided());
	}
	
	@Test
	public void cliParserGetKPVal() throws ParseException
	{
		String[] args = {"-kp", "/path/to/kernel"};
		parser = new CLIParser(args);
		assertTrue(parser.kpProvided());
		assertEquals(parser.getKPVal(), "/path/to/kernel");

		args[0] = "--kernel-path";
		parser = new CLIParser(args);
		assertTrue(parser.kpProvided());
		assertEquals(parser.getKPVal(), "/path/to/kernel");
	}
	
	@Test
	public void cliParserGetDVal() throws ParseException
	{
		String[] args = {"-d", "/path/to/debug.txt"};
		parser = new CLIParser(args);
		assertTrue(parser.dProvided());
		assertEquals(parser.getDVal(), "/path/to/debug.txt");

		args[0] = "--debug";
		parser = new CLIParser(args);
		assertTrue(parser.dProvided());
		assertEquals(parser.getDVal(), "/path/to/debug.txt");
	}
	
	@Test
	public void cliParserDefaultVals() throws ParseException
	{
		String[] args = {"-d", "-kp"};
		parser = new CLIParser(args);
		assertTrue(parser.dProvided());
		assertTrue(parser.kpProvided());
		assertEquals(parser.getDVal(), "resources/PatcherDebug.txt");
		assertEquals(parser.getKPVal(), "");
	}	
	
	@Test
	public void cliParserHelpMenu() throws ParseException
	{		
		String[] args = {"-h"};
		parser = new CLIParser(args);
		assertTrue(outContent.toString().length() > 0);
		
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		assertEquals(outContent.toString().length(), 0);
		
		args[0] = "--help";
		parser = new CLIParser(args);
		assertTrue(outContent.toString().length() > 0);
	}
}
