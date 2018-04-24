package edu.iastate.sdmay1809.shared;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.rules.TemporaryFolder;

public class DiffConfigTest {
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Test
	public void diffConfigConstructs() {
		DiffConfig.Builder base = Config.builder(DiffConfig.Builder.class);
		assertThat(Config.builder(DiffConfig.Builder.class).build(), instanceOf(DiffConfig.class));
		assertThat(Config.builder(DiffConfig.Builder.class).build(), instanceOf(DiffConfig.class));
		assertThat(base.build(), instanceOf(DiffConfig.class));
		assertThat(Config.builder(DiffConfig.Builder.class, Paths.get("config.json").toFile()).build(),
				instanceOf(DiffConfig.class));
	}

	@Test
	public void diffConfigDefaultConstructor() {
		DiffConfig config = DiffConfig.builder(DiffConfig.Builder.class).build();

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
		DiffConfig.Builder baseBuilder = DiffConfig.builder(DiffConfig.Builder.class);
		DiffConfig base = baseBuilder.build();
		DiffConfig nonDefConfig = DiffConfig.builder(DiffConfig.Builder.class, baseBuilder).setOldTag(nonDefault)
				.build();
		DiffConfig nonDefConfigNull = DiffConfig.builder(DiffConfig.Builder.class, baseBuilder).setOldTag(null).build();

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
		DiffConfig.Builder baseBuilder = DiffConfig.builder(DiffConfig.Builder.class);
		DiffConfig base = baseBuilder.build();
		DiffConfig nonDefConfig = DiffConfig.builder(DiffConfig.Builder.class, baseBuilder).setNewTag(nonDefault)
				.build();
		DiffConfig nonDefConfigNull = DiffConfig.builder(DiffConfig.Builder.class, baseBuilder).setNewTag(null).build();

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
		DiffConfig.Builder baseBuilder = DiffConfig.builder(DiffConfig.Builder.class);
		DiffConfig base = baseBuilder.build();
		DiffConfig nonDefConfig = DiffConfig.builder(DiffConfig.Builder.class, baseBuilder).setDiffTestDir(nonDefault)
				.build();
		DiffConfig nonDefConfigNull = DiffConfig.builder(DiffConfig.Builder.class, baseBuilder).setDiffTestDir(null)
				.build();

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
		DiffConfig.Builder baseBuilder = DiffConfig.builder(DiffConfig.Builder.class);
		DiffConfig base = baseBuilder.build();
		DiffConfig nonDefConfig = DiffConfig.builder(DiffConfig.Builder.class, baseBuilder).setKernelDir(nonDefault)
				.build();
		DiffConfig nonDefConfigNull = DiffConfig.builder(DiffConfig.Builder.class, baseBuilder).setKernelDir(null)
				.build();

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
		DiffConfig.Builder baseBuilder = DiffConfig.builder(DiffConfig.Builder.class);
		DiffConfig base = baseBuilder.build();
		DiffConfig nonDefConfig = DiffConfig.builder(DiffConfig.Builder.class, baseBuilder).setResultDir(nonDefault)
				.build();
		DiffConfig nonDefConfigNull = DiffConfig.builder(DiffConfig.Builder.class, baseBuilder).setResultDir(null)
				.build();

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
		String[] nonDefault = { "non", "default" };
		DiffConfig.Builder baseBuilder = DiffConfig.builder(DiffConfig.Builder.class);
		DiffConfig base = baseBuilder.build();
		DiffConfig nonDefConfig = DiffConfig.builder(DiffConfig.Builder.class, baseBuilder).setTypes(nonDefault)
				.build();
		DiffConfig nonDefConfigNull = DiffConfig.builder(DiffConfig.Builder.class, baseBuilder).setTypes(null).build();

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
		DiffConfig base = DiffConfig.builder(DiffConfig.Builder.class).build();
		DiffConfig fileConfig = DiffConfig.builder(DiffConfig.Builder.class, new File("not_a_file_name.json")).build();

		assertEquals(base.OLD_TAG, fileConfig.OLD_TAG);
		assertEquals(base.NEW_TAG, fileConfig.NEW_TAG);
		assertEquals(base.DIFF_TEST_DIR, fileConfig.DIFF_TEST_DIR);
		assertEquals(base.KERNEL_DIR, fileConfig.KERNEL_DIR);
		assertEquals(base.RESULT_DIR, fileConfig.RESULT_DIR);
		assertArrayEquals(base.TYPES, fileConfig.TYPES);
	}

