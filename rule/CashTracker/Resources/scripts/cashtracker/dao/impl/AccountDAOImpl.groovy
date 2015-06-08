package cashtracker.dao.impl;

import java.sql.ResultSet;
import java.util.List;

import cashtracker.dao.AccountDAO;
import cashtracker.model.Account;

import com.flabser.dataengine.IDatabase;
import com.flabser.users.User;


public class AccountDAOImpl implements AccountDAO {

	private IDatabase db;
	private User user;

	public AccountDAOImpl(IDatabase em, User user) {
		this.db = em;
		this.user = user;
	}

	public List <Account> findAll() {
		List <Account> result = null;
		ResultSet rs = db.select("SELECT * FROM account", user);
		// ...
		return result;
	}

	public Account findById(long id) {
		String sql = "select * from account where id = " + id;
		Account result = null;
		ResultSet rs = db.select(sql, user);
		// ...
		return result;
	}

	public int addAccount(Account a) {
		String sql = "insert into account (...) values (...)";
		return db.insert(sql, user);
	}

	public void updateAccount(Account a) {
		String sql = "update account set ... where id=";
		db.update(sql, user);
	}

	public void deleteAccount(Account a) {
		String sql = "delete from account where id = " + a.getId();
		db.delete(sql, user);
	}
}
