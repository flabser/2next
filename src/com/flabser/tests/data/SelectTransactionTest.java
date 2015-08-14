package com.flabser.tests.data;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.flabser.tests.InitEnv;

import cashtracker.dao.TransactionDAO;
import cashtracker.model.Transaction;

public class SelectTransactionTest extends InitEnv {

	@Test
	public void test() {
		assertNotNull(db);
		TransactionDAO dao = new TransactionDAO(ses);
		List<Transaction> list = dao.findAll();
		assertNotNull(list);
		for (Transaction entity : list) {
			System.out.println(entity);
			assertNotNull(entity.getAccountFrom());
			assertNotNull(entity.getAccountFromId());
			assertNotNull(entity.getAccountTo());
			assertNotNull(entity.getAmount());
			assertNotNull(entity.getBasis());
			assertNotNull(entity.getCategory());
			assertNotNull(entity.getCategoryId());
			assertNotNull(entity.getCostCenter());
			assertNotNull(entity.getCostCenterId());
			assertNotNull(entity.getDate());
			assertNotNull(entity.getEndDate());
			assertNotNull(entity.getEvery());
			assertNotNull(entity.getExchangeRate());
			assertNotNull(entity.getId());
			assertNotNull(entity.getNote());
			assertNotNull(entity.getRepeatStep());
			assertNotNull(entity.getStartDate());
			assertNotNull(entity.getTransactionState());
			assertNotNull(entity.getTransactionStateCode());
			assertNotNull(entity.getTransactionType());
			assertNotNull(entity.getUser());

		}
		System.out.println(list.size());
	}
}
