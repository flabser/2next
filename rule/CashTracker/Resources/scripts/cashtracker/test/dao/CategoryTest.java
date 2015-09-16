package cashtracker.test.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cashtracker.dao.CategoryDAO;
import cashtracker.model.Category;
import cashtracker.model.constants.TransactionType;

import com.flabser.dataengine.jpa.IAppEntity;
import com.flabser.dataengine.pool.DatabasePoolException;


public class CategoryTest extends InitEnv {

	CategoryDAO dao;

	@Before
	public void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			DatabasePoolException {
		super.init();
		dao = new CategoryDAO(ses);
	}

	@Test
	public void insertTest() {
		Category parent = null;

		int size = dao.findAll().size();
		int iteration = size + 3;

		for (int i = size; i < iteration; i++) {
			Category m = new Category();
			if (parent != null) {
				m.setParent(parent);
			}
			m.setName("category - " + i);
			m.setNote("note " + i);
			m.setColor("red");

			ArrayList <TransactionType> transactionTypes = new ArrayList <TransactionType>();
			if (i % 2 == 1) {
				transactionTypes.add(TransactionType.EXPENSE);
				transactionTypes.add(TransactionType.INCOME);
			} else {
				transactionTypes.add(TransactionType.TRANSFER);
			}
			m.setTransactionTypes(transactionTypes);

			parent = (Category) dao.add(m);
		}

		for (IAppEntity e : dao.findAll()) {
			Category m = (Category) e;
			m.getTransactionTypes().forEach(System.out::println);
		}
	}

	@Test
	public void updateTest() {
		List <Category> list = dao.findAll();

		for (Category m : list) {
			m.setName(m.getName() + "-u");
			System.out.println(dao.update(m));
		}
	}
}
