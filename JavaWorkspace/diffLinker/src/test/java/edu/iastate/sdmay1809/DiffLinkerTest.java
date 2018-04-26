package edu.iastate.sdmay1809;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import edu.iastate.sdmay1809.shared.DiffConfig;
import edu.iastate.sdmay1809.shared.Utils;

public class DiffLinkerTest {
	
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();
	
	@Test
	public void testInit() throws Exception {
		DiffConfig config = DiffConfig.builder(DiffConfig.Builder.class).build();
		DiffLinker dl = new DiffLinker(config, false, 3);
		assertTrue(dl != null);
	}
	
	@Test
	public void testSingleInstance() throws JSONException, IOException, InterruptedException {
		File workspace = testFolder.newFolder();
		File setupWorkspace = Paths.get("resources", "testing", "DiffLinker", "testSingleInstance").toAbsolutePath().toFile();
		Path diffInstances = Paths.get(workspace.getAbsolutePath(), "diffmap", "diffInstanceMap.json");
		Utils.execute(new String[] { "cp", "-R", ".", workspace.getAbsolutePath() }, setupWorkspace);
		
		DiffConfig config = DiffConfig.builder(DiffConfig.Builder.class)
				.setDiffTestDir(Paths.get(workspace.getAbsolutePath(), "diffmap/").toString())
				.setKernelDir(Paths.get(workspace.getAbsolutePath(), "kernel/").toString()).setOldTag("v3.19-rc1")
				.setNewTag("v4.13")
				.setNewResultDir(Paths.get(workspace.getAbsolutePath(), "diffmap", "413_results").toString()).build();
		
		DiffLinker dl = new DiffLinker(config, true, 10);
		
		int linked = dl.run("newInstanceMap.json");
		
		assertEquals(1, linked);
		
		try {
			String content = String.join("", Files.readAllLines(diffInstances));
			JSONArray instances = new JSONArray(content);
			assertEquals(1, instances.length());
			JSONObject instance = instances.getJSONObject(0);
			
			assertTrue(instance.has("old"));
			assertTrue(instance.has("new"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Couldn't get diffInstanceMap.json");
		} catch (JSONException e) {
			e.printStackTrace();
			fail("Couldn't Perform JSON Operation!");
		}
	}
	
	@Test
	public void testMultipleInstances() throws JSONException, IOException, InterruptedException {
		File workspace = testFolder.newFolder();
		File setupWorkspace = Paths.get("resources", "testing", "DiffLinker", "testMultipleInstances").toAbsolutePath().toFile();
		Path diffInstances = Paths.get(workspace.getAbsolutePath(), "diffmap", "diffInstanceMap.json");
		Utils.execute(new String[] { "cp", "-R", ".", workspace.getAbsolutePath() }, setupWorkspace);
		
		DiffConfig config = DiffConfig.builder(DiffConfig.Builder.class)
				.setDiffTestDir(Paths.get(workspace.getAbsolutePath(), "diffmap/").toString())
				.setKernelDir(Paths.get(workspace.getAbsolutePath(), "kernel/").toString()).setOldTag("v3.19-rc1")
				.setNewTag("v4.13")
				.setNewResultDir(Paths.get(workspace.getAbsolutePath(), "diffmap", "413_results").toString()).build();
		
		DiffLinker dl = new DiffLinker(config, true, 10);
		
		int linked = dl.run("newInstanceMap.json");
		
		assertEquals(10, linked);
		
		try {
			String content = String.join("", Files.readAllLines(diffInstances));
			JSONArray instances = new JSONArray(content);
			assertEquals(10, instances.length());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Couldn't get diffInstanceMap.json");
		} catch (JSONException e) {
			e.printStackTrace();
			fail("Couldn't Perform JSON Operation!");
		}
	}
	
	@Test
	public void testInterestingCases() throws JSONException, IOException, InterruptedException {
		File workspace = testFolder.newFolder();
		File setupWorkspace = Paths.get("resources", "testing", "DiffLinker", "testInterestingCases").toAbsolutePath().toFile();
		Path diffInstances = Paths.get(workspace.getAbsolutePath(), "diffmap", "diffInstanceMap.json");
		Path diffInteresting = Paths.get(workspace.getAbsolutePath(), "diffmap", "diffInteresting.json");
		Utils.execute(new String[] { "cp", "-R", ".", workspace.getAbsolutePath() }, setupWorkspace);
		
		DiffConfig config = DiffConfig.builder(DiffConfig.Builder.class)
				.setDiffTestDir(Paths.get(workspace.getAbsolutePath(), "diffmap/").toString())
				.setKernelDir(Paths.get(workspace.getAbsolutePath(), "kernel/").toString()).setOldTag("v3.19-rc1")
				.setNewTag("v4.13")
				.setNewResultDir(Paths.get(workspace.getAbsolutePath(), "diffmap", "413_results").toString()).build();
		
		DiffLinker dl = new DiffLinker(config, false, 10);
		
		int linked = dl.run("newInstanceMap.json");
		
		assertEquals(2, linked);
		
		try {
			String content = String.join("", Files.readAllLines(diffInstances));
			JSONArray instances = new JSONArray(content);
			assertEquals(3, instances.length());
			
			content = String.join("", Files.readAllLines(diffInteresting));
			instances = new JSONArray(content);
			assertEquals(1, instances.length());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Couldn't get diffInstanceMap.json");
		} catch (JSONException e) {
			e.printStackTrace();
			fail("Couldn't Perform JSON Operation!");
		}
	}
}
