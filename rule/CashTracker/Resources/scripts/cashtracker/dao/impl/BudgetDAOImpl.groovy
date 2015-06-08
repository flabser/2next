package cashtracker.dao.impl;

import java.sql.ResultSet;
import java.util.List;

import cashtracker.dao.BudgetDAO;
import cashtracker.model.Budget;

import com.flabser.dataengine.IDatabase;
import com.flabser.users.User;


public class BudgetDAOImpl implements BudgetDAO {

	private IDatabase db;
	private User user;

	public BudgetDAOImpl(IDatabase em, User user) {
		this.db = em;
		this.user = user;
	}

	public List <Budget> findAll() {
		List <Budget> result = null;
		ResultSet rs = db.select("SELECT * FROM budget", user);
		// ...
		return result;
	}

	public Budget findById(long id) {
		String sql = "select * from budget where id = " + id;
		Budget result = null;
		ResultSet rs = db.select(sql, user);
		// ...
		return result;
	}

	public int addBudget(Budget b) {
		String sql = "INSERT BUDGET (NAME, REGDATE, OWNER, STATUS) values ('${b.name}', '${b.regDate}', '${b.owner}', '${b.status.code}')";
		return db.insert(sql, user);
	}

	public void updateBudget(Budget b) {
		String sql = "update budget set ... where id=";
		db.update(sql, user);
	}

	public void deleteBudget(Budget b) {
		String sql = "delete from budget where id = " + b.getId();
		db.delete(sql, user);
	}
}
