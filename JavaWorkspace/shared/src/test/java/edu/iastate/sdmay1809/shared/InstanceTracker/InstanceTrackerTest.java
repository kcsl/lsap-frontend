package edu.iastate.sdmay1809.shared.InstanceTracker;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class InstanceTrackerTest {
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	Path pathToTest = Paths.get("");

	@After
	public void afterTests() {
		Path pathToInstanceMap = Paths.get(pathToTest.toString(), "oldInstanceMap.json");
		File check = new File(pathToInstanceMap.toString());
		if (check.exists()) {
			check.delete();
		}
	}

	@Test
	public void testInit() {
		InstanceTracker it = new InstanceTracker("my_source_dir");
		assertNotNull(it);
		assertEquals("my_source_dir", it.sourceDirectory);
	}

	@Test
	public void testRunSingleInstance() {
		pathToTest = Paths.get(System.getProperty("user.dir"), "resources", "testing", "InstanceTracker",
				"singleInstance");
		Path pathToInstanceMap = Paths.get(pathToTest.toString(), "oldInstanceMap.json");
		String resultsDir = Paths.get(pathToTest.toString(), "results").toString();
		InstanceTracker it = new InstanceTracker(resultsDir);
		it.run(pathToTest.toString(), false);
		try {
			String content = String.join("", Files.readAllLines(pathToInstanceMap));
			JSONArray instances = new JSONArray(content);
			assertEquals(1, instances.length());

			JSONObject instance = instances.getJSONObject(0);
			assertEquals("source.c", instance.getString("filename"));
			assertEquals(42, instance.getInt("offset"));
			assertEquals(24, instance.getInt("length"));
			assertEquals("instance_name", instance.getString("name"));
			assertEquals("ffa462", instance.getString("id"));
			assertEquals("UNPAIRED", instance.getString("status"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Couldn't get oldInstanceMap.json");
		} catch (JSONException e) {
			e.printStackTrace();
			fail("Couldn't Perform JSON Operation!");
		}
	}
	
	@Test
	public void testRunSingleInstanceWithSlashInDir() {
		pathToTest = Paths.get(System.getProperty("user.dir"), "resources", "testing", "InstanceTracker",
				"singleInstance");
		Path pathToInstanceMap = Paths.get(pathToTest.toString(), "oldInstanceMap.json");
		String resultsDir = Paths.get(pathToTest.toString(), "results").toString();
		InstanceTracker it = new InstanceTracker(resultsDir);
		it.run(pathToTest.toString() + "/", false);
		try {
			String content = String.join("", Files.readAllLines(pathToInstanceMap));
			JSONArray instances = new JSONArray(content);
			assertEquals(1, instances.length());

			JSONObject instance = instances.getJSONObject(0);
			assertEquals("source.c", instance.getString("filename"));
			assertEquals(42, instance.getInt("offset"));
			assertEquals(24, instance.getInt("length"));
			assertEquals("instance_name", instance.getString("name"));
			assertEquals("ffa462", instance.getString("id"));
			assertEquals("UNPAIRED", instance.getString("status"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Couldn't get oldInstanceMap.json");
		} catch (JSONException e) {
			e.printStackTrace();
			fail("Couldn't Perform JSON Operation!");
		}
	}

	@Test
	public void testRunCheckOverride() {
		pathToTest = Paths.get(System.getProperty("user.dir"), "resources", "testing", "InstanceTracker",
				"singleInstance");
		String resultsDir = Paths.get(pathToTest.toString(), "results").toString();
		InstanceTracker it = new InstanceTracker(resultsDir);
		boolean run1 = it.run(pathToTest.toString(), false);
		boolean run2 = it.run(pathToTest.toString(), false);
		boolean run3 = it.run(pathToTest.toString(), true);

		assertEquals(true, run1);
		assertEquals(false, run2);
		assertEquals(true, run3);
	}

	@Test
	public void testMultipleTypeDirs() {
		pathToTest = Paths.get(System.getProperty("user.dir"), "resources", "testing", "InstanceTracker",
				"multipleTypes");
		Path pathToInstanceMap = Paths.get(pathToTest.toString(), "oldInstanceMap.json");
		String resultsDir = Paths.get(pathToTest.toString(), "results").toString();
		InstanceTracker it = new InstanceTracker(resultsDir);
		it.run(pathToTest.toString(), true);

		try {
			String content = String.join("", Files.readAllLines(pathToInstanceMap));
			JSONArray instances = new JSONArray(content);
			assertEquals(2, instances.length());

			JSONObject mutexInstance, spinInstance;
			JSONObject instance1 = instances.getJSONObject(0);
			JSONObject instance2 = instances.getJSONObject(1);
			if(instance1.getString("id").equals("ffa462")) {
				mutexInstance = instance1;
				spinInstance = instance2;
			} else {
				mutexInstance = instance2;
				spinInstance = instance1;
			}

			assertEquals("source_mutex.c", mutexInstance.getString("filename"));
			assertEquals(42, mutexInstance.getInt("offset"));
			assertEquals(24, mutexInstance.getInt("length"));
			assertEquals("mutex_instance", mutexInstance.getString("name"));
			assertEquals("ffa462", mutexInstance.getString("id"));
			assertEquals("UNPAIRED", mutexInstance.getString("status"));

			assertEquals("source_spin.c", spinInstance.getString("filename"));
			assertEquals(43, spinInstance.getInt("offset"));
			assertEquals(34, spinInstance.getInt("length"));
			assertEquals("spin_instance", spinInstance.getString("name"));
			assertEquals("ffa463", spinInstance.getString("id"));
			assertEquals("PAIRED", spinInstance.getString("status"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Couldn't get oldInstanceMap.json");
		} catch (JSONException e) {
			e.printStackTrace();
			fail("Couldn't Perform JSON Operation!");
		}
	}
	
	@Test
	public void testMultipleInstances() {
		pathToTest = Paths.get(System.getProperty("user.dir"), "resources", "testing", "InstanceTracker",
				"multipleInstances");
		Path pathToInstanceMap = Paths.get(pathToTest.toString(), "oldInstanceMap.json");
		String resultsDir = Paths.get(pathToTest.toString(), "results").toString();
		InstanceTracker it = new InstanceTracker(resultsDir);
		it.run(pathToTest.toString(), false);
		try {
			String content = String.join("", Files.readAllLines(pathToInstanceMap));
			JSONArray instances = new JSONArray(content);
			assertEquals(16, instances.length());

			boolean[] check = new boolean[16];
			for(int i = 0; i < 16; i++) {
				check[i] = false;
			}
			for(int i = 0; i < instances.length(); i++) {
				JSONObject instance = instances.getJSONObject(i);
				int id = Integer.parseInt(instance.getString("id").split("_")[1]);	
				
				assertEquals("source"+id+".c", instance.getString("filename"));
				assertEquals(Integer.parseInt("1"+id), instance.getInt("offset"));
				assertEquals(Integer.parseInt("2"+id), instance.getInt("length"));
				assertEquals("instance_"+id, instance.getString("name"));
				assertEquals("id_"+id, instance.getString("id"));
				assertEquals("status_"+id, instance.getString("status"));
				check[id-1] = true;
			}
			
			assertThat(Arrays.asList(check), not(hasItem(false)));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Couldn't get oldInstanceMap.json");
		} catch (JSONException e) {
			e.printStackTrace();
			fail("Couldn't Perform JSON Operation!");
		}
	}
	
	@Test
	public void testRunParseEntryFiltersCorrectly() {
		pathToTest = Paths.get(System.getProperty("user.dir"), "resources", "testing", "InstanceTracker",
				"badInstance");
		Path pathToInstanceMap = Paths.get(pathToTest.toString(), "oldInstanceMap.json");
		String resultsDir = Paths.get(pathToTest.toString(), "results").toString();
		InstanceTracker it = new InstanceTracker(resultsDir);
		boolean result = it.run(pathToTest.toString(), false);
		assertTrue(result);
		try {
			String content = String.join("", Files.readAllLines(pathToInstanceMap));
			JSONArray instances = new JSONArray(content);
			assertEquals(0, instances.length());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Couldn't get oldInstanceMap.json");
		} catch (JSONException e) {
			e.printStackTrace();
			fail("Couldn't Perform JSON Operation!");
		}
	}
	
	@Test
	public void testRunFailsWithIOException() {
		pathToTest = Paths.get(System.getProperty("user.dir"), "resources", "testing", "InstanceTracker",
				"fileWriteFail");
		String resultsDir = Paths.get(pathToTest.toString(), "results").toString();
		InstanceTracker it = new InstanceTracker(resultsDir);
		boolean result = it.run(pathToTest.toString(), true);
		assertFalse(result);
	}
	
	@Test
	public void testRunParseEntryVariants() throws InvalidInstanceFormatException, Exception {
		pathToTest = Paths.get(System.getProperty("user.dir"), "resources", "testing", "InstanceTracker",
				"singleInstance");
		String resultsDir = Paths.get(pathToTest.toString(), "results").toString();
		InstanceTracker it = new InstanceTracker(resultsDir);
		File[] instances = it.getInstances();
		assertEquals(1, instances.length);
		
		try {
			it.parseEntry(instances[0].getName(), "V2", false);
			fail("V1 instance should fail when using V2");
		} catch (InvalidInstanceFormatException e) {}
		
		JSONObject instance = it.parseEntry(instances[0].getName(), "V2");
		assertNotNull(instance);
		assertEquals("source.c", instance.getString("filename"));
		assertEquals(42, instance.getInt("offset"));
		assertEquals(24, instance.getInt("length"));
		assertEquals("instance_name", instance.getString("name"));
		assertEquals("ffa462", instance.getString("id"));
		assertEquals("UNPAIRED", instance.getString("status"));
		
		instance = it.parseEntry(instances[0].getName(), "V1");
		assertNotNull(instance);
		assertEquals("source.c", instance.getString("filename"));
		assertEquals(42, instance.getInt("offset"));
		assertEquals(24, instance.getInt("length"));
		assertEquals("instance_name", instance.getString("name"));
		assertEquals("ffa462", instance.getString("id"));
		assertEquals("UNPAIRED", instance.getString("status"));
	}
}
