package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import org.junit.Test;

import edu.iastate.sdmay1809.DiffConfig.DiffConfigBuilder;

public class DiffConfigTest {

	@Test
	public void diffConfigConstructs() {
		DiffConfigBuilder base = DiffConfig.builder();
		assertThat(DiffConfig.builder().build(), instanceOf(DiffConfig.class));
		assertThat(DiffConfig.builder(base).build(), instanceOf(DiffConfig.class));
	}
}
