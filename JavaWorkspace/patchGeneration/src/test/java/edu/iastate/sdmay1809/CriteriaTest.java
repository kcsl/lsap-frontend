package edu.iastate.sdmay1809;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CriteriaTest {
	Criteria criteria;

	@Test
	public void criteriaConstructor()
	{
		criteria = new Criteria("test", false);
		assertThat(criteria, instanceOf(Criteria.class));
	}
	
	@Test
	public void criteriaNameComponent()
	{
		criteria = new Criteria("test", false);
		assertEquals(criteria.getNameComponent(), "test");
	}
	
	@Test
	public void criteriaMustHave()
	{
		criteria = new Criteria("test", false);
		assertEquals(criteria.mustHave(), false);
	}
	
	@Test
	public void criteriaChange()
	{
		criteria = new Criteria("test2", false);
		assertThat(criteria, instanceOf(Criteria.class));
		assertEquals(criteria.getNameComponent(), "test2");
		assertEquals(criteria.mustHave(), false);
		
		criteria = new Criteria("test3", true);
		assertThat(criteria, instanceOf(Criteria.class));
		assertEquals(criteria.getNameComponent(), "test3");
		assertEquals(criteria.mustHave(), true);
	}
}
