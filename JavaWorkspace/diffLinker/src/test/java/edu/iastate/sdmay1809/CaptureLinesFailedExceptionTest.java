package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import org.junit.Test;

public class CaptureLinesFailedExceptionTest {
	
	@Test
	public void testInit() {
		Exception e = new CaptureLinesFailedException();
		assertNotNull(e);
		assertThat(e, instanceOf(CaptureLinesFailedException.class));
	}
	
	@Test
	public void testInitMessage() {
		Exception e = new CaptureLinesFailedException("message");
		assertNotNull(e);
		assertThat(e, instanceOf(CaptureLinesFailedException.class));
		assertEquals("message", e.getMessage());
	}

}
