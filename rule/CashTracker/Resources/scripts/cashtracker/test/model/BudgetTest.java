package cashtracker.test.model;

import java.util.Date;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import cashtracker.model.Budget;
import cashtracker.model.Budget.BudgetStatus;


public class BudgetTest extends Assert {

	@Test
	public void test() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		Budget m = new Budget();
		m.setAuthor(null);
		m.setRegDate(new Date());
		m.setId(1);
		m.setName("budget 1");
		m.setRegDate(new Date());
		m.setOwner(1L);
		m.setStatus(BudgetStatus.ACTIVE);

		String json = mapper.writeValueAsString(m);

		Budget b = mapper.readValue(json, Budget.class);

		System.out.println(json);
		System.out.println(b);

		assertTrue(true);
	}
}
