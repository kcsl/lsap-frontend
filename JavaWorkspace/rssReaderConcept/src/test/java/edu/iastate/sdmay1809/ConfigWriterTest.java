package edu.iastate.sdmay1809;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class ConfigWriterTest {
	@Test
	public void TestWriter() throws URISyntaxException, IOException {
		File file = new File(System.getProperty("user.home"));
		ConfigWriter cw = new ConfigWriter("old_id", "new_id", file.getAbsolutePath());
		cw.write();
		String configFileContent;
		Path configFilePath = Paths.get(file.getAbsolutePath() + "/DiffConfig.json");
		configFileContent = String.join("\n", Files.readAllLines(configFilePath, Charset.forName("UTF-8")));
		JSONObject configObject = new JSONObject(configFileContent);
		Assert.assertTrue(configObject.getString("old_tag").equals("old_id"));

		Assert.assertTrue(configObject.getString("new_tag").equals("new_id"));
		
		configFilePath.toFile().delete();
		
	}
}
