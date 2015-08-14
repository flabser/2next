package com.flabser.tests.data;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import cashtracker.model.Account;

import com.flabser.restful.data.IAppEntity;

public class SelectAccountTest extends InitEnv {

	@Test
	public void test() {
		assertNotNull(db);

		long lStartTime = new Date().getTime();
		List<IAppEntity> al = db.select("SELECT a FROM Account  a", user);
		for (IAppEntity ia : al) {
			Account a = (Account) ia;
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
		System.out.println("Elapsed milliseconds: " + difference);
		System.out.println("Size: " + al.size());

	}

}
