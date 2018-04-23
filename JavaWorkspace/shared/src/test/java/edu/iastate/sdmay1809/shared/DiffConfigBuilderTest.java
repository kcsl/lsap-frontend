package edu.iastate.sdmay1809.shared;

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

public class DiffConfigBuilderTest {
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();
	
	DiffConfig.Builder builder;
	
	@Before
	public void setUp() {
		builder = DiffConfig.builder(DiffConfig.Builder.class);
	}

	@Test
	public void builderDefaultConstructor() {
		// builder() always constructs a DiffConfigBuilder class
		assertThat(DiffConfig.builder(DiffConfig.Builder.class), instanceOf(DiffConfig.Builder.class));
	}
	
	@Test
	public void builderBaseConstructor() {
		// builder(base) builds the same DiffConfig that the base builder builds
		assertThat(DiffConfig.builder(DiffConfig.Builder.class, builder), instanceOf(DiffConfig.Builder.class));
		builder = builder
				.setOldTag("not_a_default_old_tag")
				.setNewTag("not_a_default_new_tag")
				.setDiffTestDir("not_a_default_diff_test_dir")
				.setKernelDir("not_a_kernel_dir")
				.setResultDir("not_a_default_result_dir")
				.setTypes(new String[] {"not", "a", "default", "type", "list"});
		
		DiffConfig base = builder.build();
		DiffConfig config = DiffConfig.builder(DiffConfig.Builder.class, builder).build();
		
		assertEquals(base.OLD_TAG, config.OLD_TAG);
		assertEquals(base.NEW_TAG, config.NEW_TAG);
		assertEquals(base.DIFF_TEST_DIR, config.DIFF_TEST_DIR);
		assertEquals(base.KERNEL_DIR, config.KERNEL_DIR);
		assertEquals(base.RESULT_DIR, config.RESULT_DIR);
		assertArrayEquals(base.TYPES, config.TYPES);
	}
	
	@Test
	public void builderConstructsWithSlashUserDirEnding() {
		String userDir = System.getProperty("user.dir");
		System.setProperty("user.dir", "test/");
		assertThat(DiffConfig.builder(DiffConfig.Builder.class), instanceOf(DiffConfig.Builder.class));
		System.setProperty("user.dir", userDir);
	}
	
	@Test
	public void builderOldTag() {
		String nonDefault = "non-default";
		String nonDefault2 = "non-default2";

		DiffConfig b2 = builder.setOldTag(nonDefault).build();
		DiffConfig b3 = builder.setOldTag(null).build();
		DiffConfig b4 = builder.setOldTag(nonDefault).setOldTag(null).setOldTag(nonDefault2).build();
		
		
		// setting a non-null value changes it
		assertEquals(nonDefault, b2.OLD_TAG);
		
		// setting a null value does not change it
		assertNotNull(b3.OLD_TAG);
		assertEquals(b2.OLD_TAG, b3.OLD_TAG);
		
		// The last parameter entry is used when chaining the same one.
		assertEquals(nonDefault2, b4.OLD_TAG);
		
	}
	
	@Test
	public void builderNewTag() {
		String nonDefault = "non-default";
		String nonDefault2 = "non-default2";

		DiffConfig b2 = builder.setNewTag(nonDefault).build();
		DiffConfig b3 = builder.setNewTag(null).build();
		DiffConfig b4 = builder.setNewTag(nonDefault).setNewTag(null).setNewTag(nonDefault2).build();
		
		// setting a non-null value changes it
		assertEquals(nonDefault, b2.NEW_TAG);
		
		// setting a null value does not change it
		assertNotNull(b3.NEW_TAG);
		assertEquals(b2.NEW_TAG, b3.NEW_TAG);
		
		// The last parameter entry is used when chaining the same one.
		assertEquals(nonDefault2, b4.NEW_TAG);
	}
	
	@Test
	public void builderDiffTestDir() {
		String nonDefault = "non-default";
		String nonDefault2 = "non-default2";
		
		DiffConfig b2 = builder.setDiffTestDir(nonDefault).build();
		DiffConfig b3 = builder.setDiffTestDir(null).build();
		DiffConfig b4 = builder.setDiffTestDir(nonDefault).setDiffTestDir(null).setDiffTestDir(nonDefault2).build();
		
		// setting a non-null value changes it
		assertEquals(nonDefault, b2.DIFF_TEST_DIR);
		
		// setting a null value does not change it
		assertNotNull(b3.DIFF_TEST_DIR);
		assertEquals(b2.DIFF_TEST_DIR, b3.DIFF_TEST_DIR);
		
		// The last parameter entry is used when chaining the same one.
		assertEquals(nonDefault2, b4.DIFF_TEST_DIR);
	}

	@Test
	public void builderKernelDir() {
		String nonDefault = "non-default";
		String nonDefault2 = "non-default2";

		DiffConfig b2 = builder.setKernelDir(nonDefault).build();
		DiffConfig b3 = builder.setKernelDir(null).build();
		DiffConfig b4 = builder.setKernelDir(nonDefault).setKernelDir(null).setKernelDir(nonDefault2).build();
		
		// setting a non-null value changes it
		assertEquals(nonDefault, b2.KERNEL_DIR);
		
		// setting a null value does not change it
		assertNotNull(b3.KERNEL_DIR);
		assertEquals(b2.KERNEL_DIR, b3.KERNEL_DIR);
		
		// The last parameter entry is used when chaining the same one.
		assertEquals(nonDefault2, b4.KERNEL_DIR);
	}
	
