package edu.iastate.sdmay1809;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

public class KernelRssFeed {
	SyndFeed feed;
	String newestVersion = "";
	boolean checkedNewestVersion = false;
	boolean includeRCs;
	
	
	public KernelRssFeed(String url, boolean includeRCs) throws IllegalArgumentException, MalformedURLException, FeedException, IOException {
		feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
		newestVersion = getNewestVersion();
		this.includeRCs = includeRCs;
	}
	
	public KernelRssFeed(String url) throws IllegalArgumentException, MalformedURLException, FeedException, IOException {
		feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
		newestVersion = getNewestVersion();
		this.includeRCs = true;
	}
	
	public KernelRssFeed(SyndFeed feed) throws IllegalArgumentException, MalformedURLException, FeedException, IOException {
		this.feed = feed;
		newestVersion = getNewestVersion();
		this.includeRCs = true;
	}
	
	public KernelRssFeed(SyndFeed feed, boolean includeRCs) throws IllegalArgumentException, MalformedURLException, FeedException, IOException {
		this.feed = feed;
		newestVersion = getNewestVersion();
		this.includeRCs = includeRCs;
	}
	
	
	public boolean isNewVersionAvailiable() throws IllegalArgumentException, MalformedURLException, FeedException, IOException {
		String testNew = updateNewestVersion();
		
		if(!newestVersion.equals(testNew)) {
			newestVersion = testNew;
			return true;
		}
		return false;
	}
	
	public String getNewestVersion() { 
		return newestVersion;
	}
	
	private String updateNewestVersion() throws IllegalArgumentException, MalformedURLException, FeedException, IOException{
		String version = "";
		for(SyndEntry entry : feed.getEntries()) {
			String pattern = "[0-9]+\\.[0-9]+[\\.-][(rc)0-9]+";

			if(!includeRCs) {
				pattern = "[0-9]+\\.[0-9]+\\.[0-9]+";
			}
			
			
			String[] parts = entry.getTitle().split(": ");
			if(!Pattern.matches(pattern, parts[0])) {
				continue;
			}
			if(parts[0].contains("rc")) {
				version = getGreaterVerisonNumber(version, parts[0]);
			}
			else {
				int index = parts[0].lastIndexOf('.');
				version = getGreaterVerisonNumber(version, parts[0].substring(0, index));
			}
		}
		return version;
	}
	
	private String getGreaterVerisonNumber(String vn1, String vn2) {
		if(vn1 == null || vn1.equals(""))
			return vn2;
		if(vn2 == null || vn2.equals(""))
			return vn1;
		String[] splitvn1 = vn1.split("[\\.-]");
		String[] splitvn2 = vn2.split("[\\.-]");
		for(int i = 0; i < 3; i++) {
			if(i == 2 && splitvn1.length > 2 && splitvn2.length > 2) {
				String pattern = "rc[0-9]+";
				if(Pattern.matches(pattern, splitvn1[i]) && Pattern.matches(pattern, splitvn2[i])){
					String test1 = splitvn1[i].replace("rc", "");
					String test2 = splitvn2[i].replace("rc", "");
					if(Integer.parseInt(test1) > Integer.parseInt(test2)) {
						return vn1;
					}
					else {
						return vn2;
					}
				}
				else if(Pattern.matches(pattern, splitvn1[i])) {
					return vn2;
				}
				else if(Pattern.matches(pattern, splitvn2[i])) {
					return vn1;
				}
			}
			else if(i == 2 && splitvn1.length <= 2) {		//Deal with matching version nums when one is an rc
				return vn1;
			}
			else if(i==2) {
				return vn2;
			}
			int vn1Int = Integer.parseInt(splitvn1[i]);
			int vn2Int = Integer.parseInt(splitvn2[i]);
			if(vn1Int > vn2Int)
				return vn1;
			else if(vn1Int < vn2Int)
				return vn2;
		}
		return vn1;
	}
}
