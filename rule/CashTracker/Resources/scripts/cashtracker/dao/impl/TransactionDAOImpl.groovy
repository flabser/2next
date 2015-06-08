package cashtracker.dao.impl;

import java.sql.ResultSet;
import java.util.List;

import cashtracker.dao.TransactionDAO;
import cashtracker.model.Account;
import cashtracker.model.Category;
import cashtracker.model.CostCenter;
import cashtracker.model.Transaction;

import com.flabser.dataengine.IDatabase;
import com.flabser.script._Session
import com.flabser.users.User;


public class TransactionDAOImpl implements TransactionDAO {

	private IDatabase db;
	private User user;

	public TransactionDAOImpl(_Session session) {
		this.db = session.getDatabase();
		this.user = session.getUser();
	}

	public List <Transaction> findAll() {
		List <Transaction> result = null;
		ResultSet rs = db.select("SELECT * FROM transaction", user);
		// ...
		return result;
	}

	public Transaction findById(long id) {
		String sql = "select * from transaction where id = " + id;
		Transaction result = null;
		ResultSet rs = db.select(sql, user);
		// ...
		return result;
	}

	public List <Transaction> findAllByAccount(Account a) {
		return null;
	}

	public List <Transaction> findAllByCostCenter(CostCenter cc) {
		return null;
	}

	public List <Transaction> findAllByCategory(Category c) {
		return null;
	}

	public int addTransaction(Transaction t) {
		String sql = "insert into costcenter (...) values (...)";
		return db.insert(sql, user);
	}

	public void updateTransaction(Transaction t) {
		String sql = "update costcenter set ... where id=";
		db.update(sql, user);
	}

	public void deleteTransaction(Transaction t) {
		String sql = "delete from costcenter where id = " + t.getId();
		db.delete(sql, user);
	}
}
