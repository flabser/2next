package cashtracker.test.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cashtracker.dao.AccountDAO;
import cashtracker.model.Account;
import cashtracker.validation.AccountValidator;
import cashtracker.validation.ValidationError;

import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.util.Util;


public class AccountTest extends InitEnv {

	AccountDAO dao;
	private AccountValidator validator = new AccountValidator();

	@Override
	@Before
	public void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			DatabasePoolException {
		super.init();
		dao = new AccountDAO(ses);
	}

	@Test
	public void insertTest() {
		int size = dao.findAll().size();
		int iteration = size + 2;

		for (int i = size; i < iteration; i++) {
			Account m = new Account();
			m.setName("Account - " + i);

			if (i % 2 == 1) {
				m.setCurrencyCode("RUB");
			} else if (i % 3 == 1) {
				m.setCurrencyCode("USD");
			} else {
				m.setCurrencyCode("KZT");
			}

			m.setOpeningBalance(new BigDecimal(Util.generateRandom()));
			m.setAmountControl(new BigDecimal(Util.generateRandom()));
			m.setEnabled(Util.getRandomBoolean());
			m.setNote("note " + i);

			ValidationError ve = validator.validate(m);
			if (ve.hasError()) {
				for (cashtracker.validation.ValidationError.Error err : ve.getErrors()) {
					assertFalse("ValidationError : " + err.toString(), ve.hasError());
				}
			}

			dao.add(m);
		}
	}

	@Test
	public void selectTest() {
		List <Account> list = dao.findAll();
		assertTrue(list.size() > 0);

		Account aFirst = dao.findById(list.get(0).getId());
		Account aLast = dao.findById(list.get(list.size() - 1).getId());

		assertNotNull(aFirst);
		assertNotNull(aLast);
	}

	@Test
	public void updateTest() {
		List <Account> list = dao.findAll();

		for (Account m : list) {
			m.setName(m.getName());
			System.out.println(dao.update(m));
		}
	}

	@Test
	public void deleteTest() {
		List <Account> list = dao.findAll();

		for (Account m : list) {
			if (!dao.existsTransactionByAccount(m)) {
				dao.delete(m);
			}
		}
	}
}
