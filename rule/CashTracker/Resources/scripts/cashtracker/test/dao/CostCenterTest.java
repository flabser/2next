package cashtracker.test.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import cashtracker.dao.CostCenterDAO;
import cashtracker.model.CostCenter;

import com.flabser.tests.InitEnv;


public class CostCenterTest extends InitEnv {

	@Test
	public void test() {
		assertNotNull(db);

		CostCenterDAO dao = new CostCenterDAO(ses);

		int size = dao.findAll().size();
		int iteration = size + 1;

		for (int i = size; i < iteration; i++) {
			CostCenter m = new CostCenter();
			m.setName("cost center - " + i);

			dao.add(m);
		}

		System.out.println(dao.findAll());
	}
}
