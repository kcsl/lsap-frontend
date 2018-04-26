package edu.iastate.sdmay1809;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;

import edu.iastate.sdmay1809.shared.DiffConfig;

public class DataTranslatorMain {
	public static void main(String[] args) throws JSONException, IOException, Exception {
		DiffConfig config = DiffConfig.builder(DiffConfig.Builder.class, args).build();
		
		DataBaseFileTranslator dt = new DataBaseFileTranslator(config.RESULT_DIR, config.NEW_TAG);
		File output = new File(config.DIFF_TEST_DIR);
		dt.run(output, false);
		
	}
}
