package edu.iastate.sdmay1809;

import junit.framework.TestCase;

public class DiffMapperTest extends TestCase {
	public DiffMapperTest(String name) {
		super(name);
	}
	
	public void testInit() throws Exception {
		DiffMapper dm = new DiffMapper(false);
		assertTrue(dm != null);
	}
}
