package com.flabser.tests.data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cashtracker.dao.CostCenterDAO;
import cashtracker.model.CostCenter;

import com.flabser.tests.InitEnv;
import com.flabser.util.Util;

public class InsertCostCenter extends InitEnv {
	int iteration = 100;

	@Test
	public void test() {
		assertNotNull(db);
		for (int i = 0; i < iteration; i++) {
			CostCenter e = new CostCenter();
			e.setName(Util.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890", 128));

			CostCenterDAO dao = new CostCenterDAO(ses);
			assertTrue(dao.add(e) > 0);
		}
	}

}
