package cashtracker.test.dao;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cashtracker.dao.AccountDAO;
import cashtracker.dao.CostCenterDAO;
import cashtracker.dao.TagDAO;
import cashtracker.dao.TransactionDAO;
import cashtracker.helper.PageRequest;
import cashtracker.model.Account;
import cashtracker.model.CostCenter;
import cashtracker.model.Tag;
import cashtracker.model.Transaction;
import cashtracker.model.constants.TransactionState;
import cashtracker.model.constants.TransactionType;

import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.restful.data.IAppEntity;


public class TransactionTest extends InitEnv {

	TransactionDAO dao;
	String NL = "\n--------\n---------\n";

	@Before
	public void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			DatabasePoolException {
		super.init();
		dao = new TransactionDAO(ses);
	}

	@Test
	public void insertTest() {
		assertNotNull(db);

		AccountDAO accountDAO = new AccountDAO(ses);
		CostCenterDAO costCenterDAO = new CostCenterDAO(ses);
		TagDAO tagDAO = new TagDAO(ses);

		PageRequest pr = new PageRequest(0, 5, "", "");

		int size = dao.find(pr, null).size();
		int iteration = size + 100;

		for (int i = size; i < iteration; i++) {
			Transaction m = new Transaction();

			m.setDate(new Date(System.currentTimeMillis() + (3600 * i)));
			m.setAmount(new BigDecimal(1000 + i));
			m.setAccountFrom((Account) accountDAO.findAll().get(0));
			m.setCostCenter((CostCenter) costCenterDAO.findAll().get(0));

			List <Tag> tags = m.getTags();
			if (tags == null) {
				tags = new ArrayList <Tag>();
			}
			for (IAppEntity tag : tagDAO.findAll()) {
				tags.add((Tag) tag);
			}
			m.setTags(tags);

			if (i % 2 == 1) {
				m.setTransactionType(TransactionType.EXPENSE);
				m.setTransactionState(TransactionState.CONFIRMED);
			} else if (i % 10 == 0) {
				m.setTransactionType(TransactionType.TRANSFER);
				m.setTransactionState(TransactionState.PENDING);
			} else {
				m.setTransactionType(TransactionType.INCOME);
				m.setTransactionState(TransactionState.CONFIRMED);
			}

			dao.add(m);
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
}
