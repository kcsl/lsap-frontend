package edu.iastate.sdmay1809;

import static org.junit.Assert.*;

import org.junit.Test;

public class DiffMapperTest {
	
	@Test
	public void testInit() {
		DiffMapper dm = new DiffMapper(DiffConfig.builder().build(), false);
		assertTrue(dm != null);
	}
}
