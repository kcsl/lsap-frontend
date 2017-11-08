package rsstesting;

import java.io.IOException;
import java.net.MalformedURLException;

import com.rometools.rome.io.FeedException;

public class Main {
	static long lastTime;
	public static void main(String[] args) throws IllegalArgumentException, MalformedURLException, FeedException, IOException, InterruptedException {
		KernelRssFeed feed = new KernelRssFeed("https://www.kernel.org/feeds/kdist.xml");
		while(true) {
			System.out.println("Version: " + feed.getNewestVersion());
			Thread.sleep(30000);
		}
	}
}
