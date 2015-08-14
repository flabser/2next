package com.flabser.tests.data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import cashtracker.dao.TransactionDAO;
import cashtracker.model.Transaction;
import cashtracker.model.constants.TransactionState;
import cashtracker.model.constants.TransactionType;

import com.flabser.tests.InitEnv;
import com.flabser.util.Util;

public class InsertTransactionTest extends InitEnv {
	int iteration = 100;
	public static final SimpleDateFormat sqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Test
	public void test() {
		assertNotNull(db);
		for (int i = 0; i < iteration; i++) {
			Transaction t = new Transaction();
			t.setUser(new Long(Util.generateRandom()));
			t.setTransactionType(TransactionType.getRandom());
			t.setTransactionState(TransactionState.getRandom());
			t.setAccountFromById(new Long(1));
			t.setAccountToById(new Long(2));
			t.setCategoryById(new Long(1));
			t.setCostCenterById(new Long(1));
			t.setDate(new Date());
			t.setAmount(new BigDecimal(Util.generateRandom()));
			t.setExchangeRate(new Float(Util.generateRandom()));
			t.setRepeat(Util.getRandomBoolean());
			t.setEvery(Util.generateRandom());
			t.setRepeatStep(1);
			t.setStartDate(new Date());
			t.setEndDate(new Date());
			t.setNote(Util.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890", 256));
			t.setIncludeInReports(Util.getRandomBoolean());
			t.setBasis(Util.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890", 256));

			TransactionDAO dao = new TransactionDAO(ses);
			assertTrue(dao.add(t) > 0);
			System.out.println(t);
		}
	}

}
