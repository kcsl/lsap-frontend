package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class RepositoryManagerTest {
	//https://github.com/Pr1sM/test-repo.git
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();
	
	@Test
	public void testInit() throws InvalidRemoteException, TransportException, IOException, GitAPIException {
		File workspace = testFolder.newFolder();
		RepositoryManager rm = new RepositoryManager(workspace.getAbsolutePath(), "https://github.com/Pr1sM/test-repo.git");
		
		assertNotNull(rm);
		assertThat(rm, instanceOf(RepositoryManager.class));
	}
	
	@Test
	public void testInitExistingKernel() throws InvalidRemoteException, TransportException, IOException, GitAPIException {
		File workspace = testFolder.newFolder();
		RepositoryManager rm = new RepositoryManager(workspace.getAbsolutePath(), "https://github.com/Pr1sM/test-repo.git");
		RepositoryManager rm2 = new RepositoryManager(workspace.getAbsolutePath(), "https://github.com/Pr1sM/test-repo.git");
		
		assertNotNull(rm);
		assertThat(rm, instanceOf(RepositoryManager.class));
		
		assertNotNull(rm2);
		assertThat(rm, instanceOf(RepositoryManager.class));
	}
	
	@Test
	public void updateToRepoWorks() throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		File workspace = testFolder.newFolder();
		RepositoryManager rm = new RepositoryManager(workspace.getAbsolutePath(), "https://github.com/Pr1sM/test-repo.git");
		
		rm.UpdateRepoToTag("test_tag");
		
		try {
			rm.UpdateRepoToTag("not_a_tag");
			fail("An exception should throw if the tag does not exist");
		} catch (Exception e) {}
	}
}
