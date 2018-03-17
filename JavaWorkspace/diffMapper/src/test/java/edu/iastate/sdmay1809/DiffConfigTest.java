package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import edu.iastate.sdmay1809.DiffConfig.DiffConfigBuilder;

public class DiffConfigTest {
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void diffConfigConstructs() {
		DiffConfigBuilder base = DiffConfig.builder();
		assertThat(DiffConfig.builder().build(), instanceOf(DiffConfig.class));
		assertThat(DiffConfig.builder(base).build(), instanceOf(DiffConfig.class));
		assertThat(DiffConfig.builder("config.json").build(), instanceOf(DiffConfig.class));
	}
	
	@Test
	public void diffConfigDefaultConstructor() {
		DiffConfig config = DiffConfig.builder().build();
		
		assertNotNull(config.OLD_TAG);
		assertNotNull(config.NEW_TAG);
		assertNotNull(config.DIFF_TEST_DIR);
		assertNotNull(config.KERNEL_DIR);
		assertNotNull(config.RESULT_DIR);
		assertNotNull(config.TYPES);
	}
	
	@Test
	public void diffConfigBaseDifferentOldTag() {
		String nonDefault = "non-default";
		DiffConfigBuilder baseBuilder = DiffConfig.builder();
		DiffConfig base = baseBuilder.build();
		DiffConfig nonDefConfig = DiffConfig.builder(baseBuilder).setOldTag(nonDefault).build();
		DiffConfig nonDefConfigNull = DiffConfig.builder(baseBuilder).setOldTag(null).build();
		
		assertNotEquals(base.OLD_TAG, nonDefConfig.OLD_TAG);
		assertEquals(base.OLD_TAG, nonDefConfigNull.OLD_TAG);
		
		assertEquals(base.NEW_TAG, nonDefConfig.NEW_TAG);
		assertEquals(base.NEW_TAG, nonDefConfigNull.NEW_TAG);
		
		assertEquals(base.DIFF_TEST_DIR, nonDefConfig.DIFF_TEST_DIR);
		assertEquals(base.DIFF_TEST_DIR, nonDefConfigNull.DIFF_TEST_DIR);
		
		assertEquals(base.KERNEL_DIR, nonDefConfig.KERNEL_DIR);
		assertEquals(base.KERNEL_DIR, nonDefConfigNull.KERNEL_DIR);
		
		assertEquals(base.RESULT_DIR, nonDefConfig.RESULT_DIR);
		assertEquals(base.RESULT_DIR, nonDefConfigNull.RESULT_DIR);
		
		assertArrayEquals(base.TYPES, nonDefConfig.TYPES);
		assertArrayEquals(base.TYPES, nonDefConfigNull.TYPES);
	}
	
	@Test
	public void diffConfigBaseDifferentNewTag() {
		String nonDefault = "non-default";
		DiffConfigBuilder baseBuilder = DiffConfig.builder();
		DiffConfig base = baseBuilder.build();
		DiffConfig nonDefConfig = DiffConfig.builder(baseBuilder).setNewTag(nonDefault).build();
		DiffConfig nonDefConfigNull = DiffConfig.builder(baseBuilder).setNewTag(null).build();
		
		assertEquals(base.OLD_TAG, nonDefConfig.OLD_TAG);
		assertEquals(base.OLD_TAG, nonDefConfigNull.OLD_TAG);
		
		assertNotEquals(base.NEW_TAG, nonDefConfig.NEW_TAG);
		assertEquals(base.NEW_TAG, nonDefConfigNull.NEW_TAG);
		
		assertEquals(base.DIFF_TEST_DIR, nonDefConfig.DIFF_TEST_DIR);
		assertEquals(base.DIFF_TEST_DIR, nonDefConfigNull.DIFF_TEST_DIR);
		
		assertEquals(base.KERNEL_DIR, nonDefConfig.KERNEL_DIR);
		assertEquals(base.KERNEL_DIR, nonDefConfigNull.KERNEL_DIR);
		
		assertEquals(base.RESULT_DIR, nonDefConfig.RESULT_DIR);
		assertEquals(base.RESULT_DIR, nonDefConfigNull.RESULT_DIR);
		
		assertArrayEquals(base.TYPES, nonDefConfig.TYPES);
		assertArrayEquals(base.TYPES, nonDefConfigNull.TYPES);
	}
	
