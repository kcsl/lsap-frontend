package edu.iastate.sdmay1809.shared.InstanceTracker;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;

public class InstanceParserV1Test {
	
	@Test
	public void testInit() {
		InstanceParser ip = new InstanceParserV1();
		assertNotNull(ip);
		assertThat(ip, instanceOf(InstanceParserV1.class));
		assertEquals("V1", ip.getName());
	}
	
	@Test
	public void testParseEntry() throws InvalidInstanceFormatException {
		String validEntry = "DEADLOCK@@@1aa730a@@@[30569+39+@linux-3.17-rc1@drivers@mtd@chips@cfi_cmdset_0001.c]@@@mutex";
		String invalidEntry = "UNPAIRED@@@781093@@@[f:@kernel_4.13@drivers@usb@host@ohci-q.c,l:76,o:1859+24]@@@lock";
		
		InstanceParser ip = new InstanceParserV1();
		JSONObject jo = null;
		try {
			jo = ip.parseEntry(invalidEntry);
			fail("Parsing an Invalid Entry should throw and exception!");
		} catch (InvalidInstanceFormatException e) {
			assertNull(jo);
		}
		
		jo = ip.parseEntry(validEntry);
		
		assertNotNull(jo);
		assertEquals("DEADLOCK", jo.getString("status"));
		assertEquals("1aa730a", jo.getString("id"));
		assertEquals(30569, jo.getInt("offset"));
		assertEquals(39, jo.getInt("length"));
		assertEquals("drivers/mtd/chips/cfi_cmdset_0001.c", jo.getString("filename"));
		assertEquals("mutex", jo.getString("name"));
	}

}
