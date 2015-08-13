package com.flabser.tests.data;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.junit.Test;

import cashtracker.model.Account;

public class SelectAccountTestJPA extends InitEnv {

	@Test
	public void test() {
		assertNotNull(db);

		long lStartTime = new Date().getTime();
		Query q = em.createQuery("SELECT a FROM Account  a");
		@SuppressWarnings("unchecked")
		List<Account> al = q.getResultList();
		for (Account a : al) {
			// System.out.println(a);
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
		long lEndTime = new Date().getTime();
		long difference = lEndTime - lStartTime;
		System.out.println(" 2 Elapsed milliseconds: " + difference);
		System.out.println("Size: " + al.size());

	}

}
