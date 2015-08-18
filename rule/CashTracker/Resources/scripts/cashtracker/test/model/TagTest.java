package cashtracker.test.model;

import org.junit.Assert;
import org.junit.Test;

import cashtracker.model.Tag;

import com.fasterxml.jackson.databind.ObjectMapper;


public class TagTest extends Assert {

	@Test
	public void test() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		Tag m = new Tag();
		m.setId(1);
		m.setName("tag 1");

		String json = mapper.writeValueAsString(m);
		Tag c = mapper.readValue(json, Tag.class);

		System.out.println(json);

		assertTrue(true);
	}
}