	@Test
	public void diffConfigBaseDifferentDiffTestDir() {
		String nonDefault = "non-default";
		DiffConfigBuilder baseBuilder = DiffConfig.builder();
		DiffConfig base = baseBuilder.build();
		DiffConfig nonDefConfig = DiffConfig.builder(baseBuilder).setDiffTestDir(nonDefault).build();
		DiffConfig nonDefConfigNull = DiffConfig.builder(baseBuilder).setDiffTestDir(null).build();
		
		assertEquals(base.OLD_TAG, nonDefConfig.OLD_TAG);
		assertEquals(base.OLD_TAG, nonDefConfigNull.OLD_TAG);
		
		assertEquals(base.NEW_TAG, nonDefConfig.NEW_TAG);
		assertEquals(base.NEW_TAG, nonDefConfigNull.NEW_TAG);
		
		assertNotEquals(base.DIFF_TEST_DIR, nonDefConfig.DIFF_TEST_DIR);
		assertEquals(base.DIFF_TEST_DIR, nonDefConfigNull.DIFF_TEST_DIR);
		
		assertEquals(base.KERNEL_DIR, nonDefConfig.KERNEL_DIR);
		assertEquals(base.KERNEL_DIR, nonDefConfigNull.KERNEL_DIR);
		
		assertEquals(base.RESULT_DIR, nonDefConfig.RESULT_DIR);
		assertEquals(base.RESULT_DIR, nonDefConfigNull.RESULT_DIR);
		
		assertArrayEquals(base.TYPES, nonDefConfig.TYPES);
		assertArrayEquals(base.TYPES, nonDefConfigNull.TYPES);
	}
	
	@Test
	public void diffConfigBaseDifferentKernelDir() {
		String nonDefault = "non-default";
		DiffConfigBuilder baseBuilder = DiffConfig.builder();
		DiffConfig base = baseBuilder.build();
		DiffConfig nonDefConfig = DiffConfig.builder(baseBuilder).setKernelDir(nonDefault).build();
		DiffConfig nonDefConfigNull = DiffConfig.builder(baseBuilder).setKernelDir(null).build();
		
		assertEquals(base.OLD_TAG, nonDefConfig.OLD_TAG);
		assertEquals(base.OLD_TAG, nonDefConfigNull.OLD_TAG);
		
		assertEquals(base.NEW_TAG, nonDefConfig.NEW_TAG);
		assertEquals(base.NEW_TAG, nonDefConfigNull.NEW_TAG);
		
		assertEquals(base.DIFF_TEST_DIR, nonDefConfig.DIFF_TEST_DIR);
		assertEquals(base.DIFF_TEST_DIR, nonDefConfigNull.DIFF_TEST_DIR);
		
		assertNotEquals(base.KERNEL_DIR, nonDefConfig.KERNEL_DIR);
		assertEquals(base.KERNEL_DIR, nonDefConfigNull.KERNEL_DIR);
		
		assertEquals(base.RESULT_DIR, nonDefConfig.RESULT_DIR);
		assertEquals(base.RESULT_DIR, nonDefConfigNull.RESULT_DIR);
		
		assertArrayEquals(base.TYPES, nonDefConfig.TYPES);
		assertArrayEquals(base.TYPES, nonDefConfigNull.TYPES);
	}
	
	@Test
	public void diffConfigBaseDifferentResultDir() {
		String nonDefault = "non-default";
		DiffConfigBuilder baseBuilder = DiffConfig.builder();
		DiffConfig base = baseBuilder.build();
		DiffConfig nonDefConfig = DiffConfig.builder(baseBuilder).setResultDir(nonDefault).build();
		DiffConfig nonDefConfigNull = DiffConfig.builder(baseBuilder).setResultDir(null).build();
		
		assertEquals(base.OLD_TAG, nonDefConfig.OLD_TAG);
		assertEquals(base.OLD_TAG, nonDefConfigNull.OLD_TAG);
		
		assertEquals(base.NEW_TAG, nonDefConfig.NEW_TAG);
		assertEquals(base.NEW_TAG, nonDefConfigNull.NEW_TAG);
		
		assertEquals(base.DIFF_TEST_DIR, nonDefConfig.DIFF_TEST_DIR);
		assertEquals(base.DIFF_TEST_DIR, nonDefConfigNull.DIFF_TEST_DIR);
		
		assertEquals(base.KERNEL_DIR, nonDefConfig.KERNEL_DIR);
		assertEquals(base.KERNEL_DIR, nonDefConfigNull.KERNEL_DIR);
		
		assertNotEquals(base.RESULT_DIR, nonDefConfig.RESULT_DIR);
		assertEquals(base.RESULT_DIR, nonDefConfigNull.RESULT_DIR);
		
		assertArrayEquals(base.TYPES, nonDefConfig.TYPES);
		assertArrayEquals(base.TYPES, nonDefConfigNull.TYPES);
	}
	