	@Test
	public void builderResultDir() {
		String nonDefault = "non-default";
		String nonDefault2 = "non-default2";

		DiffConfig b2 = builder.setResultDir(nonDefault).build();
		DiffConfig b3 = builder.setResultDir(null).build();
		DiffConfig b4 = builder.setResultDir(nonDefault).setResultDir(null).setResultDir(nonDefault2).build();
		
		// setting a non-null value changes it
		assertEquals(nonDefault, b2.RESULT_DIR);
		
		// setting a null value does not change it
		assertNotNull(b3.RESULT_DIR);
		assertEquals(b2.RESULT_DIR, b3.RESULT_DIR);
		
		// The last parameter entry is used when chaining the same one.
		assertEquals(nonDefault2, b4.RESULT_DIR);
	}
	
	@Test
	public void builderTypes() {
		String[] nonDefault = {"non", "default"};
		String[] nonDefault2 = {"non2", "default2"};
		
		DiffConfig b2 = builder.setTypes(nonDefault).build();
		DiffConfig b3 = builder.setTypes(null).build();
		DiffConfig b4 = builder.setTypes(nonDefault).setTypes(null).setTypes(nonDefault2).build();
		
		// setting a non-null value changes it
		assertArrayEquals(nonDefault, b2.TYPES);
		
		// setting a null value does not change it
		assertNotNull(b3.TYPES);
		assertArrayEquals(b2.TYPES, b3.TYPES);
		
		// The last parameter entry is used when chaining the same one.
		assertArrayEquals(nonDefault2, b4.TYPES);
	}
	
	@Test
	public void builderChaining() {
		String nonDefault1 = "non-default1";
		String nonDefault2 = "non-default2";
		
		DiffConfig b1 = builder.build();
		DiffConfig b2 = builder.setNewTag(null).setOldTag(null).build();
		DiffConfig b3 = builder.setNewTag(nonDefault1).setOldTag(nonDefault2).build();
		DiffConfig b4 = builder.setNewTag(null).setOldTag(nonDefault1).build();
		DiffConfig b5 = builder.setNewTag(nonDefault2).setOldTag(null).build();
		
		// Chaining nulls shouldn't affect values
		assertEquals(b1.NEW_TAG, b2.NEW_TAG);
		assertEquals(b1.OLD_TAG, b2.OLD_TAG);
		
		// Chaining non-nulls should affect values
		assertEquals(nonDefault1, b3.NEW_TAG);
		assertEquals(nonDefault2, b3.OLD_TAG);
		
		// Mixing non-null and null values should correctly adjust the non-null values
		assertEquals(nonDefault1, b4.NEW_TAG);
		assertEquals(nonDefault1, b4.OLD_TAG);
		assertEquals(nonDefault2, b5.NEW_TAG);
		assertEquals(nonDefault1, b5.OLD_TAG);
	}
	
	@Test
	public void builderFile() throws IOException {
		File fullPath = setUpConfigFile("\"old_tag\": \"my_old_tag\",\"new_tag\": \"my_new_tag\","
				+ "\"diff_test_dir\": \"my_diff_test_dir\"," + "\"kernel_dir\": \"my_kernel_dir\","
				+ "\"result_dir\": \"my_result_dir\"," + "\"types\": [\"type1\", \"type2\"]\n}\n");
		DiffConfig base = DiffConfig.builder(DiffConfig.Builder.class).build();
		DiffConfig file = DiffConfig.builder(DiffConfig.Builder.class, fullPath).build();
		
		assertEquals(base.OLD_TAG, file.OLD_TAG);
		assertEquals(base.NEW_TAG, file.NEW_TAG);
		assertEquals(base.DIFF_TEST_DIR, file.DIFF_TEST_DIR);
		assertEquals(base.KERNEL_DIR, file.KERNEL_DIR);
		assertEquals(base.RESULT_DIR, file.RESULT_DIR);
		assertArrayEquals(base.TYPES, file.TYPES);
	}
	
	@Test
	public void buildTest() {
		DiffConfig config = builder
		.setOldTag("not_a_default_old_tag")
		.setNewTag("not_a_default_new_tag")
		.setDiffTestDir("not_a_default_diff_test_dir")
		.setKernelDir("not_a_kernel_dir")
		.setResultDir("not_a_default_result_dir")
		.setTypes(new String[] {"not", "a", "default", "type", "list"})
		.build();
		
		assertEquals("not_a_default_old_tag", config.OLD_TAG);
		assertEquals("not_a_default_new_tag", config.NEW_TAG);
		assertEquals("not_a_default_diff_test_dir", config.DIFF_TEST_DIR);
		assertEquals("not_a_kernel_dir", config.KERNEL_DIR);
		assertEquals("not_a_default_result_dir", config.RESULT_DIR);
		assertArrayEquals(new String[] {"not", "a", "default", "type", "list"}, config.TYPES);
	}
	
	private File setUpConfigFile(String content) throws IOException {
		File f = testFolder.newFile();

		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.write(content);
		bw.close();

		return f;
	}
}
