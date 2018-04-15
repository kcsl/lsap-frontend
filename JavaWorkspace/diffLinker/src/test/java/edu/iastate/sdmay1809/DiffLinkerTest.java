package edu.iastate.sdmay1809;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import edu.iastate.sdmay1809.shared.DiffConfig;
import edu.iastate.sdmay1809.shared.Utils;
import edu.iastate.sdmay1809.shared.InstanceTracker.InstanceTracker;

public class DiffLinkerTest {
	
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();
	
	@Test
	public void testInit() throws Exception {
		DiffConfig config = DiffConfig.builder().build();
		DiffLinker dl = new DiffLinker(config, false, 3);
		assertTrue(dl != null);
	}
	
	@Test
	public void testSingleInstance() throws JSONException, IOException, InterruptedException {
		File workspace = testFolder.newFolder();
		File setupWorkspace = Paths.get("resources", "testing", "DiffLinker", "testSingleInstance").toAbsolutePath().toFile();
		Utils.execute(new String[] { "cp", "-R", ".", workspace.getAbsolutePath() }, setupWorkspace);
		
		DiffConfig config = DiffConfig.builder()
				.setDiffTestDir(Paths.get(workspace.getAbsolutePath(), "diffmap/").toString())
				.setKernelDir(Paths.get(workspace.getAbsolutePath(), "kernel/").toString()).setOldTag("v3.19-rc1")
				.setNewTag("v4.13")
				.setResultDir(Paths.get(workspace.getAbsolutePath(), "diffmap", "413_results").toString()).build();
		
		InstanceTracker it = new InstanceTracker(config.RESULT_DIR, Arrays.asList(config.TYPES));
		it.run(Paths.get(config.DIFF_TEST_DIR, "newInstanceMap.json").toFile(), false);
		DiffLinker dl = new DiffLinker(config, false, 10);
		
		dl.run("newInstanceMap.json");
	}
}
