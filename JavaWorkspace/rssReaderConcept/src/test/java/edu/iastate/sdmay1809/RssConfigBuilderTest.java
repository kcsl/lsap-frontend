package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class RssConfigBuilderTest {
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	RssConfig.Builder builder;

	@Before
	public void setUp() {
		builder = RssConfig.builder(RssConfig.Builder.class);
	}

	@Test
	public void builderDefaultConstructor() {
		assertThat(RssConfig.builder(RssConfig.Builder.class), instanceOf(RssConfig.Builder.class));
	}

	@Test
	public void builderBaseConstructor() {
		assertThat(RssConfig.builder(RssConfig.Builder.class, builder), instanceOf(RssConfig.Builder.class));
		builder = builder.setFeedUrl("a_feed_url").setGitUrl("a_git_url").setRunOnce(true).setWaitPeriod(30L)
				.setWorkspace("dir");
		RssConfig base = builder.build();
		RssConfig config = RssConfig.builder(RssConfig.Builder.class, builder).build();

		assertEquals(base.FEED_URL, config.FEED_URL);
		assertEquals(base.GIT_URL, config.GIT_URL);
		assertEquals(base.RUN_ONCE, config.RUN_ONCE);
		assertEquals(base.WAIT_PERIOD, config.WAIT_PERIOD);
		assertEquals(base.WORKSPACE, config.WORKSPACE);
	}

	@Test
	public void builderFileConstructor() throws IOException {
		File fullPath = setUpConfigFile(
				"{\"feed_url\":\"a_feed_url\",\"git_url\":\"a_git_url\",\"workspace\":\"a_workspace\",\"run_once\":\"true\",\"wait_period\":\"30\"}");

		RssConfig base = RssConfig.builder(RssConfig.Builder.class).build();
		RssConfig file = RssConfig.builder(RssConfig.Builder.class, fullPath).build();
		
		assertEquals("a_feed_url", file.FEED_URL);
		assertEquals("a_git_url", file.GIT_URL);
		assertEquals(true, file.RUN_ONCE);
		assertEquals(30L, file.WAIT_PERIOD);
		assertEquals("a_workspace", file.WORKSPACE);
		
		assertNotEquals(base.FEED_URL, file.FEED_URL);
		assertNotEquals(base.GIT_URL, file.GIT_URL);
		assertNotEquals(base.RUN_ONCE, file.RUN_ONCE);
		assertNotEquals(base.WAIT_PERIOD, file.WAIT_PERIOD);
		assertNotEquals(base.WORKSPACE, file.WORKSPACE);
	}

	@Test
	public void builderGitUrl() {
		RssConfig r1 = builder.build();
		RssConfig r2 = builder.setGitUrl("non-default").build();
		RssConfig r3 = builder.setGitUrl(null).build();
		
		assertNotEquals(r1.GIT_URL, r2.GIT_URL);
		assertEquals(r2.GIT_URL, r3.GIT_URL);
		
		assertEquals(r1.FEED_URL, r2.FEED_URL);
		assertEquals(r1.RUN_ONCE, r2.RUN_ONCE);
		assertEquals(r1.WAIT_PERIOD, r2.WAIT_PERIOD);
		assertEquals(r1.WORKSPACE, r2.WORKSPACE);
		
		assertEquals(r3.FEED_URL, r2.FEED_URL);
		assertEquals(r3.RUN_ONCE, r2.RUN_ONCE);
		assertEquals(r3.WAIT_PERIOD, r2.WAIT_PERIOD);
		assertEquals(r3.WORKSPACE, r2.WORKSPACE);
	}

	@Test
	public void builderFeedUrl() {
		RssConfig r1 = builder.build();
		RssConfig r2 = builder.setFeedUrl("non-default").build();
		RssConfig r3 = builder.setFeedUrl(null).build();
		
		assertNotEquals(r1.FEED_URL, r2.FEED_URL);
		assertEquals(r2.FEED_URL, r3.FEED_URL);
		
		assertEquals(r1.GIT_URL, r2.GIT_URL);
		assertEquals(r1.RUN_ONCE, r2.RUN_ONCE);
		assertEquals(r1.WAIT_PERIOD, r2.WAIT_PERIOD);
		assertEquals(r1.WORKSPACE, r2.WORKSPACE);
		
		assertEquals(r3.GIT_URL, r2.GIT_URL);
		assertEquals(r3.RUN_ONCE, r2.RUN_ONCE);
		assertEquals(r3.WAIT_PERIOD, r2.WAIT_PERIOD);
		assertEquals(r3.WORKSPACE, r2.WORKSPACE);
	}

	@Test
	public void builderWorkspace() {
		RssConfig r1 = builder.build();
		RssConfig r2 = builder.setWorkspace("non-default").build();
		RssConfig r3 = builder.setWorkspace(null).build();
		
		assertNotEquals(r1.WORKSPACE, r2.WORKSPACE);
		assertEquals(r2.WORKSPACE, r3.WORKSPACE);
		
		assertEquals(r1.GIT_URL, r2.GIT_URL);
		assertEquals(r1.RUN_ONCE, r2.RUN_ONCE);
		assertEquals(r1.WAIT_PERIOD, r2.WAIT_PERIOD);
		assertEquals(r1.FEED_URL, r2.FEED_URL);
		
		assertEquals(r3.GIT_URL, r2.GIT_URL);
		assertEquals(r3.RUN_ONCE, r2.RUN_ONCE);
		assertEquals(r3.WAIT_PERIOD, r2.WAIT_PERIOD);
		assertEquals(r3.FEED_URL, r2.FEED_URL);
	}

	@Test
	public void builderRunOnce() {
		RssConfig r1 = builder.build();
		RssConfig r2 = builder.setRunOnce(!r1.RUN_ONCE).build();
		RssConfig r3 = builder.setRunOnce(null).build();
		
		assertNotEquals(r1.RUN_ONCE, r2.RUN_ONCE);
		assertEquals(r2.RUN_ONCE, r3.RUN_ONCE);
		
		assertEquals(r1.GIT_URL, r2.GIT_URL);
		assertEquals(r1.WORKSPACE, r2.WORKSPACE);
		assertEquals(r1.WAIT_PERIOD, r2.WAIT_PERIOD);
		assertEquals(r1.FEED_URL, r2.FEED_URL);
		
		assertEquals(r3.GIT_URL, r2.GIT_URL);
		assertEquals(r3.WORKSPACE, r2.WORKSPACE);
		assertEquals(r3.WAIT_PERIOD, r2.WAIT_PERIOD);
		assertEquals(r3.FEED_URL, r2.FEED_URL);
	}

	@Test
	public void builderWaitPeriod() {
		RssConfig r1 = builder.build();
		RssConfig r2 = builder.setWaitPeriod(r1.WAIT_PERIOD + 40L).build();
		RssConfig r3 = builder.setRunOnce(null).build();
		
		assertNotEquals(r1.WAIT_PERIOD, r2.WAIT_PERIOD);
		assertEquals(r2.WAIT_PERIOD, r3.WAIT_PERIOD);
		
		assertEquals(r1.GIT_URL, r2.GIT_URL);
		assertEquals(r1.WORKSPACE, r2.WORKSPACE);
		assertEquals(r1.RUN_ONCE, r2.RUN_ONCE);
		assertEquals(r1.FEED_URL, r2.FEED_URL);
		
		assertEquals(r3.GIT_URL, r2.GIT_URL);
		assertEquals(r3.WORKSPACE, r2.WORKSPACE);
		assertEquals(r3.RUN_ONCE, r2.RUN_ONCE);
		assertEquals(r3.FEED_URL, r2.FEED_URL);
	}

	private File setUpConfigFile(String content) throws IOException {
		File f = testFolder.newFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.write(content);
		bw.close();

		return f;
	}
}
