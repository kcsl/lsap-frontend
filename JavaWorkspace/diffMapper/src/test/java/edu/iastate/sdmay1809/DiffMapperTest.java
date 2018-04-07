package edu.iastate.sdmay1809;

import static org.junit.Assert.*;

import java.io.File;
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
	public void testRunSingleInstance() throws JSONException, IOException, InterruptedException, Exception {
		// Git Setup!
		String testKernelDir = Paths.get("resources", "testing", "DiffMapper", "runSingleInstance", "kernel")
				.toString();
		Utils.execute(new String[] { "cp", "-R", "../.notgit", ".git" }, new File(testKernelDir));

		String configFile = Paths.get("resources", "testing", "DiffMapper", "runSingleInstance", "config.json")
				.toString();
		DiffConfig config = DiffConfig.builder(configFile).build();

		DiffMapper dm = new DiffMapper(config, false);
		int placed = dm.run("oldInstanceMap.json");

		// Git Cleanup!
		Utils.execute(new String[] {"git", "checkout", "v3.19-rc1"}, new File(testKernelDir));
		Utils.execute(new String[] { "rm", "-rf", ".git/" }, new File(testKernelDir));

		assertEquals(1, placed);
	}

	@Test
	public void testAllowPrints() throws JSONException, IOException, InterruptedException, Exception {
		// Git Setup!
		String testKernelDir = Paths.get("resources", "testing", "DiffMapper", "runSingleInstance", "kernel")
				.toString();
		Utils.execute(new String[] { "cp", "-R", "../.notgit", ".git" }, new File(testKernelDir));

		String configFile = Paths.get("resources", "testing", "DiffMapper", "runSingleInstance", "config.json")
				.toString();
		DiffConfig config = DiffConfig.builder(configFile).build();

		DiffMapper dm = new DiffMapper(config, true);
		int placed = dm.run("oldInstanceMap.json");

		// Git Cleanup!
		Utils.execute(new String[] {"git", "checkout", "v3.19-rc1"}, new File(testKernelDir));
		Utils.execute(new String[] { "rm", "-rf", ".git/" }, new File(testKernelDir));

		assertEquals(1, placed);
	}

	@Test
	public void testMultpleInstancesSingleFile() throws JSONException, IOException, InterruptedException, Exception {
		// Git Setup!
		String testKernelDir = Paths.get("resources", "testing", "DiffMapper", "runMultipleInstancesSingleFile", "kernel")
				.toString();
		Utils.execute(new String[] { "cp", "-R", "../.notgit", ".git" }, new File(testKernelDir));

		String configFile = Paths.get("resources", "testing", "DiffMapper", "runMultipleInstancesSingleFile", "config.json")
				.toString();
		DiffConfig config = DiffConfig.builder(configFile).build();

		DiffMapper dm = new DiffMapper(config, false);
		int placed = dm.run("oldInstanceMap.json");

		// Git Cleanup!
		Utils.execute(new String[] {"git", "checkout", "v3.17-rc1"}, new File(testKernelDir));
		Utils.execute(new String[] { "rm", "-rf", ".git/" }, new File(testKernelDir));

		assertEquals(8, placed);
	}
}
