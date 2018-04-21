package edu.iastate.sdmay1809.shared;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DiffConfig extends Config {
	public String OLD_TAG;
	public String NEW_TAG;
	public String DIFF_TEST_DIR;
	public String KERNEL_DIR;
	public String RESULT_DIR;
	public String[] TYPES;

	@SuppressWarnings("unchecked")
	DiffConfig(Builder builder) {
		super(builder);
		
		OLD_TAG = (String) configOptions.get("old_tag");
		NEW_TAG = (String) configOptions.get("new_tag");
		DIFF_TEST_DIR = (String) configOptions.get("diff_test_dir");
		KERNEL_DIR = (String) configOptions.get("kernel_dir");
		RESULT_DIR = (String) configOptions.get("result_dir");
		Object types = configOptions.get("types");
		if(types instanceof List) {
			List<Object> list = (List<Object>) types;
			List<String> convert = list.stream().filter(o -> o != null).map(o -> {
				return o.toString();
			}).collect(Collectors.toList());
			TYPES = convert.toArray(new String[0]);
		} else {
			TYPES = new String[] {};
		}
	}

	public static class Builder extends Config.Builder<DiffConfig> {
		
		{
			String cwd = System.getProperty("user.dir");
			configOptions.putIfAbsent("old_tag", "v3.17-rc1");
			configOptions.putIfAbsent("new_tag", "v3.18-rc1");
			configOptions.putIfAbsent("diff_test_dir", Paths.get(cwd, "diffmap").toString());
			configOptions.putIfAbsent("kernel_dir", Paths.get(cwd, "kernel").toString());
			configOptions.putIfAbsent("result_dir", Paths.get(cwd, "diffmap", "results").toString());
			configOptions.putIfAbsent("types", Arrays.asList(new String[] { "mutex", "spin" }));
		};
		
		public Builder() {
			super();
		}
		
		public Builder(File file) {
			super(file);
		}
		
		public Builder(Builder base) {
			super(base);
		}

		public Builder setOldTag(String old_tag) {
			setConfig("old_tag", old_tag);
			return this;
		}

		public Builder setNewTag(String new_tag) {
			setConfig("new_tag", new_tag);
			return this;
		}

		public Builder setDiffTestDir(String diff_test_dir) {
			setConfig("diff_test_dir", diff_test_dir);
			return this;
		}

		public Builder setKernelDir(String kernel_dir) {
			setConfig("kernel_dir", kernel_dir);
			return this;
		}

		public Builder setResultDir(String result_dir) {
			setConfig("result_dir", result_dir);
			return this;
		}

		public Builder setTypes(String[] types) {
			if(types != null) {
				setConfig("types", Arrays.asList(types));
			}
			return this;
		}
	}
}
