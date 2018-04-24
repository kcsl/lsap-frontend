package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.rometools.rome.io.FeedException;

public class RssReaderMainTest {
	
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void testInit() {
		Main m = new Main();
		
		assertNotNull(m);
		assertThat(m, instanceOf(Main.class));
	}
	
	@Test
	public void testRunMain() throws IllegalArgumentException, MalformedURLException, InvalidRemoteException, TransportException, FeedException, IOException, InterruptedException, GitAPIException {
		String[] args = new String[] {
				"-Dfeed_url=file://" + Paths.get(System.getProperty("user.dir"), "testFeed.xml").toString(),
				"-Dgit_url=https://github.com/Pr1sM/test-repo.git",
				"-Drun_once=true",
				"-Dwait_period=1",
				"-Dworkspace="+ testFolder.newFolder().getAbsolutePath()
		};
		
		Main.main(args);
	}
	
}
