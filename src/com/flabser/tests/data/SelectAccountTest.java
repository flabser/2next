package com.flabser.tests.data;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import cashtracker.dao.AccountDAO;
import cashtracker.model.Account;

public class SelectAccountTest extends InitEnv {

	@Test
	public void test() {
		assertNotNull(db);
		AccountDAO dao = new AccountDAO(ses);
		List<Account> list = dao.findAll();
		assertNotNull(list);
		for (Account a : list) {
			System.out.println(a);
			assertNotNull(a.getAmountControl());
			assertNotNull(a.getCurrencyCode());
			assertNotNull(a.getId());
			assertNotNull(a.getName());
			assertNotNull(a.getNote());
			assertNotNull(a.getOpeningBalance());
			assertNotNull(a.getSortOrder());
			assertNotNull(a.getTableName());
			assertNotNull(a.isEnabled());
			assertNotNull(a.isIncludeInTotals());

		}
		System.out.println(list.size());
	}

}
