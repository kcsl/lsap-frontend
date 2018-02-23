package rsstesting;

import java.io.IOException;
import java.net.MalformedURLException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import com.rometools.rome.io.FeedException;

public class Main {
	static long lastTime;
	public static void main(String[] args) throws IllegalArgumentException, MalformedURLException, FeedException, IOException, InterruptedException, InvalidRemoteException, TransportException, GitAPIException {
		KernelRssFeed feed = new KernelRssFeed("https://www.kernel.org/feeds/kdist.xml", false);
		RepositoryManager manager = new RepositoryManager();
		while(true) {
			if(feed.isNewVersionAvailiable()) {
				System.out.println("New version: " + feed.getNewestVersion());
				manager.UpdateRepoToTag("v" + feed.getNewestVersion());
			}
			Thread.sleep(30000);
		}
	}
}
