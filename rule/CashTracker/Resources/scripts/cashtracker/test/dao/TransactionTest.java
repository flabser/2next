package cashtracker.test.dao;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import cashtracker.dao.AccountDAO;
import cashtracker.dao.CostCenterDAO;
import cashtracker.dao.TagDAO;
import cashtracker.dao.TransactionDAO;
import cashtracker.model.Account;
import cashtracker.model.CostCenter;
import cashtracker.model.Tag;
import cashtracker.model.Transaction;
import cashtracker.model.constants.TransactionState;
import cashtracker.model.constants.TransactionType;

import com.flabser.restful.data.IAppEntity;
import com.flabser.tests.InitEnv;


public class TransactionTest extends InitEnv {

	@Test
	public void test() {
		assertNotNull(db);

		TransactionDAO dao = new TransactionDAO(ses);
		AccountDAO accountDAO = new AccountDAO(ses);
		CostCenterDAO costCenterDAO = new CostCenterDAO(ses);
		TagDAO tagDAO = new TagDAO(ses);

		int size = dao.findAll().size();
		int iteration = size + 1;

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
			} else if (i % 2 == 0) {
				m.setTransactionType(TransactionType.TRANSFER);
				m.setTransactionState(TransactionState.PENDING);
			} else {
				m.setTransactionType(TransactionType.INCOME);
				m.setTransactionState(TransactionState.CONFIRMED);
			}

			dao.add(m);
		}

		System.out.println(dao.findAll().size());

		List <IAppEntity> ts = dao.findAll();
		for (IAppEntity it : ts) {
			Transaction t = (Transaction) it;
			t.getTags().forEach(System.out::println);
		}
	}
}
