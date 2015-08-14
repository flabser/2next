package com.flabser.tests.system;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;

import cashtracker.model.Account;

import com.flabser.tests.InitEnv;
import com.flabser.util.Util;

public class AddDatabaseUserTest extends InitEnv {
	int iteration = 100000;

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

			System.out.println(i);
			db.insert(a, user);

		}
		System.out.println("done " + iteration);
	}

}
