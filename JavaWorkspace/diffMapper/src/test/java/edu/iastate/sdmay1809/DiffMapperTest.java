package edu.iastate.sdmay1809;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DiffMapperTest {
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();
	
	@Test
	public void testInit() {
		DiffMapper dm = new DiffMapper(DiffConfig.builder().build(), false);
		assertTrue(dm != null);
	}
	
	@Test
	public void testRunSingleInstance() throws JSONException, IOException {
		String configFile = Paths.get("resources", "testing", "DiffMapper", "runSingleInstance", "config.json").toString();
		DiffConfig config = DiffConfig.builder(configFile).build();

		DiffMapper dm = new DiffMapper(config, false);
		int placed = dm.run("oldInstanceMap.json");
		
		assertEquals(1, placed);
	}
}
