package com.flabser.tests.data;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import cashtracker.dao.TransactionDAO;
import cashtracker.model.Transaction;

import com.flabser.restful.data.IAppEntity;
import com.flabser.tests.InitEnv;

public class SelectTransactionTest extends InitEnv {

	@Test
	public void test() {
		assertNotNull(db);
		TransactionDAO dao = new TransactionDAO(ses);
		List<IAppEntity> list = dao.findAll();
		assertNotNull(list);
		for (IAppEntity entity : list) {
			Transaction t = (Transaction) entity;
			System.out.println(entity);
			assertNotNull(t.getAccountFrom());
			assertNotNull(t.getAccountFromId());
			assertNotNull(t.getAccountTo());
			assertNotNull(t.getAmount());
			assertNotNull(t.getCategory());
			assertNotNull(t.getCategoryId());
			assertNotNull(t.getCostCenter());
			assertNotNull(t.getCostCenterId());
			assertNotNull(t.getDate());
			assertNotNull(t.getEndDate());
			assertNotNull(t.getEvery());
			assertNotNull(t.getExchangeRate());
			assertNotNull(t.getId());
			assertNotNull(t.getNote());
			assertNotNull(t.getRepeatStep());
			assertNotNull(t.getStartDate());
			assertNotNull(t.getTransactionState());
			assertNotNull(t.getTransactionState());
			assertNotNull(t.getTransactionType());
			// assertNotNull(t.getUser());
		}
		System.out.println(list.size());
	}
}
