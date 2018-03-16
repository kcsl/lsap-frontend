package edu.iastate.sdmay1809;

import junit.framework.TestCase;

public class DiffLinkerTest extends TestCase {
	public DiffLinkerTest(String name) {
		super(name);
	}
	
	public void testInit() throws Exception {
		DiffConfig config = DiffConfig.builder().build();
		DiffLinker dl = new DiffLinker(config, false, 3);
		assertTrue(dl != null);
	}
}
