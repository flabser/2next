package com.flabser.tests.data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import cashtracker.dao.CategoryDAO;
import cashtracker.model.Category;
import cashtracker.model.constants.TransactionType;

import com.flabser.tests.InitEnv;
import com.flabser.util.Util;

public class InsertCategoryTest extends InitEnv {
	int iteration = 100;

	@Test
	public void test() {
		assertNotNull(db);
		for (int i = 0; i < iteration; i++) {
			Category e = new Category();
			ArrayList<TransactionType> type = new ArrayList<TransactionType>();
			type.add(TransactionType.getRandom());
			type.add(TransactionType.getRandom());
			//e.setTransactionTypes(type);
			e.setName(Util.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890", 128));
			e.setEnabled(Util.getRandomBoolean());
			e.setNote(Util.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890", 256));
			e.setColor(Util.generateRandomShort());
			e.setSortOrder(Util.generateRandomShort());

			CategoryDAO dao = new CategoryDAO(ses);
			assertTrue(dao.add(e).getId() > 0);
		}
	}

}
