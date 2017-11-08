package rsstesting;
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
	String url;
	SyndFeed feed;
	String newestVersion = "";
	boolean checkedNewestVersion = false;
	KernelRssFeed(String url) throws IllegalArgumentException, MalformedURLException, FeedException, IOException {
		this.url = url;
		newestVersion = getNewestVersion();
	}
	
	public String getNewestVersion() throws IllegalArgumentException, MalformedURLException, FeedException, IOException{
		feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
		for(SyndEntry entry : feed.getEntries()) {
			String pattern = "[0-9]+\\.[0-9]+[\\.-][(rc)0-9]+";

			String[] parts = entry.getTitle().split(": ");
			if(!Pattern.matches(pattern, parts[0])) {
				continue;
			}
			newestVersion = getGreaterVerisonNumber(newestVersion, parts[0]);
		}
		return newestVersion;
	}
	
	String getGreaterVerisonNumber(String vn1, String vn2) {
		if(vn1 == null || vn1.equals(""))
			return vn2;
		if(vn2 == null || vn2.equals(""))
			return vn1;
		String[] splitvn1 = vn1.split("[\\.-]");
		String[] splitvn2 = vn2.split("[\\.-]");
		for(int i = 0; i < 3; i++) {
			if(i == 2) {
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
