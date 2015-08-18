package cashtracker.test.dao;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;

import cashtracker.dao.AccountDAO;
import cashtracker.model.Account;

import com.flabser.tests.InitEnv;
import com.flabser.util.Util;


public class AccountTest extends InitEnv {

	int iteration = 1;

	@Test
	public void test() {
		assertNotNull(db);

		AccountDAO dao = new AccountDAO(ses);

		int it = dao.findAll().size();

		for (int i = 0; i < iteration; i++) {
			Account m = new Account();
			m.setName("Account - " + it);
			m.setCurrencyCode(Util.generateRandomAsText("QWERTYUIOP", 3));
			m.setOpeningBalance(new BigDecimal(Util.generateRandom()));
			m.setAmountControl(new BigDecimal(Util.generateRandom()));
			m.setEnabled(Util.getRandomBoolean());
			m.setIncludeInTotals(Util.getRandomBoolean());
			m.setNote("note " + it);

			it++;

			dao.add(m);
		}

		System.out.println(dao.findAll());
	}
}