	@Test
	public void diffConfigBaseDifferentTypes() {
		String[] nonDefault = {"non", "default"};
		DiffConfigBuilder baseBuilder = DiffConfig.builder();
		DiffConfig base = baseBuilder.build();
		DiffConfig nonDefConfig = DiffConfig.builder(baseBuilder).setTypes(nonDefault).build();
		DiffConfig nonDefConfigNull = DiffConfig.builder(baseBuilder).setTypes(null).build();
		
		assertEquals(base.OLD_TAG, nonDefConfig.OLD_TAG);
		assertEquals(base.OLD_TAG, nonDefConfigNull.OLD_TAG);
		
		assertEquals(base.NEW_TAG, nonDefConfig.NEW_TAG);
		assertEquals(base.NEW_TAG, nonDefConfigNull.NEW_TAG);
		
		assertEquals(base.DIFF_TEST_DIR, nonDefConfig.DIFF_TEST_DIR);
		assertEquals(base.DIFF_TEST_DIR, nonDefConfigNull.DIFF_TEST_DIR);
		
		assertEquals(base.KERNEL_DIR, nonDefConfig.KERNEL_DIR);
		assertEquals(base.KERNEL_DIR, nonDefConfigNull.KERNEL_DIR);
		
		assertEquals(base.RESULT_DIR, nonDefConfig.RESULT_DIR);
		assertEquals(base.RESULT_DIR, nonDefConfigNull.RESULT_DIR);
		
		assertThat(nonDefConfig.TYPES, not(equalTo(base.TYPES)));
		assertArrayEquals(base.TYPES, nonDefConfigNull.TYPES);
	}
	
	@Test
	public void diffConfigFileConstructsDefault() {
		DiffConfig base = DiffConfig.builder().build();
		DiffConfig fileConfig = DiffConfig.builder("not_a_file_name.json").build();
		
		assertEquals(base.OLD_TAG, fileConfig.OLD_TAG);
		assertEquals(base.NEW_TAG, fileConfig.NEW_TAG);
		assertEquals(base.DIFF_TEST_DIR, fileConfig.DIFF_TEST_DIR);
		assertEquals(base.KERNEL_DIR, fileConfig.KERNEL_DIR);
		assertEquals(base.RESULT_DIR, fileConfig.RESULT_DIR);
		assertArrayEquals(base.TYPES, fileConfig.TYPES);
	}
	
	@Test
	public void diffConfigFileConstructsOldTag() throws IOException {
		String fullPath = setUpConfigFile("{\n\"old_tag\": \"my_old_tag\"\n}\n");
		
		DiffConfig defaultConfig = DiffConfig.builder().build();
		DiffConfig fileConfig = DiffConfig.builder(fullPath).build();
		
		assertEquals("my_old_tag", fileConfig.OLD_TAG);
		
		assertNotEquals(defaultConfig.OLD_TAG, fileConfig.OLD_TAG);
		assertEquals(defaultConfig.NEW_TAG, fileConfig.NEW_TAG);
		assertEquals(defaultConfig.DIFF_TEST_DIR, fileConfig.DIFF_TEST_DIR);
		assertEquals(defaultConfig.KERNEL_DIR, fileConfig.KERNEL_DIR);
		assertEquals(defaultConfig.RESULT_DIR, fileConfig.RESULT_DIR);
		assertArrayEquals(defaultConfig.TYPES, fileConfig.TYPES);
	}
	
	@Test
	public void diffConfigFileConstructsNewTag() throws IOException {
		String fullPath = setUpConfigFile("{\n\"new_tag\": \"my_new_tag\"\n}\n");
		
		DiffConfig defaultConfig = DiffConfig.builder().build();
		DiffConfig fileConfig = DiffConfig.builder(fullPath).build();
		
		assertEquals("my_new_tag", fileConfig.NEW_TAG);
		
		assertEquals(defaultConfig.OLD_TAG, fileConfig.OLD_TAG);
		assertNotEquals(defaultConfig.NEW_TAG, fileConfig.NEW_TAG);
		assertEquals(defaultConfig.DIFF_TEST_DIR, fileConfig.DIFF_TEST_DIR);
		assertEquals(defaultConfig.KERNEL_DIR, fileConfig.KERNEL_DIR);
		assertEquals(defaultConfig.RESULT_DIR, fileConfig.RESULT_DIR);
		assertArrayEquals(defaultConfig.TYPES, fileConfig.TYPES);
	}
	
