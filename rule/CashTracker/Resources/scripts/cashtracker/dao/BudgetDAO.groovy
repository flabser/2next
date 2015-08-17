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
		return "SELECT b FROM Budget AS b";
	}

	/*public String getCreateQuery() {
		return "INSERT INTO budgets (name, reg_date, owner, status) VALUES (?, ?, ?, ?)"
	}

	public String getUpdateQuery() {
		return "UPDATE budgets SET name = ?, owner = ?, status = ? WHERE id = ?";
	}*/

	public String getDeleteQuery() {
		return "DELETE FROM budgets";
	}

	public List <Budget> findAll() {
		List <Budget> result = db.select(getSelectQuery(), user)
		return result
	}

	public Budget add(Budget m) {
		//String sql = """INSERT INTO budgets (name, reg_date, owner) VALUES ('${m.name}', ${m.regDate}, ${m.owner})"""
		return db.insert(m, user)
	}

	public Budget update(Budget m) {
		//String sql = """UPDATE budgets SET name = '${m.name}', owner = ${m.owner}"""
		return db.update(m, user)
	}

	public void delete() {
		String sql = getDeleteQuery()
		db.delete(sql, user)
	}
}
