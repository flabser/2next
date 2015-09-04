package cashtracker.test.model;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import cashtracker.model.Budget;
import cashtracker.model.constants.BudgetState;

import com.fasterxml.jackson.databind.ObjectMapper;


public class BudgetTest extends Assert {

	@Test
	public void test() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		Budget m = new Budget();
		m.setAuthor(1L);
		m.setRegDate(new Date());
		m.setId(1);
		m.setName("budget 1");
		m.setRegDate(new Date());
		m.setOwner(1L);
		m.setStatus(BudgetState.ACTIVE);

		String json = mapper.writeValueAsString(m);
		Budget b = mapper.readValue(json, Budget.class);

		System.out.println(json);

		assertTrue(true);
	}
}
