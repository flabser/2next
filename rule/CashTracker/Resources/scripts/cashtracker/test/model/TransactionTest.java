package cashtracker.test.model;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import cashtracker.model.Transaction;

import com.fasterxml.jackson.databind.ObjectMapper;


public class TransactionTest extends Assert {

	@Test
	public void test() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		Transaction m = new Transaction();
		m.setAuthor(1L);
		m.setRegDate(new Date());
		m.setId(1);
		m.setAmount(new BigDecimal(1000));
		m.setDate(new Date());
		m.setNote("note");

		String json = mapper.writeValueAsString(m);
		Transaction c = mapper.readValue(json, Transaction.class);

		System.out.println(json);

		assertTrue(true);
	}
}
