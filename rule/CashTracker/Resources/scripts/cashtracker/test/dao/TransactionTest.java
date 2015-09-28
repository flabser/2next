package cashtracker.test.dao;

import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cashtracker.dao.AccountDAO;
import cashtracker.dao.CategoryDAO;
import cashtracker.dao.CostCenterDAO;
import cashtracker.dao.TagDAO;
import cashtracker.dao.TransactionDAO;
import cashtracker.helper.PageRequest;
import cashtracker.helper.TransactionFilter;
import cashtracker.model.Account;
import cashtracker.model.Category;
import cashtracker.model.CostCenter;
import cashtracker.model.Tag;
import cashtracker.model.Transaction;
import cashtracker.model.constants.TransactionState;
import cashtracker.model.constants.TransactionType;
import cashtracker.validation.TransactionValidator;
import cashtracker.validation.ValidationError;

import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.util.Util;


public class TransactionTest extends InitEnv {

	private TransactionDAO dao;
	private TransactionValidator validator = new TransactionValidator();

	@Before
	public void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			DatabasePoolException {
		super.init();
		dao = new TransactionDAO(ses);
	}

	@Test
	public void insertTest() {
		AccountDAO accountDAO = new AccountDAO(ses);
		CostCenterDAO costCenterDAO = new CostCenterDAO(ses);
		TagDAO tagDAO = new TagDAO(ses);
		CategoryDAO categoryDAO = new CategoryDAO(ses);

		PageRequest pr = new PageRequest(0, 5, "", "");
		int size = dao.find(pr, null).size();
		int iteration = size + 25;
		long ctime = System.currentTimeMillis() - ((60 * 60 * 24) * 1000);

		for (int i = size; i < iteration; i++) {
			Transaction m = new Transaction();

			m.setDate(new Date(ctime + ((60 * 60 * 24) * i)));
			m.setAmount(new BigDecimal(1000 + i));
			m.setAccountFrom((Account) Util.getRandomFromList(accountDAO.findAllEnabled()));
			m.setCostCenter((CostCenter) Util.getRandomFromList(costCenterDAO.findAll()));

			List <Tag> tags = new ArrayList <Tag>();
			tags.add((Tag) Util.getRandomFromList(tagDAO.findAll()));
			m.setTags(tags);

			if (i % 2 == 1) {
				m.setTransactionType(TransactionType.EXPENSE);
				m.setTransactionState(TransactionState.CONFIRMED);
			} else if (i % 10 == 0) {
				m.setTransactionType(TransactionType.TRANSFER);
				m.setTransactionState(TransactionState.PENDING);
				m.setAccountTo((Account) Util.getRandomFromList(accountDAO.findAllEnabled()));
			} else {
				m.setTransactionType(TransactionType.INCOME);
				m.setTransactionState(TransactionState.CONFIRMED);
			}

			m.setCategory((Category) Util.getRandomFromList(categoryDAO.findByTransactionType(m.getTransactionType())));

			ValidationError ve = validator.validate(m);
			if (ve.hasError()) {
				for (cashtracker.validation.ValidationError.Error err : ve.getErrors()) {
					assertFalse("ValidationError : " + err.toString(), ve.hasError());
				}
			}

			dao.add(m);

			System.out.println(iteration + " : " + i);
		}
	}

	@Test
	public void selectTest() {
		List <Transaction> ts = dao.find(new PageRequest(50000, 20, "", ""), TransactionType.EXPENSE);
		for (Transaction t : ts) {
			t.getTags().forEach(Tag::getId);
		}
		//
		ts = dao.find(new PageRequest(10000, 20, "", ""), TransactionType.INCOME);
		ts = dao.find(new PageRequest(20000, 20, "", ""), TransactionType.TRANSFER);
	}

	@Test
	public void updateTest() {
		List <Transaction> list = dao.find(new PageRequest(0, 20, "", ""), null);

		for (Transaction m : list) {
			m.setAmount(m.getAmount().add(new BigDecimal(1)));
			System.out.println(dao.update(m));
		}
	}

	@Test
	public void deleteTest() {
		List <Transaction> list = dao.find(new PageRequest(0, 100, "", ""), null);
		for (Transaction t : list) {
			dao.delete(t);
		}
	}

	@Test
	public void findAllByTagsTest() {
		TagDAO tagDao = new TagDAO(ses);
		dao.findAllByTags(tagDao.findAll().subList(0, 1));
	}

	@Test
	public void findByTransactionFilterTest() {
		AccountDAO accountDao = new AccountDAO(ses);
		CategoryDAO categoryDao = new CategoryDAO(ses);
		CostCenterDAO ccDao = new CostCenterDAO(ses);
		TagDAO tagDao = new TagDAO(ses);

		List <Account> accounts = new ArrayList <Account>();
		Account acc1 = new Account();
		acc1.setId(1L);
		Account acc2 = new Account();
		acc2.setId(2L);
		accounts.add(acc1);
		accounts.add(acc2);

		TransactionFilter tf = new TransactionFilter();
		// tf.setAccounts(accountDao.findAll().subList(0, 2));
		tf.setAccounts(accounts);
		tf.setCategories(categoryDao.findAll().subList(0, 2));
		tf.setCostCenters(ccDao.findAll().subList(0, 2));
		tf.setTags(tagDao.findAll().subList(0, 2));
		List <TransactionType> types = new ArrayList <TransactionType>();
		types.add(TransactionType.EXPENSE);
		types.add(TransactionType.INCOME);
		tf.setTypes(types);

		Date stD = new Date(System.currentTimeMillis() - (86400000L));
		Date eD = new Date();
		tf.setDateRange(new Date[] { stD, eD });
		//
		PageRequest pr = new PageRequest(0, 100, "", "");
		List <Transaction> list = dao.find(tf, pr);

		System.out.println(list.size());
		for (Transaction t : list) {
			// System.out.println(t);
			t.getAuthor();
		}
	}
}
