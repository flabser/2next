package com.flabser.tests.data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

import cashtracker.dao.AccountDAO;
import cashtracker.model.Account;

import com.flabser.util.Util;

public class InsertEntityTest extends InitEnv {
	int iteration = 100;

	@Test
	public void test() {
		assertNotNull(db);
		for (int i = 0; i < iteration; i++) {
			Account a = new Account();
			a.setName(Util.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890", 128));
			a.setCurrencyCode(Util.generateRandomAsText("QWERTYUIOP", 3));
			a.setOpeningBalance(new BigDecimal(Util.generateRandom()));
			a.setAmountControl(new BigDecimal(Util.generateRandom()));
			a.setEnabled(Util.getRandomBoolean());
			a.setIncludeInTotals(Util.getRandomBoolean());
			a.setNote(Util.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890", 256));
			a.setSortOrder(0);

			AccountDAO dao = new AccountDAO(ses);
			assertTrue(dao.add(a) > 0);
		}
	}

}