	@Test
	public void diffConfigFileConstructsOldTag() throws IOException {
		File fullPath = setUpConfigFile("{\n\"old_tag\": \"my_old_tag\"\n}\n");

		DiffConfig defaultConfig = DiffConfig.builder(DiffConfig.Builder.class).build();
		DiffConfig fileConfig = DiffConfig.builder(DiffConfig.Builder.class, fullPath).build();

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
		File fullPath = setUpConfigFile("{\n\"new_tag\": \"my_new_tag\"\n}\n");

		DiffConfig defaultConfig = DiffConfig.builder(DiffConfig.Builder.class).build();
		DiffConfig fileConfig = DiffConfig.builder(DiffConfig.Builder.class, fullPath).build();

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
		File fullPath = setUpConfigFile("{\n\"diff_test_dir\": \"my_diff_test_dir\"\n}\n");

		DiffConfig defaultConfig = DiffConfig.builder(DiffConfig.Builder.class).build();
		DiffConfig fileConfig = DiffConfig.builder(DiffConfig.Builder.class, fullPath).build();

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
		File fullPath = setUpConfigFile("{\n\"kernel_dir\": \"my_kernel_dir\"\n}\n");

		DiffConfig defaultConfig = DiffConfig.builder(DiffConfig.Builder.class).build();
		DiffConfig fileConfig = DiffConfig.builder(DiffConfig.Builder.class, fullPath).build();

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
		File fullPath = setUpConfigFile("{\n\"result_dir\": \"my_result_dir\"\n}\n");

		DiffConfig defaultConfig = DiffConfig.builder(DiffConfig.Builder.class).build();
		DiffConfig fileConfig = DiffConfig.builder(DiffConfig.Builder.class, fullPath).build();

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
		File fullPath = setUpConfigFile("{\n\"types\": [\"type1\", \"type2\"]\n}\n");

		DiffConfig defaultConfig = DiffConfig.builder(DiffConfig.Builder.class).build();
		DiffConfig fileConfig = DiffConfig.builder(DiffConfig.Builder.class, fullPath).build();

		assertArrayEquals(new String[] { "type1", "type2" }, fileConfig.TYPES);

		assertEquals(defaultConfig.OLD_TAG, fileConfig.OLD_TAG);
		assertEquals(defaultConfig.NEW_TAG, fileConfig.NEW_TAG);
		assertEquals(defaultConfig.DIFF_TEST_DIR, fileConfig.DIFF_TEST_DIR);
		assertEquals(defaultConfig.KERNEL_DIR, fileConfig.KERNEL_DIR);
		assertEquals(defaultConfig.RESULT_DIR, fileConfig.RESULT_DIR);
		assertThat(fileConfig.TYPES, not(equalTo(defaultConfig.TYPES)));
	}

	@Test
	public void diffConfigArgsConstructsDefault() {
		String[] args = { "command" };

		DiffConfig defaultConfig = DiffConfig.builder(DiffConfig.Builder.class).build();
		DiffConfig argsConfig = DiffConfig.builder(DiffConfig.Builder.class, args).build();

		assertEquals(defaultConfig.OLD_TAG, argsConfig.OLD_TAG);
		assertEquals(defaultConfig.NEW_TAG, argsConfig.NEW_TAG);
		assertEquals(defaultConfig.DIFF_TEST_DIR, argsConfig.DIFF_TEST_DIR);
		assertEquals(defaultConfig.KERNEL_DIR, argsConfig.KERNEL_DIR);
		assertEquals(defaultConfig.RESULT_DIR, argsConfig.RESULT_DIR);
		assertArrayEquals(defaultConfig.TYPES, argsConfig.TYPES);
	}

	@Test
	public void diffConfigArgsConstructsOldTag() {
		String[] args = { "command", "-Dold_tag=cli_arg_value" };

		DiffConfig defaultConfig = DiffConfig.builder(DiffConfig.Builder.class).build();
		DiffConfig argsConfig = DiffConfig.builder(DiffConfig.Builder.class, args).build();

		assertEquals("cli_arg_value", argsConfig.OLD_TAG);

		assertNotEquals(defaultConfig.OLD_TAG, argsConfig.OLD_TAG);
		assertEquals(defaultConfig.NEW_TAG, argsConfig.NEW_TAG);
		assertEquals(defaultConfig.DIFF_TEST_DIR, argsConfig.DIFF_TEST_DIR);
		assertEquals(defaultConfig.KERNEL_DIR, argsConfig.KERNEL_DIR);
		assertEquals(defaultConfig.RESULT_DIR, argsConfig.RESULT_DIR);
		assertArrayEquals(defaultConfig.TYPES, argsConfig.TYPES);
	}

