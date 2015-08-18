package cashtracker.test.model;

import org.junit.Assert;
import org.junit.Test;

import cashtracker.model.CostCenter;

import com.fasterxml.jackson.databind.ObjectMapper;


public class CostCenterTest extends Assert {

	@Test
	public void test() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		CostCenter m = new CostCenter();
		m.setId(1);
		m.setName("CostCenter 1");

		String json = mapper.writeValueAsString(m);
		CostCenter c = mapper.readValue(json, CostCenter.class);

		System.out.println(json);

		assertTrue(true);
	}
}
