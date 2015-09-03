package cashtracker.test.model;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import cashtracker.model.Account;

import com.fasterxml.jackson.databind.ObjectMapper;


public class AccountTest extends Assert {

	@Test
	public void test() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		Account m = new Account();
		m.setId(1);
		m.setAmountControl(new BigDecimal(10000));
		m.setCurrencyCode("KZT");
		m.setEnabled(true);
		m.setName("account 1");
		m.setNote("account note");
		m.setOpeningBalance(new BigDecimal(0));

		String json = mapper.writeValueAsString(m);
		Account c = mapper.readValue(json, Account.class);

		System.out.println(json);

		assertTrue(true);
	}
}
