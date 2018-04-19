package edu.iastate.sdmay1809;

import static org.junit.Assert.*;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.rules.TemporaryFolder;

import edu.iastate.sdmay1809.shared.Utils;
import edu.iastate.sdmay1809.shared.InstanceTracker.InstanceParserManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

public class DataBaseFileTranslatorTest {
	String pathToSource = "resources/translatorTestFiles/kernel/results";
	
	@Rule
	public TemporaryFolder folder= new TemporaryFolder();
	
	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Before
	public void beforeTest() {
		InstanceParserManager.clear();
//		exit.expectSystemExit();
	}
	
	@Test 
	public void testFileOverride() throws IOException, InterruptedException {
		File workspace = folder.newFolder();
		File setupWorkspace = Paths.get("resources", "translatorTestFiles", "kernel", "results").toAbsolutePath().toFile();
		Utils.execute(new String[] { "cp", "-R", ".", workspace.getAbsolutePath() }, setupWorkspace);
		
		DataBaseFileTranslator dbt = new DataBaseFileTranslator(workspace.getAbsolutePath());
		boolean test0 = dbt.run(workspace, false);
		boolean test = dbt.run(workspace, false);
		assertTrue(test0);
		assertFalse(test);
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
		
		boolean foundMpg = false;
		
		for(File f : spin.listFiles()) {
			assertEquals(f.list().length, 7);
			for(File img : f.listFiles())
				if(img.getName().equals("mpg.png"))
					foundMpg = true;
		}
		
		assertTrue(foundMpg);
		foundMpg = false;
		
		for(File f : mutex.listFiles()) {
			assertEquals(f.list().length, 7);
			for(File img : f.listFiles())
				if(img.getName().equals("mpg.png"))
					foundMpg = true;
		}
		assertTrue(foundMpg);
	}
	
//	@Test
//	public void testDataCreation() throws IOException {
//		folder.create();
//		File savedFile = new File(folder.getRoot().getAbsolutePath() + "/db_132rc2.json");
//		DataBaseFileTranslator dbt = new DataBaseFileTranslator(pathToSource.toString());
//		assertTrue(dbt.run(folder.getRoot(), true));
//		
//		assertTrue(savedFile.exists());
//		String fileData = "";
//		
//		try {
//			fileData = readFile(savedFile.getAbsolutePath(), Charset.defaultCharset());
//		}
//		catch(Exception e) {
//			System.err.println("Exception reading file!: " + e.toString());
//			fail();
//		}
//		
//		JSONObject testObj = new JSONObject(fileData);
//		
//		System.out.println(fileData);
//		
//		JSONObject head = testObj.getJSONObject("132rc2");
//		
//		assertNotNull(head);
//		assertTrue(testObj.getJSONObject("132rc2").length() == 4);
//		
//		for(String k: head.keySet()) {
//			JSONObject instance = head.getJSONObject(k);
//			assertTrue(instance.has("filename"));
//			assertTrue(instance.has("driver"));
//			assertTrue(instance.has("offset"));
//			assertTrue(instance.has("mpg"));
//			assertTrue(instance.has("cfg"));
//			assertTrue(instance.has("pcg"));
//			assertTrue(instance.has("length"));
//			assertTrue(instance.has("id"));
//			assertTrue(instance.has("title"));
//			assertTrue(instance.has("status"));
//			
//			assertTrue(instance.getString("id").equals(k));
//			
//			JSONObject cfgs = instance.getJSONObject("cfg");
//			
//			assertNotNull(cfgs);
//			
//			assertTrue(cfgs.length() == 3);
//			
//			for(String c: cfgs.keySet()) {
//				String dir = cfgs.getString(c);
//				assertNotNull(dir);
//				assertTrue(dir.endsWith(c+".png"));
//				
//			}
//			
//			JSONObject pcgs = instance.getJSONObject("pcg");
//			
//			assertNotNull(pcgs);
//			
//			assertTrue(pcgs.length() == 3);
//			
//			for(String c: cfgs.keySet()) {
//				String dir = cfgs.getString(c);
//				assertNotNull(dir);
//				assertTrue(dir.endsWith(c+".png"));
//				
//			}
//			
//		}
//		
//	}
	
//	private String readFile(String path, Charset encoding) throws IOException {
//		byte[] encoded = Files.readAllBytes(Paths.get(path));
//		return new String(encoded, encoding);
//	}

}
