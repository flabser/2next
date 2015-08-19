package cashtracker.test.dao;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cashtracker.dao.CategoryDAO;
import cashtracker.model.Category;
import cashtracker.model.constants.TransactionType;

import com.flabser.tests.InitEnv;


public class CategoryTest extends InitEnv {

	@Test
	public void test() {
		assertNotNull(db);

		CategoryDAO dao = new CategoryDAO(ses);

		int size = dao.findAll().size();
		int iteration = size + 2;

		for (int i = size; i < iteration; i++) {
			Category m = new Category();
			m.setParentCategory(null);
			m.setName("category - " + i);
			m.setNote("note " + i);

			List <TransactionType> transactionTypes = new ArrayList <TransactionType>();
			if (i % 2 == 1) {
				transactionTypes.add(TransactionType.EXPENSE);
				transactionTypes.add(TransactionType.INCOME);
				m.setSortOrder(0);
			} else {
				transactionTypes.add(TransactionType.EXPENSE);
				m.setSortOrder(i);
			}
			m.setTransactionTypes(transactionTypes);

			dao.add(m);
		}
	}
}
