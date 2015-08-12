package cashtracker.dao

import cashtracker.model.Budget

import com.flabser.dataengine.IDatabase
import com.flabser.script._Session
import com.flabser.users.User


public class BudgetDAO {

	private IDatabase db
	private User user

	public BudgetDAO(_Session session) {
		this.db = session.getDatabase()
		this.user = session.getAppUser()
	}

	public String getSelectQuery() {
		return "SELECT id, name, reg_date, owner, status FROM budgets";
	}

	public String getCreateQuery() {
		return "INSERT INTO budgets (name, reg_date, owner, status) VALUES (?, ?, ?, ?)"
	}

	public String getUpdateQuery() {
		return "UPDATE budgets SET name = ?, owner = ?, status = ? WHERE id = ?";
	}

	public String getDeleteQuery() {
		return "DELETE FROM budgets WHERE id = ";
	}

	public List <Budget> findAll() {
		List <Budget> result = db.select(getSelectQuery(), Budget.class, user)
		return result
	}

	public Budget findById(long id) {
		List <Budget> list = db.select(getSelectQuery() + " WHERE id = $id", Budget.class, user)

		Budget result = null

		if (list.size() > 0) {
			result = list[0]
		}

		return result
	}

	public int add(Budget m) {
		String sql = """INSERT INTO budgets (name, reg_date, owner, status)
							VALUES ('${m.name}', '${m.regDate}', '${m.owner}', ${m.status.code})"""
		return db.insert(sql, user)
	}

	public void update(Budget m) {
		String sql = """UPDATE budgets
							SET name = '${m.name}', owner = '${m.owner}', status = ${m.status.code}
						WHERE id = ${m.id}"""
		db.update(sql, user)
	}

	public void delete(Budget m) {
		String sql = getDeleteQuery() + m.id
		db.delete(sql, user)
	}
}
