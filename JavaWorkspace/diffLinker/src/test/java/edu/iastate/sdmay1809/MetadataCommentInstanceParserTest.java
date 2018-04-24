package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import edu.iastate.sdmay1809.shared.InstanceTracker.InstanceParser;
import edu.iastate.sdmay1809.shared.InstanceTracker.InvalidInstanceFormatException;

public class MetadataCommentInstanceParserTest {

	@Test
	public void testInit() {
		InstanceParser ip = new MetadataCommentInstanceParser();
		assertNotNull(ip);
		assertThat(ip, instanceOf(MetadataCommentInstanceParser.class));
		assertEquals("MetadataCommentInstanceParser", ip.getName());
	}
	
	@Test
	public void testInitName() {
		InstanceParser ip = new MetadataCommentInstanceParser("ipTest");
		assertNotNull(ip);
		assertThat(ip, instanceOf(MetadataCommentInstanceParser.class));
		assertEquals("ipTest", ip.getName());
	}
	
	@Test
	public void testParseEntry() throws IOException, InvalidInstanceFormatException {
		Path pathToValidEntries = Paths.get("resources", "testing", "MetadataCommentInstanceParser", "testValidEntries", "validEntries.json");
		String content = String.join("", Files.readAllLines(pathToValidEntries));
		JSONArray entries = new JSONArray(content);
		InstanceParser ip = new MetadataCommentInstanceParser();
		
		for(int i = 0; i < entries.length(); i++) {
			JSONObject entry = entries.getJSONObject(i);
			String instance = entry.getString("metadata");
			JSONObject data = entry.getJSONObject("old");
			JSONObject test = ip.parseEntry(instance);
			
			assertEquals(data.getString("filename"), test.getString("filename"));
			assertEquals(data.getString("name"), test.getString("name"));
			assertEquals(data.getString("status"), test.getString("status"));
			assertEquals(data.getString("id"), test.getString("id"));
			assertEquals(data.getInt("offset"), test.getInt("offset"));
			assertEquals(data.getInt("length"), test.getInt("length"));
		}
	}
	
	@Test
	public void testParseEntryFails() {
		String invalidEntry = "/* 8f68c0@@@PAIRED78458@@@3drivers/gpu/drm/i915/i915_debugfs.clock */";
		String invalidEntry2 = "/* 8f68c0@@@PAIRED@@@78f58@@@31@@@drivers/gpu/drm/i915/i915_debugfs.c@@@lock */";
		InstanceParser ip = new MetadataCommentInstanceParser();
		JSONObject test = null;
		try {
			test = ip.parseEntry(invalidEntry);
			fail("Parsing should fail");
		} catch (InvalidInstanceFormatException e) {
			assertNull(test);
		}
		
		try {
			test = ip.parseEntry(invalidEntry2);
			fail("Parsing should fail");
		} catch (InvalidInstanceFormatException e) {
			assertNull(test);
		}
	}
	
}
