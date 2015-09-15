package cashtracker.test.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cashtracker.dao.CostCenterDAO;
import cashtracker.model.CostCenter;

import com.flabser.dataengine.pool.DatabasePoolException;


public class CostCenterTest extends InitEnv {

	CostCenterDAO dao;

	@Before
	public void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			DatabasePoolException {
		super.init();
		dao = new CostCenterDAO(ses);
	}

	@Test
	public void insertTest() {
		assertNotNull(db);

		int size = dao.findAll().size();
		int iteration = size + 50;

		for (int i = size; i < iteration; i++) {
			CostCenter m = new CostCenter();
			m.setName("cost center - " + i);

			dao.add(m);
		}
	}

	@Test
	public void selectTest() {
		List <CostCenter> list = dao.findAll();
		assertTrue(list.size() > 0);

		CostCenter mFirst = dao.findById(list.get(0).getId());
		CostCenter mLast = dao.findById(list.get(list.size() - 1).getId());

		assertNotNull(mFirst);
		assertNotNull(mLast);
	}

	@Test
	public void updateTest() {
		List <CostCenter> list = dao.findAll();

		for (CostCenter m : list) {
			m.setName(m.getName() + "-u");
			System.out.println(dao.update(m));
		}
	}
}
