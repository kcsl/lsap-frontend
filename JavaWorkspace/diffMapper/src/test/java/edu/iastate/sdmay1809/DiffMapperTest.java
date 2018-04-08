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
		// Setup Test Workspace!
		File workspace = testFolder.newFolder();
		File baseDir = Paths.get("resources", "testing", "DiffMapper", "runSingleInstance").toAbsolutePath().toFile();
		Utils.execute(new String[] { "cp", "-R", ".", workspace.getAbsolutePath() }, baseDir);

		// Git Setup!
		Utils.execute(new String[] { "cp", "-R", ".notgit/", "kernel/.git/" }, workspace);

		DiffConfig config = DiffConfig.builder()
				.setDiffTestDir(Paths.get(workspace.getAbsolutePath(), "diffmap/").toString())
				.setKernelDir(Paths.get(workspace.getAbsolutePath(), "kernel/").toString()).setOldTag("v3.19-rc1")
				.setNewTag("v3.20")
				.setResultDir(Paths.get(workspace.getAbsolutePath(), "diffmap", "results").toString()).build();

		DiffMapper dm = new DiffMapper(config, false);
		int placed = dm.run("oldInstanceMap.json");

		assertEquals(1, placed);
	}

	@Test
	public void testAllowPrints() throws JSONException, IOException, InterruptedException, Exception {
		// Setup Test Workspace!
		File workspace = testFolder.newFolder();
		File baseDir = Paths.get("resources", "testing", "DiffMapper", "runSingleInstance").toAbsolutePath().toFile();
		Utils.execute(new String[] { "cp", "-R", ".", workspace.getAbsolutePath() }, baseDir);

		// Git Setup!
		Utils.execute(new String[] { "cp", "-R", ".notgit/", "kernel/.git/" }, workspace);

		DiffConfig config = DiffConfig.builder()
				.setDiffTestDir(Paths.get(workspace.getAbsolutePath(), "diffmap/").toString())
				.setKernelDir(Paths.get(workspace.getAbsolutePath(), "kernel/").toString()).setOldTag("v3.19-rc1")
				.setNewTag("v3.20")
				.setResultDir(Paths.get(workspace.getAbsolutePath(), "diffmap", "results").toString()).build();

		DiffMapper dm = new DiffMapper(config, true);
		int placed = dm.run("oldInstanceMap.json");

		assertEquals(1, placed);
	}

	@Test
	public void testMultpleInstancesSingleFile() throws JSONException, IOException, InterruptedException, Exception {
		// Setup Test Workspace!
		File workspace = testFolder.newFolder();
		File baseDir = Paths.get("resources", "testing", "DiffMapper", "runMultipleInstancesSingleFile")
				.toAbsolutePath().toFile();
		Utils.execute(new String[] { "cp", "-R", ".", workspace.getAbsolutePath() }, baseDir);

		// Git Setup!
		Utils.execute(new String[] { "cp", "-R", ".notgit/", "kernel/.git/" }, workspace);

		DiffConfig config = DiffConfig.builder()
				.setDiffTestDir(Paths.get(workspace.getAbsolutePath(), "diffmap/").toString())
				.setKernelDir(Paths.get(workspace.getAbsolutePath(), "kernel/").toString()).setOldTag("v3.17-rc1")
				.setNewTag("v3.18-rc1")
				.setResultDir(Paths.get(workspace.getAbsolutePath(), "diffmap", "results").toString()).build();

		DiffMapper dm = new DiffMapper(config, false);
		int placed = dm.run("oldInstanceMap.json");

		assertEquals(8, placed);
	}
}
