package edu.iastate.sdmay1809;

public class DiffConfig {
	
	public String OLD_TAG = "v3.17-rc1";
	public String NEW_TAG = "v3.18-rc1";
	public String DIFF_TEST_DIR = "/Volumes/dhanwada_cs/";
	public String KERNEL_DIR = "/Volumes/dhanwada_cs/kernel/";
	public String RESULT_DIR = "/Volumes/dhanwada_cs/sdmay18-09/linux-kernel-" + OLD_TAG.substring(1) + "/";
	public String[] TYPES = { "mutex", "spin" };
	
	public static class DiffConfigBuilder {
		private String OLD_TAG = "v3.17-rc1";
		private String NEW_TAG = "v3.18-rc1";
		private String DIFF_TEST_DIR = "/Volumes/dhanwada_cs/";
		private String KERNEL_DIR = "/Volumes/dhanwada_cs/kernel/";
		private String RESULT_DIR = "/Volumes/dhanwada_cs/sdmay18-09/linux-kernel-" + OLD_TAG.substring(1) + "/";
		private String[] TYPES = { "mutex", "spin" };
		
		public DiffConfig build() {
			return new DiffConfig(this);
		}
		
		public DiffConfigBuilder setOldTag(String old_tag) {
			this.OLD_TAG = Utils.coalesce(old_tag, OLD_TAG);
			return this;
		}
		
		public DiffConfigBuilder setNewTag(String new_tag) {
			this.NEW_TAG = Utils.coalesce(new_tag, NEW_TAG);
			return this;
		}
		
		public DiffConfigBuilder setDiffTestDir(String diff_test_dir) {
			this.DIFF_TEST_DIR = Utils.coalesce(diff_test_dir, DIFF_TEST_DIR);
			return this;
		}
		
		public DiffConfigBuilder setKernelDir(String kernel_dir) {
			this.KERNEL_DIR = Utils.coalesce(kernel_dir, KERNEL_DIR);
			return this;
		}
		
		public DiffConfigBuilder setResultDir(String result_dir) {
			this.RESULT_DIR = Utils.coalesce(result_dir, RESULT_DIR);
			return this;
		}
		
		public DiffConfigBuilder setTypes(String[] types) {
			this.TYPES = Utils.coalesce(types, TYPES);
			return this;
		}
	}
	
	private DiffConfig(DiffConfigBuilder builder) {
		this.OLD_TAG = builder.OLD_TAG;
		this.NEW_TAG = builder.NEW_TAG;
		this.DIFF_TEST_DIR = builder.DIFF_TEST_DIR;
		this.KERNEL_DIR = builder.KERNEL_DIR;
		this.RESULT_DIR = builder.RESULT_DIR;
		this.TYPES = builder.TYPES;
	}
	
	public static DiffConfigBuilder builder() {
		return new DiffConfigBuilder();
	}
}
