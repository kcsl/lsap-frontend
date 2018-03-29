package edu.iastate.sdmay1809;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

public class ConfigWriter {
	private String location;
	private String old_tag;
	private String new_tag;
	private String diff_test_dir;
	private String kernel_dir;
	private String result_dir;
	private ArrayList<String> types;
	
	public ConfigWriter(String old_tag, String new_tag) {
		this.old_tag = old_tag;
		this.new_tag = new_tag;
		String ws = System.getProperty("user.home") + "/Workspace";
		location = ws + "/DiffConfig.json";
		diff_test_dir = ws;
		kernel_dir = ws;
		result_dir = ws;
		types = new ArrayList<String>();
	}
	
	public void write() throws IOException {
		JSONObject obj = new JSONObject();
		obj
			.put("old_tag", old_tag)
			.put("new_tag", new_tag)
			.put("diff_test_dir", diff_test_dir)
			.put("kernel_dir", kernel_dir)
			.put("result_dir", result_dir)
			.put("types", types);
		FileWriter fw = new FileWriter(location);
		obj.write(fw);
	}
}