	@Test
	public void diffConfigArgsConstructsNewTag() {
		String[] args = { "command", "-Dnew_tag=cli_arg_value" };

		DiffConfig defaultConfig = DiffConfig.builder(DiffConfig.Builder.class).build();
		DiffConfig argsConfig = DiffConfig.builder(DiffConfig.Builder.class, args).build();

		assertEquals("cli_arg_value", argsConfig.NEW_TAG);

		assertEquals(defaultConfig.OLD_TAG, argsConfig.OLD_TAG);
		assertNotEquals(defaultConfig.NEW_TAG, argsConfig.NEW_TAG);
		assertEquals(defaultConfig.DIFF_TEST_DIR, argsConfig.DIFF_TEST_DIR);
		assertEquals(defaultConfig.KERNEL_DIR, argsConfig.KERNEL_DIR);
		assertEquals(defaultConfig.RESULT_DIR, argsConfig.RESULT_DIR);
		assertArrayEquals(defaultConfig.TYPES, argsConfig.TYPES);
	}

	@Test
	public void diffConfigArgsConstructsDiffTestDir() {
		String[] args = { "command", "-Ddiff_test_dir=cli_arg_value" };

		DiffConfig defaultConfig = DiffConfig.builder(DiffConfig.Builder.class).build();
		DiffConfig argsConfig = DiffConfig.builder(DiffConfig.Builder.class, args).build();

		assertEquals("cli_arg_value", argsConfig.DIFF_TEST_DIR);

		assertEquals(defaultConfig.OLD_TAG, argsConfig.OLD_TAG);
		assertEquals(defaultConfig.NEW_TAG, argsConfig.NEW_TAG);
		assertNotEquals(defaultConfig.DIFF_TEST_DIR, argsConfig.DIFF_TEST_DIR);
		assertEquals(defaultConfig.KERNEL_DIR, argsConfig.KERNEL_DIR);
		assertEquals(defaultConfig.RESULT_DIR, argsConfig.RESULT_DIR);
		assertArrayEquals(defaultConfig.TYPES, argsConfig.TYPES);
	}

	@Test
	public void diffConfigArgsConstructsKernelDir() {
		String[] args = { "command", "-Dkernel_dir=cli_arg_value" };

		DiffConfig defaultConfig = DiffConfig.builder(DiffConfig.Builder.class).build();
		DiffConfig argsConfig = DiffConfig.builder(DiffConfig.Builder.class, args).build();

		assertEquals("cli_arg_value", argsConfig.KERNEL_DIR);

		assertEquals(defaultConfig.OLD_TAG, argsConfig.OLD_TAG);
		assertEquals(defaultConfig.NEW_TAG, argsConfig.NEW_TAG);
		assertEquals(defaultConfig.DIFF_TEST_DIR, argsConfig.DIFF_TEST_DIR);
		assertNotEquals(defaultConfig.KERNEL_DIR, argsConfig.KERNEL_DIR);
		assertEquals(defaultConfig.RESULT_DIR, argsConfig.RESULT_DIR);
		assertArrayEquals(defaultConfig.TYPES, argsConfig.TYPES);
	}

	@Test
	public void diffConfigArgsConstructsResultDir() {
		String[] args = { "command", "-Dresult_dir=cli_arg_value" };

		DiffConfig defaultConfig = DiffConfig.builder(DiffConfig.Builder.class).build();
		DiffConfig argsConfig = DiffConfig.builder(DiffConfig.Builder.class, args).build();

		assertEquals("cli_arg_value", argsConfig.RESULT_DIR);

		assertEquals(defaultConfig.OLD_TAG, argsConfig.OLD_TAG);
		assertEquals(defaultConfig.NEW_TAG, argsConfig.NEW_TAG);
		assertEquals(defaultConfig.DIFF_TEST_DIR, argsConfig.DIFF_TEST_DIR);
		assertEquals(defaultConfig.KERNEL_DIR, argsConfig.KERNEL_DIR);
		assertNotEquals(defaultConfig.RESULT_DIR, argsConfig.RESULT_DIR);
		assertArrayEquals(defaultConfig.TYPES, argsConfig.TYPES);
	}

