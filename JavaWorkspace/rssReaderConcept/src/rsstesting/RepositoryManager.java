package rsstesting;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class RepositoryManager {
	Git git;	
	public RepositoryManager() throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		if( getDirectory().exists()) {
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			Repository repo = builder.findGitDir(getDirectory())
			  .setMustExist(true)
			  .readEnvironment() 
			  .build();
			
			git = Git.wrap(repo);			
		}
		else {
			System.out.println("Cloning linux kernel for the first time\nThis may take some time");
			
			git = Git.cloneRepository()
					  .setCloneSubmodules( true )
					  .setURI( "https://github.com/torvalds/linux.git" )
					  .setDirectory( getDirectory() )
					  .call();
	
			System.out.println("Finished Cloning!");
		}
		
	}
	
	
	private File getDirectory() {
		return new File(System.getProperty("user.home") + "/LinuxKernel/linux");
	}
	
	public void UpdateRepoToTag(String tag) throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		git.checkout().setName("master").call();
		git.pull().call();
		git.checkout().setName(tag).call();
	}
	

}
