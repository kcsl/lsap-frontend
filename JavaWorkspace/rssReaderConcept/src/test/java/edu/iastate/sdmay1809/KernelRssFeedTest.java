package edu.iastate.sdmay1809;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Assert;
import org.junit.Test;


import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

public class KernelRssFeedTest {

	static SyndFeed feed;
	
	@BeforeClass
	public static void setup() {
		try {
			File f = new File("testFeed.xml");
			if(!f.exists()) 
				Assert.fail("test feed file does not exist");
			feed = new SyndFeedInput().build(new XmlReader(f));
		}		
		catch(Exception e) {
			Assert.fail("Threw file expection: " + e.getMessage());
			return;
		}
		
		if(feed == null)
			Assert.fail("Failed to create feed");
	}
	
	
	@Test
	public void kernelRssFeedTestWithRc() {
		KernelRssFeed kernelRssReader; 
		try {
			kernelRssReader = new KernelRssFeed(feed);
		}
		catch(Exception e) {
			Assert.fail("Threw expection creating reader: " + e.getMessage());
			return;
		}
		
		try {
			kernelRssReader.isNewVersionAvailiable();
		} catch (IllegalArgumentException | FeedException | IOException e) {
			e.printStackTrace();
		}
		
		Assert.assertTrue(kernelRssReader.getNewestVersion().equals("4.17-rc9"));	
	}
	
	
	@Test
	public void kernelRssFeedTestWithoutRc() {
		KernelRssFeed kernelRssReader; 
		try {
			kernelRssReader = new KernelRssFeed(feed, false);
		}
		catch(Exception e) {
			Assert.fail("Threw expection creating reader: " + e.getMessage());
			return;
		}
		
		try {
			kernelRssReader.isNewVersionAvailiable();
		} catch (IllegalArgumentException | FeedException | IOException e) {
			e.printStackTrace();
		}
		
		Assert.assertTrue(kernelRssReader.getNewestVersion().equals("4.16"));	
	}
	
	@Test
	public void testKerenlRssConstructors() {
		String feedURL = "https://www.kernel.org/feeds/kdist.xml";
		KernelRssFeed feed;
		try {
			feed = new KernelRssFeed(feedURL, false);
		}
		catch(Exception e) {
			Assert.fail("Threw expection creating reader: " + e.getMessage());
			return;
		}
		
		Assert.assertEquals(feed.getFeed().getLink(), "http://www.kernel.org"); 
		Assert.assertFalse(feed.getIncludeRCs());
		
		try {
			feed = new KernelRssFeed(feedURL);
		}
		catch(Exception e) {
			Assert.fail("Threw expection creating reader: " + e.getMessage());
			return;
		}
		
		Assert.assertEquals(feed.getFeed().getLink(), "http://www.kernel.org"); 
		Assert.assertTrue(feed.getIncludeRCs());
		
	}

}