	@Test
	public void diffConfigArgsConstructsTypes() {
		String[] args = { "command", "-Dtypes.0=cli_arg_value1", "-Dtypes.1=cli_arg_value2" };

		DiffConfig defaultConfig = DiffConfig.builder(DiffConfig.Builder.class).build();
		DiffConfig argsConfig = DiffConfig.builder(DiffConfig.Builder.class, args).build();

		assertArrayEquals(new String[] { "cli_arg_value1", "cli_arg_value2" }, argsConfig.TYPES);

		assertEquals(defaultConfig.OLD_TAG, argsConfig.OLD_TAG);
		assertEquals(defaultConfig.NEW_TAG, argsConfig.NEW_TAG);
		assertEquals(defaultConfig.DIFF_TEST_DIR, argsConfig.DIFF_TEST_DIR);
		assertEquals(defaultConfig.KERNEL_DIR, argsConfig.KERNEL_DIR);
		assertEquals(defaultConfig.RESULT_DIR, argsConfig.RESULT_DIR);
		assertThat(argsConfig.TYPES, not(equalTo(defaultConfig.TYPES)));
	}

	@Test
	public void diffConfigArgsExitsForDryRun() {
		exit.expectSystemExitWithStatus(0);

		String[] args = { "command", "-n" };
		DiffConfig.builder(DiffConfig.Builder.class, args).build();
	}

	@Test
	public void diffConfigArgsExitsForHelp() {
		exit.expectSystemExitWithStatus(0);

		String[] args = { "command", "-h" };
		DiffConfig.builder(DiffConfig.Builder.class, args).build();
	}

	@Test
	public void diffConfigArgsExitsForParseException() {
		exit.expectSystemExitWithStatus(0);

		String[] args = { "command", "-D" };
		DiffConfig.builder(DiffConfig.Builder.class, args).build();
	}

	@Test
	public void diffConfigArgsImportsFromFile() throws IOException {
		File fullPath = setUpConfigFile("{\"old_tag\": \"my_old_tag\",\"new_tag\": \"my_new_tag\","
				+ "\"diff_test_dir\": \"my_diff_test_dir\"," + "\"kernel_dir\": \"my_kernel_dir\","
				+ "\"result_dir\": \"my_result_dir\"," + "\"types\": [\"type1\", \"type2\"]\n}\n");

		String[] args = { "command", "-c" + fullPath.getAbsolutePath() };
		DiffConfig.Builder builder = DiffConfig.builder(DiffConfig.Builder.class, args);
		DiffConfig argsConfig = builder.build();

		assertEquals("my_old_tag", argsConfig.OLD_TAG);
		assertEquals("my_new_tag", argsConfig.NEW_TAG);
		assertEquals("my_diff_test_dir", argsConfig.DIFF_TEST_DIR);
		assertEquals("my_kernel_dir", argsConfig.KERNEL_DIR);
		assertEquals("my_result_dir", argsConfig.RESULT_DIR);
		assertArrayEquals(new String[] { "type1", "type2" }, argsConfig.TYPES);
	}

	@Test
	public void diffConfigArgsPropsOverrideFile() throws IOException {
		File fullPath = setUpConfigFile("{\"old_tag\": \"my_old_tag\",\"new_tag\": \"my_new_tag\","
				+ "\"diff_test_dir\": \"my_diff_test_dir\"," + "\"kernel_dir\": \"my_kernel_dir\","
				+ "\"result_dir\": \"my_result_dir\"," + "\"types\": [\"type1\", \"type2\"]\n}\n");

		String[] args = { "command", "-c" + fullPath.getAbsolutePath(), "-Dtypes.0=cli_arg_value1",
				"-Dtypes.1=cli_arg_value2", "-Dold_tag=cli_old_tag", "-Dnew_tag=cli_new_tag",
				"-Ddiff_test_dir=cli_diff_test_dir", "-Dkernel_dir=cli_kernel_dir", "-Dresult_dir=cli_result_dir" };
		DiffConfig argsConfig = DiffConfig.builder(DiffConfig.Builder.class, args).build();

		assertEquals("cli_old_tag", argsConfig.OLD_TAG);
		assertEquals("cli_new_tag", argsConfig.NEW_TAG);
		assertEquals("cli_diff_test_dir", argsConfig.DIFF_TEST_DIR);
		assertEquals("cli_kernel_dir", argsConfig.KERNEL_DIR);
		assertEquals("cli_result_dir", argsConfig.RESULT_DIR);
		assertArrayEquals(new String[] { "cli_arg_value1", "cli_arg_value2" }, argsConfig.TYPES);
	}

	private File setUpConfigFile(String content) throws IOException {
		File f = testFolder.newFile();

		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.write(content);
		bw.close();

		return f;
	}
}
