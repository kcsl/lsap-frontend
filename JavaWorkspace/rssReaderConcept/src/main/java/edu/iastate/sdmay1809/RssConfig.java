package edu.iastate.sdmay1809;

import java.io.File;

import edu.iastate.sdmay1809.shared.Config;

public class RssConfig extends Config {

	public String FEED_URL;
	public String GIT_URL;
	public String WORKSPACE;
	public boolean RUN_ONCE;
	public long WAIT_PERIOD;
	
	public RssConfig(Builder builder) {
		super(builder);
		
		Object ro = configOptions.get("run_once");
		if(ro instanceof Boolean) {
			this.RUN_ONCE = (Boolean)ro;
		} else {
			this.RUN_ONCE = ro.toString().equalsIgnoreCase("true");
		}
		
		Object wp = configOptions.get("wait_period");
		if(wp instanceof Long) {
			this.WAIT_PERIOD = (Long)wp;
		} else {
			try {
				this.WAIT_PERIOD = Long.parseLong(wp.toString());
			} catch (Exception e) {
				this.WAIT_PERIOD = 30000;
			}
			
		}
		
		this.FEED_URL = configOptions.get("feed_url").toString();
		this.GIT_URL = configOptions.get("git_url").toString();
		this.WORKSPACE = configOptions.get("workspace").toString();
	}
	
	public static class Builder extends Config.Builder<RssConfig> {
		
		{
			// Set default values
			configOptions.putIfAbsent("run_once", false);
			configOptions.putIfAbsent("wait_period", 30000);
			configOptions.putIfAbsent("feed_url", "https://www.kernel.org/feeds/kdist.xml");
			configOptions.putIfAbsent("git_url", "https://github.com/torvalds/linux.git");
			configOptions.putIfAbsent("workspace", System.getProperty("user.dir"));
		}
		
		public Builder() {
			super();
		}
		
		public Builder(File file) {
			super(file);
		}
		
		public Builder(Builder base) {
			super(base);
		}
		
		public Builder setFeedUrl(String url) {
			setConfig("feed_url", url);
			return this;
		}
		
		public Builder setGitUrl(String url) {
			setConfig("git_url", url);
			return this;
		}
		
		public Builder setRunOnce(Boolean runOnce) {
			setConfig("run_once", runOnce);
			return this;
		}
		
		public Builder setWaitPeriod(Long waitPeriod) {
			setConfig("wait_period", waitPeriod);
			return this;
		}
		
		public Builder setWorkspace(String dir) {
			setConfig("workspace", dir);
			return this;
		}
	}
	
}
