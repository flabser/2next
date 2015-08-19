package cashtracker.test.dao;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import cashtracker.dao.CategoryDAO;
import cashtracker.model.Category;
import cashtracker.model.constants.TransactionType;

import com.flabser.restful.data.IAppEntity;
import com.flabser.tests.InitEnv;


public class CategoryTest extends InitEnv {

	@Test
	public void test() {
		assertNotNull(db);

		CategoryDAO dao = new CategoryDAO(ses);
		Category parent = null;

		int size = dao.findAll().size();
		int iteration = size + 1;

		for (int i = size; i < iteration; i++) {
			Category m = new Category();
			if (parent != null) {
				m.setParentCategory(parent);
			}
			m.setName("category - " + i);
			m.setNote("note " + i);

			ArrayList <TransactionType> transactionTypes = new ArrayList <TransactionType>();
			if (i % 2 == 1) {
				transactionTypes.add(TransactionType.EXPENSE);
				transactionTypes.add(TransactionType.INCOME);
				m.setSortOrder(0);
			} else {
				transactionTypes.add(TransactionType.TRANSFER);
				m.setSortOrder(i);
			}
			m.setTransactionTypes(transactionTypes);

			parent = dao.add(m);
		}

		for (IAppEntity e : dao.findAll()) {
			Category m = (Category) e;
			m.getTransactionTypes().forEach(System.out::println);
		}
	}
}