	@Test
	public void diffConfigFileConstructsDiffTestDir() throws IOException {
		String fullPath = setUpConfigFile("{\n\"diff_test_dir\": \"my_diff_test_dir\"\n}\n");
		
		DiffConfig defaultConfig = DiffConfig.builder().build();
		DiffConfig fileConfig = DiffConfig.builder(fullPath).build();
		
		assertEquals("my_diff_test_dir", fileConfig.DIFF_TEST_DIR);
		
		assertEquals(defaultConfig.OLD_TAG, fileConfig.OLD_TAG);
		assertEquals(defaultConfig.NEW_TAG, fileConfig.NEW_TAG);
		assertNotEquals(defaultConfig.DIFF_TEST_DIR, fileConfig.DIFF_TEST_DIR);
		assertEquals(defaultConfig.KERNEL_DIR, fileConfig.KERNEL_DIR);
		assertEquals(defaultConfig.RESULT_DIR, fileConfig.RESULT_DIR);
		assertArrayEquals(defaultConfig.TYPES, fileConfig.TYPES);
	}
	
	@Test
	public void diffConfigFileConstructsKernelDir() throws IOException {
		String fullPath = setUpConfigFile("{\n\"kernel_dir\": \"my_kernel_dir\"\n}\n");
		
		DiffConfig defaultConfig = DiffConfig.builder().build();
		DiffConfig fileConfig = DiffConfig.builder(fullPath).build();
		
		assertEquals("my_kernel_dir", fileConfig.KERNEL_DIR);
		
		assertEquals(defaultConfig.OLD_TAG, fileConfig.OLD_TAG);
		assertEquals(defaultConfig.NEW_TAG, fileConfig.NEW_TAG);
		assertEquals(defaultConfig.DIFF_TEST_DIR, fileConfig.DIFF_TEST_DIR);
		assertNotEquals(defaultConfig.KERNEL_DIR, fileConfig.KERNEL_DIR);
		assertEquals(defaultConfig.RESULT_DIR, fileConfig.RESULT_DIR);
		assertArrayEquals(defaultConfig.TYPES, fileConfig.TYPES);
	}
	
	@Test
	public void diffConfigFileConstructsResultDir() throws IOException {
		String fullPath = setUpConfigFile("{\n\"result_dir\": \"my_result_dir\"\n}\n");
		
		DiffConfig defaultConfig = DiffConfig.builder().build();
		DiffConfig fileConfig = DiffConfig.builder(fullPath).build();
		
		assertEquals("my_result_dir", fileConfig.RESULT_DIR);
		
		assertEquals(defaultConfig.OLD_TAG, fileConfig.OLD_TAG);
		assertEquals(defaultConfig.NEW_TAG, fileConfig.NEW_TAG);
		assertEquals(defaultConfig.DIFF_TEST_DIR, fileConfig.DIFF_TEST_DIR);
		assertEquals(defaultConfig.KERNEL_DIR, fileConfig.KERNEL_DIR);
		assertNotEquals(defaultConfig.RESULT_DIR, fileConfig.RESULT_DIR);
		assertArrayEquals(defaultConfig.TYPES, fileConfig.TYPES);
	}
	
	@Test
	public void diffConfigFileConstructsTypes() throws IOException {
		String fullPath = setUpConfigFile("{\n\"types\": [\"type1\", \"type2\"]\n}\n");
		
		DiffConfig defaultConfig = DiffConfig.builder().build();
		DiffConfig fileConfig = DiffConfig.builder(fullPath).build();
		
		assertArrayEquals(new String[] {"type1", "type2"}, fileConfig.TYPES);
		
		assertEquals(defaultConfig.OLD_TAG, fileConfig.OLD_TAG);
		assertEquals(defaultConfig.NEW_TAG, fileConfig.NEW_TAG);
		assertEquals(defaultConfig.DIFF_TEST_DIR, fileConfig.DIFF_TEST_DIR);
		assertEquals(defaultConfig.KERNEL_DIR, fileConfig.KERNEL_DIR);
		assertEquals(defaultConfig.RESULT_DIR, fileConfig.RESULT_DIR);
		assertThat(fileConfig.TYPES, not(equalTo(defaultConfig.TYPES)));
	}
	
	private String setUpConfigFile(String content) throws IOException {
		String cwd = System.getProperty("user.dir");
		String rewind = cwd.replaceAll("(?:\\/(?:\\w|-)+)", "../");
		File f = testFolder.newFile();
		String fullPath = rewind + f.getAbsolutePath().substring(1);

		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.write(content);
		bw.close();
		
		return fullPath;
	}
}
