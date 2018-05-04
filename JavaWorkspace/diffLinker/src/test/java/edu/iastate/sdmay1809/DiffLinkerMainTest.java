package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.TemporaryFolder;

import edu.iastate.sdmay1809.shared.Utils;

public class DiffLinkerMainTest {
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Rule
	public SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@Test
	public void testInit() {
		DiffLinkerMain dlm = new DiffLinkerMain();
		assertNotNull(dlm);
		assertThat(dlm, instanceOf(DiffLinkerMain.class));
	}

	@Test
	public void runMain() throws IOException, InterruptedException {
		// Setup Test Workspace!
		File workspace = testFolder.newFolder();
		File setupWorkspace = Paths.get("resources", "testing", "DiffLinker", "testSingleInstance").toAbsolutePath()
				.toFile();
		if(System.getProperty("os.name").contains("Windows")) {
			Utils.execute(new String[] {"robocopy", setupWorkspace.getAbsolutePath(), workspace.getAbsolutePath(), "/s", "/e"}, setupWorkspace, false);
		} else {
			Utils.execute(new String[] { "cp", "-R", ".", workspace.getAbsolutePath() }, setupWorkspace, false);
		}
		String[] args = new String[] {
				"-Ddiff_test_dir=" + Paths.get(workspace.getAbsolutePath(), "diffmap/").toString(),
				"-Dkernel_dir=" + Paths.get(workspace.getAbsolutePath(), "kernel/").toString(),
				"-Dresult_dir=" + Paths.get(workspace.getAbsolutePath(), "diffmap", "413_results").toString(),
				"-Dold_tag=v3.19-rc1", "-Dnew_tag=v4.13", "-Dtypes.1=spin" };

		DiffLinkerMain.main(args);

		assertThat(systemOutRule.getLog(), containsString("Done! Linked 1 instances"));
		assertThat(systemOutRule.getLog(), not(containsString("[ERROR] could not link instances!")));
	}
}
