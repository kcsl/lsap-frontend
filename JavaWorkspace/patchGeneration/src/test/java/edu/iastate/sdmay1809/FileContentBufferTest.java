package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class FileContentBufferTest {
	FileContentBuffer fcb;
	String path;
	
	@Before
	public void setUp()
	{
		path = "resources/FileContentBufferTest.txt";
		fcb = new FileContentBuffer(path);
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

		for (String line : lines)
		{
			fcb.writeln(line);
		}
		
		fcb.print();
		
		ArrayList<String> content = (ArrayList<String>) Files.readAllLines(Paths.get(path), Charset.forName("UTF-8"));

		for (int i = 0; i < content.size(); i++)
		{
			try
			{
				assertEquals(content.get(i), lines.get(i));
			}
			
			catch(Exception e)
			{
				fail("More content was written than was supplied!");
			}
			
			if (i == content.size() - 1 && i < lines.size() - 1)
			{
				fail("Not all of the content was written!");
			}
		}
	}
}
