package edu.iastate.sdmay1809.shared.InstanceTracker;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;

public class InstanceParserManagerTest {
	
	@After
	public void cleanup() {
		InstanceParserManager.clear();
	}

	@Test
	public void testInit() {
		InstanceParserManager ipm = new InstanceParserManager();
		assertNotNull(ipm);
		assertThat(ipm, instanceOf(InstanceParserManager.class));
	}
	
	@Test
	public void testPut() {
		InstanceParser ip = InstanceParserManager.put(null);
		assertNull(ip);
		
		ip = InstanceParserManager.put(new InstanceParserMock("mock1", false));
		assertNull(ip);
		
		ip = InstanceParserManager.put(new InstanceParserMock("mock1", false));
		assertNotNull(ip);
		assertEquals("mock1", ip.getName());
	}
	
	@Test
	public void testGet() {
		InstanceParser ip = InstanceParserManager.put(new InstanceParserMock("mock1", false));
		assertNull(ip);
		
		ip = InstanceParserManager.get("mock1");
		assertNotNull(ip);
		assertEquals("mock1", ip.getName());
	}
	
	@Test
	public void testClear() {
		InstanceParser ip = InstanceParserManager.put(new InstanceParserMock("mock1", false));
		assertNull(ip);
		
		ip = InstanceParserManager.get("mock1");
		assertNotNull(ip);
		assertEquals("mock1", ip.getName());
		
		InstanceParserManager.clear();
		ip = InstanceParserManager.get("mock1");
		assertNull(ip);
		assertEquals(0, InstanceParserManager.getParsers().size());
	}
	
	@Test
	public void testGetParsers() {
		assertEquals(0, InstanceParserManager.getParsers().size());
		for(int i = 0; i < 100; i++) {
			InstanceParserManager.put(new InstanceParserMock("mock" + i, false));
			assertEquals(i+1, InstanceParserManager.getParsers().size());
		}
		for(int i = 0; i < 100; i++) {
			InstanceParser ip = InstanceParserManager.get("mock" + i);
			assertNotNull(ip);
			assertEquals("mock" + i, ip.getName());
			
			InstanceParserManager.put(new InstanceParserMock("mock" + i, false));
			assertEquals(100, InstanceParserManager.getParsers().size());
		}
		for(int i = 0; i < 100; i++) {
			InstanceParser ip = InstanceParserManager.get("mock" + i);
			assertNotNull(ip);
			assertEquals("mock" + i, ip.getName());
		}
	}
	
	class InstanceParserMock implements InstanceParser {
		
		public boolean throwError;
		public String name;
		
		public InstanceParserMock(String name, boolean throwError) {
			this.throwError = throwError;
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public JSONObject parseEntry(String instance) throws InvalidInstanceFormatException {
			if(throwError) {
				throw new InvalidInstanceFormatException("thrown on purpose");
			}
			
			return new JSONObject().put("mock", true);
		}
		
	}
}
