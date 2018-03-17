package edu.iastate.sdmay1809;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilsTest {

	@Test
	public void coalesceTestWorksWithInteger() {
		Integer one = 1;
		Integer two = 2;
		
		Integer t1 = Utils.coalesce(one, two);
		Integer t2 = Utils.coalesce(two, one);
		Integer t3 = Utils.coalesce(null, one);
		Integer t4 = Utils.coalesce(null, two);
		Integer t5 = Utils.coalesce(one, null);
		Integer t6 = Utils.coalesce(two, null);
		Integer t7 = Utils.coalesce(null, null);
		
		assertEquals(one, t1);
		assertEquals(two, t2);
		assertEquals(one, t3);
		assertEquals(two, t4);
		assertEquals(one, t5);
		assertEquals(two, t6);
		assertNull(t7);
	}
}
