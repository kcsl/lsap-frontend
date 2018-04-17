package edu.iastate.sdmay1809;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONException;

import edu.iastate.sdmay1809.shared.DiffConfig;
import edu.iastate.sdmay1809.shared.InstanceTracker.InstanceTracker;

public class DiffLinkerMain {

	public static void main(String[] args) throws JSONException, IOException {
		ArrayList<Long> timings = new ArrayList<Long>();

		DiffConfig config = DiffConfig.builder(args).build();

		timings.add(System.nanoTime());
		InstanceTracker it = new InstanceTracker(config.RESULT_DIR);
		it.run(Paths.get(config.DIFF_TEST_DIR, "newInstanceMap.json").toFile(), false);

		timings.add(System.nanoTime());
		DiffLinker dl = new DiffLinker(config, true, 10);
		int instancesLinked = dl.run("newInstanceMap.json");
		if (instancesLinked < 0) {
			System.err.println("[ERROR] could not link instances!");
		} else {
			System.out.println("Done! Linked " + instancesLinked + " instances");
		}

		timings.add(System.nanoTime());

		// Print out time differences
		for (int i = 1; i < timings.size(); i++) {
			long startTime = timings.get(i - 1);
			long endTime = timings.get(i);
			long duration = (endTime - startTime); // divide by 1000000 to get
													// milliseconds.
			System.out.println("Function #" + i + ": " + duration + "ns = " + duration / 1000000.0 + "ms");
		}
	}
}
