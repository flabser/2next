package com.flabser.tests.data;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import cashtracker.dao.AccountDAO;
import cashtracker.model.Account;

public class SelectAccountTest extends InitEnv {

	@Test
	public void test() {
		assertNotNull(db);
		long lStartTime = new Date().getTime();
		AccountDAO dao = new AccountDAO(ses);
		List<Account> list = dao.findAll();
		for (Account a : list) {
			// System.out.println(a);
			assertNotNull(a.getAmountControl());
			assertNotNull(a.getCurrencyCode());
			assertNotNull(a.getId());
			assertNotNull(a.getName());
			assertNotNull(a.getNote());
			assertNotNull(a.getOpeningBalance());
			assertNotNull(a.getSortOrder());
			assertNotNull(a.isEnabled());
			assertNotNull(a.isIncludeInTotals());

		}
		long lEndTime = new Date().getTime();
		long difference = lEndTime - lStartTime;
		System.out.println(" 1 Elapsed milliseconds: " + difference);
		System.out.println("------------------" + list.size());

	}

}
