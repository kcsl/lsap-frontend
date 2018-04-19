package edu.iastate.sdmay1809;

import java.io.IOException;
import java.net.MalformedURLException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;


import com.rometools.rome.io.FeedException;

public class Main {
	static long lastTime;
	public static void main(String[] args) throws IllegalArgumentException, MalformedURLException, FeedException, IOException, InterruptedException, InvalidRemoteException, TransportException, GitAPIException {
		
		RssConfig config = RssConfig.builder(RssConfig.Builder.class, args).build();
		
		KernelRssFeed feed = new KernelRssFeed(config.FEED_URL, false);
		RepositoryManager manager = new RepositoryManager(config.WORKSPACE, config.GIT_URL);
		do {
			String oldVersion = feed.getNewestVersion();
			if(feed.isNewVersionAvailiable()) {
				System.out.println("New version: " + feed.getNewestVersion());
				manager.UpdateRepoToTag("v" + feed.getNewestVersion());
				ConfigWriter cw = new ConfigWriter(oldVersion, feed.getNewestVersion(), config.WORKSPACE);
				cw.write();
			}
			Thread.sleep(config.WAIT_PERIOD);
		} while(!config.RUN_ONCE);
	}
}
