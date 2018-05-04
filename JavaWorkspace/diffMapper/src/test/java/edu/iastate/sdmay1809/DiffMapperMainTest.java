package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.TemporaryFolder;

import edu.iastate.sdmay1809.shared.Utils;

public class DiffMapperMainTest {
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();
	
	@Rule
	public SystemOutRule systemOutRule = new SystemOutRule().enableLog();
	
	@Test
	public void testInit() {
		DiffMapperMain dmm = new DiffMapperMain();
		
		assertNotNull(dmm);
		assertThat(dmm, instanceOf(DiffMapperMain.class));
	}
	
	@Test
	public void runMain() throws JSONException, IOException, InterruptedException, Exception {
		// Setup Test Workspace!
		File workspace = testFolder.newFolder();
		File baseDir = Paths.get("resources", "testing", "DiffMapper", "runSingleInstance").toAbsolutePath().toFile();
		if(System.getProperty("os.name").contains("Windows")) {
			Utils.execute(new String[] {"robocopy", baseDir.getAbsolutePath(), workspace.getAbsolutePath(), "/s", "/e"}, baseDir, true);
		} else {
			Utils.execute(new String[] { "cp", "-R", ".", workspace.getAbsolutePath() }, baseDir, true);
		}

		// Git Setup!
		if(System.getProperty("os.name").contains("Windows")) {
			Utils.execute(new String[] {"robocopy", ".notgit/", "kernel/.git/", "/s", "/e"}, workspace, true);
		} else {
			Utils.execute(new String[] { "cp", "-R", ".notgit/", "kernel/.git/" }, workspace, true);
		}
		
		String[] args = new String[] {
				"-Ddiff_test_dir=" + Paths.get(workspace.getAbsolutePath(), "diffmap/").toString(),
				"-Dkernel_dir=" + Paths.get(workspace.getAbsolutePath(), "kernel/").toString(),
				"-Dold_result_dir=" + Paths.get(workspace.getAbsolutePath(), "diffmap", "results").toString(),
				"-Dold_tag=v3.19-rc1",
				"-Dnew_tag=v3.20",
				"-Dtypes.1=mutex"
		};
		
		DiffMapperMain.main(args);
		
		assertThat(systemOutRule.getLog(), containsString("Done! Applied 1 changes"));
		assertThat(systemOutRule.getLog(), not(containsString("[ERROR] could not map differences!")));
	}
}
