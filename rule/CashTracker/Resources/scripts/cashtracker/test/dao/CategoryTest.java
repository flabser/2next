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

	int iteration = 1;

	@Test
	public void test() {
		assertNotNull(db);

		CategoryDAO dao = new CategoryDAO(ses);

		int it = dao.findAll().size();

		for (int i = 0; i < iteration; i++) {
			Category m = new Category();
			m.setName("category - " + it);
			m.setParentCategory(null);

			List <TransactionType> transactionTypes = new ArrayList <TransactionType>();
			transactionTypes.add(TransactionType.EXPENSE);
			transactionTypes.add(TransactionType.INCOME);
			m.setTransactionTypes(transactionTypes);

			m.setNote("note " + it);
			m.setSortOrder(0);

			it++;

			System.out.println(i);
			System.out.println(m);

			dao.add(m);
		}

		System.out.println(dao.findAll());
	}
}
