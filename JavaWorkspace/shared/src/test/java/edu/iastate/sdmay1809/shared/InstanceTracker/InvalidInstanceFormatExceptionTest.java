package edu.iastate.sdmay1809.shared.InstanceTracker;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import org.junit.Test;

public class InvalidInstanceFormatExceptionTest {
	
	@Test
	public void initDefault() {
		InvalidInstanceFormatException e = new InvalidInstanceFormatException();
		assertNotNull(e);
		assertThat(e, instanceOf(InvalidInstanceFormatException.class));
	}
	
	@Test
	public void initMessage() {
		InvalidInstanceFormatException e = new InvalidInstanceFormatException("message");
		assertNotNull(e);
		assertThat(e, instanceOf(InvalidInstanceFormatException.class));
		assertEquals(e.getMessage(), "message");
	}

}
