package cashtracker.test.dao;

import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cashtracker.dao.CategoryDAO;
import cashtracker.model.Category;
import cashtracker.model.constants.TransactionType;
import cashtracker.validation.CategoryValidator;
import cashtracker.validation.ValidationError;

import com.flabser.dataengine.pool.DatabasePoolException;


public class CategoryTest extends InitEnv {

	private CategoryDAO dao;
	private CategoryValidator validator = new CategoryValidator();

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
		int iteration = size + 5;

		for (int i = size; i < iteration; i++) {
			Category m = new Category();
			if (parent != null) {
				m.setParent(parent);
			}
			m.setName("category - " + i);
			m.setNote("note " + i);
			m.setColor("#FF0000");

			List <TransactionType> transactionTypes = new ArrayList <TransactionType>();

			if (parent == null) {
				if (i % 2 == 1) {
					transactionTypes.add(TransactionType.EXPENSE);
					transactionTypes.add(TransactionType.INCOME);
				} else {
					transactionTypes.add(TransactionType.TRANSFER);
				}
			} else {
				transactionTypes = parent.getTransactionTypes();
			}

			m.setTransactionTypes(transactionTypes);

			ValidationError ve = validator.validate(m);
			if (ve.hasError()) {
				for (cashtracker.validation.ValidationError.Error err : ve.getErrors()) {
					assertFalse("ValidationError : " + err.toString(), ve.hasError());
				}
			}

			dao.add(m);

			parent = (i % 3 == 1) ? m : null;
		}
	}

	@Test
	public void selectByTrType() {
		List <Category> eList = dao.findByTransactionType(TransactionType.EXPENSE);
		List <Category> iList = dao.findByTransactionType(TransactionType.INCOME);
		List <Category> tList = dao.findByTransactionType(TransactionType.TRANSFER);

		System.out.println(eList.size() + ", " + iList.size() + ", " + tList.size());
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
