package edu.iastate.sdmay1809;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class CCodeParser {
	private Set<String> poundDefines;
	private Set<String> poundIncludes;
	
	public CCodeParser(String path) throws IOException
	{
		poundDefines = new HashSet<String>();
		poundIncludes = new HashSet<String>();
		
		List<String> lines = Files.readAllLines(Paths.get(path), Charset.forName("UTF-8"));
		
		for (Iterator<String> iter = lines.listIterator(); iter.hasNext();)
		{			
			String line = iter.next();
			
			if (line.toLowerCase().matches("#\\s*define.*"))
			{
				String macro = line;
				
				iter.remove();
				
				if (macro.matches(".*\\\\\\s*"))
				{
					macro = macro.replace("\\", "").replaceFirst("\\s++$", "");
					String nextLine;
					
					do
					{
						nextLine = iter.next();
						iter.remove();
						
						macro += "\n" + nextLine.replace("\\", "").replaceFirst("\\s++$", "");
					} while(nextLine.matches(".*\\\\\\s*"));
				}
				poundDefines.add(macro);
			}
			
			else if (line.toLowerCase().matches("#\\s*include.*"))
			{
				poundIncludes.add(line);
				iter.remove();
			}
			
			else if (line.matches("\\s*\\/\\*.*"))
			{
				iter.remove();
				String nextLine = iter.next();
				
				while(!nextLine.matches(".*\\*\\/.*"))
				{
					iter.remove();
					nextLine = iter.next();
				}
				
				if (!nextLine.matches(".*\\*\\/\\w+")) iter.remove();
				
			}
		}
		
		for (String s : lines)
		{
			System.out.println(s);
		}
	}
}
