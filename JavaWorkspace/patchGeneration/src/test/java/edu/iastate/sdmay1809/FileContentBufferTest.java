package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileContentBufferTest {
	ByteArrayOutputStream outContent;
	FileContentBuffer fcb;
	String path;
	
	@Before
	public void setUp()
	{
		path = "resources/testing/FileContentBufferTest.txt";
		fcb = new FileContentBuffer(path);
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}
	
	@Test
	public void fileContentBufferConstructor()
	{
		assertThat(fcb, instanceOf(FileContentBuffer.class));
	}
	
	@Test
	public void fileContentBufferPrint() throws IOException
	{
		ArrayList<String> lines = new ArrayList<String>();
		
		lines.add("test1");
		lines.add("test2");
		lines.add("test3");
		lines.add(null);

		for (String line : lines)
		{
			fcb.writeln(line);
		}
		
		fcb.print();
		
		ArrayList<String> content = (ArrayList<String>) Files.readAllLines(Paths.get(path), Charset.forName("UTF-8"));
		
		for (int i = 0; i < Math.max(content.size(), lines.size()); i++)
		{
			try
			{
				if (lines.get(i) != null) assertEquals(content.get(i), lines.get(i));
			}
			
			catch(Exception e)
			{
				fail("More content was written than was supplied!");
			}
			
			if (i == content.size() - 1 && i < lines.size() - 2)
			{
				fail("Not all of the content was written!");
			}
		}
		
		Files.delete(Paths.get(path));
		
		fcb.print(false);
		
		content = (ArrayList<String>) Files.readAllLines(Paths.get(path), Charset.forName("UTF-8"));
		
		for (int i = 0; i < Math.max(content.size(), lines.size()); i++)
		{
			try
			{
				if (lines.get(i) != null) assertEquals(content.get(i), lines.get(i));
			}
			
			catch(Exception e)
			{
				fail("More content was written than was supplied!");
			}
			
			if (i == content.size() - 1 && i < lines.size() - 2)
			{
				fail("Not all of the content was written!");
			}
		}
		
		Files.delete(Paths.get(path));

		fcb.print(true);
		
		content = (ArrayList<String>) Files.readAllLines(Paths.get(path), Charset.forName("UTF-8"));
		
		for (int i = 0; i < Math.max(content.size(), lines.size()); i++)
		{
			try
			{
				if (lines.get(i) != null) assertEquals(content.get(i), lines.get(i));
			}
			
			catch(Exception e)
			{
				fail("More content was written than was supplied!");
			}
			
			if (i == content.size() - 1 && i < lines.size() - 2)
			{
				fail("Not all of the content was written!");
			}
		}
		
		assertTrue(outContent.toString().length() > 0);

	}
	
	@After
	public void restoreStream()
	{
		System.setOut(System.out);
	}
}
