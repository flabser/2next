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
		this.user = session.getUser()
	}

	public List <Budget> findAll() {
		List <Budget> result = db.select("SELECT * FROM budgets", Budget.class, user)
		return result
	}

	public Budget findById(long id) {
		List <Budget> list = db.select("select * from budgets where id = $id", Budget.class, user)

		Budget result = null

		if (list.size() > 0) {
			result = list[0]
		}

		return result
	}

	public int add(Budget b) {
		String sql = """INSERT BUDGETS (NAME, REGDATE, OWNER, STATUS)
							values ('${b.name}', '${b.regDate}', '${b.owner}', ${b.status.code})"""
		return db.insert(sql, user)
	}

	public void update(Budget b) {
		String sql = "update budgets set name='${b.name}', owner='${b.owner}', status=${b.status.code} where id = ${b.id}"
		db.update(sql, user)
	}

	public void delete(Budget b) {
		String sql = "delete from budgets where id = ${b.id}"
		db.delete(sql, user)
	}
}
