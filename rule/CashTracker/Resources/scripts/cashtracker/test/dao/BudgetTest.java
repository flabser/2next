package cashtracker.test.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import cashtracker.dao.BudgetDAO;
import cashtracker.model.Budget;
import cashtracker.model.constants.BudgetState;

import com.flabser.dataengine.pool.DatabasePoolException;


public class BudgetTest extends InitEnv {

	BudgetDAO dao;

	@Before
	public void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			DatabasePoolException {
		super.init();
		dao = new BudgetDAO(ses);
	}

	@Test
	public void insertTest() {
		int it = dao.findAll().size();

		Budget m = new Budget();
		m.setName("my budget " + it);

		if (it % 2 == 1) {
			m.setStatus(BudgetState.ACTIVE);
		} else {
			m.setStatus(BudgetState.DELETED);
		}

		assertNotNull(dao.add(m) != null);
	}

	@Test
	public void deleteTest() {
		dao.delete();
	}
}
