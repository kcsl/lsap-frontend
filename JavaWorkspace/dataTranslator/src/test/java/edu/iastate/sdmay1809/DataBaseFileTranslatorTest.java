package edu.iastate.sdmay1809;

import static org.junit.Assert.*;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.rules.TemporaryFolder;
import org.junit.runners.MethodSorters;
import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataBaseFileTranslatorTest {
	static String pathToSource = "resources/translatorTestFiles/kernel/results";
	
	@Rule
	public TemporaryFolder folder= new TemporaryFolder();
	

	
	@Test 
	public void testFileOverride() throws IOException {
		folder.create();
		File savedFile = new File(folder.getRoot().getAbsolutePath() + "/db_132rc2.json");
		DataBaseFileTranslator dbt = new DataBaseFileTranslator(pathToSource.toString());
		dbt.run(savedFile.getParentFile(), false);
		assertFalse(dbt.run(savedFile.getParentFile(), false));
	}
	
	@Test
	public void testAssetFolderCreation() throws IOException {
		folder.create();
		DataBaseFileTranslator dbt = new DataBaseFileTranslator(pathToSource.toString());
		assertTrue(dbt.run(folder.getRoot(), true));
		File root = new File(folder.getRoot().getAbsoluteFile() + "/132rc2");
		assertTrue(root.exists());
		File spin = new File(root.getAbsolutePath() + "/spin");
		File mutex = new File(root.getAbsolutePath() + "/mutex");
		assertTrue(spin.exists());
		assertTrue(mutex.exists());
		assertEquals(spin.list().length, 2);
		assertEquals(mutex.list().length, 2);
		
		for(File f : spin.listFiles()) {
			assertEquals(f.list().length, 6);
		}
		
		for(File f : mutex.listFiles()) {
			assertEquals(f.list().length, 6);
		}
	}
	
	@Test
	public void testDataCreation() throws IOException {
		folder.create();
		File savedFile = new File(folder.getRoot().getAbsolutePath() + "/db_132rc2.json");
		DataBaseFileTranslator dbt = new DataBaseFileTranslator(pathToSource.toString());
		assertTrue(dbt.run(folder.getRoot(), true));
		
		assertTrue(savedFile.exists());
		String fileData = "";
		
		try {
			fileData = readFile(savedFile.getAbsolutePath(), Charset.defaultCharset());
		}
		catch(Exception e) {
			System.err.println("Exception reading file!: " + e.toString());
			fail();
		}
		
		JSONObject testObj = new JSONObject(fileData);
		
		System.out.println(fileData);
		
		JSONObject head = testObj.getJSONObject("132rc2");
		
		assertNotNull(head);
		assertTrue(testObj.getJSONObject("132rc2").length() == 4);
		
		for(String k: head.keySet()) {
			JSONObject instance = head.getJSONObject(k);
			assertTrue(instance.has("filename"));
			assertTrue(instance.has("driver"));
			assertTrue(instance.has("offset"));
			assertTrue(instance.has("mpg"));
			assertTrue(instance.has("cfg"));
			assertTrue(instance.has("pcg"));
			assertTrue(instance.has("length"));
			assertTrue(instance.has("id"));
			assertTrue(instance.has("title"));
			assertTrue(instance.has("status"));
			
			assertTrue(instance.getString("id").equals(k));
			
			JSONObject cfgs = instance.getJSONObject("cfg");
			
			assertNotNull(cfgs);
			
			assertTrue(cfgs.length() == 3);
			
			for(String c: cfgs.keySet()) {
				String dir = cfgs.getString(c);
				assertNotNull(dir);
				assertTrue(dir.endsWith(c+".png"));
				
			}
			
			JSONObject pcgs = instance.getJSONObject("pcg");
			
			assertNotNull(pcgs);
			
			assertTrue(pcgs.length() == 3);
			
			for(String c: cfgs.keySet()) {
				String dir = cfgs.getString(c);
				assertNotNull(dir);
				assertTrue(dir.endsWith(c+".png"));
				
			}
			
		}
		
	}
	
	private String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}
