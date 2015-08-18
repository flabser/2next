package cashtracker.test.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import cashtracker.dao.CostCenterDAO;
import cashtracker.model.CostCenter;

import com.flabser.tests.InitEnv;


public class CostCenterTest extends InitEnv {

	int iteration = 1;

	@Test
	public void test() {
		assertNotNull(db);

		CostCenterDAO dao = new CostCenterDAO(ses);

		int it = dao.findAll().size();

		for (int i = 0; i < iteration; i++) {
			CostCenter m = new CostCenter();
			m.setName("cost center - " + it++);

			dao.add(m);
		}

		System.out.println(dao.findAll());
	}
}
