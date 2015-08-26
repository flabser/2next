package cashtracker.test.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import cashtracker.dao.BudgetDAO;
import cashtracker.model.Budget;
import cashtracker.model.constants.BudgetState;


public class BudgetTest extends InitEnv {

	// @Test
	public void test() {
		assertNotNull(db);

		BudgetDAO dao = new BudgetDAO(ses);

		int it = dao.findAll().size();

		Budget m = new Budget();
		m.setName("my budget " + it);

		if (it % 2 == 1) {
			m.setStatus(BudgetState.ACTIVE);
		} else {
			m.setStatus(BudgetState.DELETED);
		}

		dao.add(m);
	}

	@Test
	public void deleteTest() {
		BudgetDAO dao = new BudgetDAO(ses);
		dao.delete();
	}
}
