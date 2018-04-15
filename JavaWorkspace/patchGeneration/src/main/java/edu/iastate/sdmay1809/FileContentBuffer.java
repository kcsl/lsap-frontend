package edu.iastate.sdmay1809;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class is designed to essentially write to a file. Rather than writing as we process input,
 * write all of the file contents to this object, then write this objects contents to the file.
 * 
 * This should reduce the amount of time the file that's being written to stays open and reduce the
 * number of I/O operations on that file.
 */
public class FileContentBuffer 
{
	// The path to the file we'll ultimately write to
	private Path file;
	
	// The content we'll write to the file
	private ArrayList<String> lines;
	
	// Constructor. Accept a filename and initialize the list of lines
	public FileContentBuffer(String fileName)
	{
		this.file = Paths.get(fileName);
		this.lines = new ArrayList<String>();
	}
	
	// For a given input, convert it to a string and add it to the lines
	public <T> void writeln(T input)
	{
		if (input != null) lines.add(input.toString());
	}
	
	// Print the contents to the file, and optionally to the console as well
	public void print(boolean printToConsole) throws IOException
	{
		// If we're printing to the console, iterate through the lines and print them out
		if (printToConsole)
		{
			for (String line : lines)
			{
				System.out.println(line);
			}
		}
		
		// Overloaded method for printing. This function writes to a file
		print();
	}
	
	// Print the contents to the file
	public void print() throws IOException
	{
		Files.write(file, lines, Charset.forName("UTF-8"));		
	}
}
