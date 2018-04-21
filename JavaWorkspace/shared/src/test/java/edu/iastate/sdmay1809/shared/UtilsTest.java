package edu.iastate.sdmay1809.shared;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class UtilsTest {
	
	@Test
	public void utilsInit() {
		Utils utils = new Utils();
		assertNotNull(utils);
		assertThat(utils, instanceOf(Utils.class));
	}
	
	@Test
	public void coalesceTestChecksForNull() {
		Object nullCheck = Utils.coalesce((Object[]) null);
		
		assertNull(nullCheck);
	}

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
	
	@Test
	public void coalesceTestWorksWithString() {
		String one = "one";
		String two = "two";
		
		String t1 = Utils.coalesce(one, two);
		String t2 = Utils.coalesce(two, one);
		String t3 = Utils.coalesce(null, one);
		String t4 = Utils.coalesce(null, two);
		String t5 = Utils.coalesce(one, null);
		String t6 = Utils.coalesce(two, null);
		String t7 = Utils.coalesce(null, null);
		
		assertEquals(one, t1);
		assertEquals(two, t2);
		assertEquals(one, t3);
		assertEquals(two, t4);
		assertEquals(one, t5);
		assertEquals(two, t6);
		assertNull(t7);
	}
	
	@Test
	public void concatenateWorksWithInteger() {
		Integer[] list1 = {0, 1, 2, 3, 4, 5};
		Integer[] list2 = {6, 7, 8, 9, 10, 11};
		Integer[] list3 = {12, 13, 14};
		
		Integer[] test1 = Utils.concatenate(list1, list2);
		Integer[] test2 = Utils.concatenate(list2, list3);
		Integer[] test3 = Utils.concatenate(list3, list3);
		Integer[] test4 = Utils.concatenate(list1, null);
		Integer[] test5 = Utils.concatenate(null, list3);
		Integer[] test6 = Utils.concatenate(null, null);
		
		Integer[] expected1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
		Integer[] expected2 = {6, 7, 8, 9, 10, 11, 12, 13, 14};
		Integer[] expected3 = {12, 13, 14, 12, 13, 14};
		Integer[] expected4 = {0, 1, 2, 3, 4, 5};
		Integer[] expected5 = {12, 13, 14};
		
		assertArrayEquals(expected1, test1);
		assertArrayEquals(expected2, test2);
		assertArrayEquals(expected3, test3);
		assertArrayEquals(expected4, test4);
		assertArrayEquals(expected5, test5);
		assertNull(test6);
	}
	
	@Test
	public void concatenateWorksWithString() {
		String[] list1 = {"zero", "one", "two", "three", "four", "five"};
		String[] list2 = {"six", "seven", "eight", "nine", "ten", "eleven"};
		String[] list3 = {"twelve", "thirteen", "fourteen"};
		
		String[] test1 = Utils.concatenate(list1, list2);
		String[] test2 = Utils.concatenate(list2, list3);
		String[] test3 = Utils.concatenate(list3, list3);
		String[] test4 = Utils.concatenate(list1, null);
		String[] test5 = Utils.concatenate(null, list3);
		String[] test6 = Utils.concatenate(null, null);
		
		String[] expected1 = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven"};
		String[] expected2 = {"six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen"};
		String[] expected3 = {"twelve", "thirteen", "fourteen", "twelve", "thirteen", "fourteen"};
		String[] expected4 = {"zero", "one", "two", "three", "four", "five"};
		String[] expected5 = {"twelve", "thirteen", "fourteen"};
		
		assertArrayEquals(expected1, test1);
		assertArrayEquals(expected2, test2);
		assertArrayEquals(expected3, test3);
		assertArrayEquals(expected4, test4);
		assertArrayEquals(expected5, test5);
		assertNull(test6);
	}
	
	@Test
	public void executeCompletesCorrectly() throws IOException, InterruptedException {
		String execOutput;
		if(System.getProperty("os.name").equals("Windows")) {
			execOutput = Utils.execute(new String[] {"cd"}, null);
			assertThat(execOutput, containsString(System.getProperty("user.dir")));
		} else {
			execOutput = Utils.execute(new String[] {"pwd"}, null);
			assertThat(execOutput, containsString("Process exited with value 0"));
		}
	}
	
	@Test
	public void executeRedirectCompletesCorrectly() throws IOException, InterruptedException {
		if(System.getProperty("os.name").equals("Windows")) {
			int ret = Utils.execute(new String[] {"cd"}, null, false);
			assertEquals(0, ret);
			ret = Utils.execute(new String[] {"cd"}, null, true);
			assertEquals(0, ret);
		} else {
			int ret = Utils.execute(new String[] {"pwd"}, null, false);
			assertEquals(0, ret);
			ret = Utils.execute(new String[] {"pwd"}, null, true);
			assertEquals(0, ret);
		}
	}
}
