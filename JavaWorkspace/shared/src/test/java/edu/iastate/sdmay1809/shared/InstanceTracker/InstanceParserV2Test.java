package edu.iastate.sdmay1809.shared.InstanceTracker;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.json.JSONObject;
import org.junit.Test;

public class InstanceParserV2Test {

	@Test
	public void testInit() {
		InstanceParser ip = new InstanceParserV2();
		assertNotNull(ip);
		assertThat(ip, instanceOf(InstanceParserV2.class));
		assertEquals("V2", ip.getName());
	}
	
	@Test
	public void testParseEntry() throws InvalidInstanceFormatException {
		String invalidEntry = "DEADLOCK@@@1aa730a@@@[30569+39+@linux-3.17-rc1@drivers@mtd@chips@cfi_cmdset_0001.c]@@@mutex";
		String validEntry = "UNPAIRED@@@781093@@@[f:@kernel_4.13@drivers@usb@host@ohci-q.c,l:76,o:1859+24]@@@lock";
		
		InstanceParser ip = new InstanceParserV2();
		JSONObject jo = null;
		try {
			jo = ip.parseEntry(invalidEntry);
			fail("Parsing an Invalid Entry should throw and exception!");
		} catch (InvalidInstanceFormatException e) {
			assertNull(jo);
		}
		
		jo = ip.parseEntry(validEntry);
		
		assertNotNull(jo);
		assertEquals("UNPAIRED", jo.getString("status"));
		assertEquals("781093", jo.getString("id"));
		assertEquals(1859, jo.getInt("offset"));
		assertEquals(24, jo.getInt("length"));
		assertEquals(76, jo.getInt("length2"));
		assertEquals("drivers/usb/host/ohci-q.c", jo.getString("filename"));
		assertEquals("lock", jo.getString("name"));
	}
	
}
