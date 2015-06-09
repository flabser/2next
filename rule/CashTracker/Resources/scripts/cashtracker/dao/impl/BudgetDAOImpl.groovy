package cashtracker.dao.impl

import java.sql.ResultSet
import java.util.List

import cashtracker.dao.BudgetDAO
import cashtracker.model.Budget

import com.flabser.dataengine.IDatabase
import com.flabser.script._Session
import com.flabser.solutions.cashtracker.constants.BudgetStatusType
import com.flabser.users.User


public class BudgetDAOImpl implements BudgetDAO {

	private IDatabase db
	private User user

	public BudgetDAOImpl(_Session session) {
		this.db = session.getDatabase()
		this.user = session.getUser()
	}

	public List <Budget> findAll() {
		ResultSet rs = db.select("SELECT * FROM budget", user)
		List <Budget> result = []

		while (rs.next()) {
			result.add(getModelFromResultSet(rs))
		}

		return result
	}

	public Budget findById(long id) {
		String sql = "select * from budget where id = $id"
		ResultSet rs = db.select(sql, user)
		Budget result = null

		if (rs.next()) {
			result = getModelFromResultSet(rs)
		}

		return result
	}

	public int addBudget(Budget b) {
		String sql = """INSERT BUDGET (NAME, REGDATE, OWNER, STATUS)
							values ('${b.name}', '${b.regDate}', '${b.owner}', ${b.status.code})"""
		return db.insert(sql, user)
	}

	public void updateBudget(Budget b) {
		String sql = "update budget set name='${b.name}', owner='${b.owner}', status=${b.status.code} where id = ${b.id}"
		db.update(sql, user)
	}

	public void deleteBudget(Budget b) {
		String sql = "delete from budget where id = ${b.id}"
		db.delete(sql, user)
	}

	private Budget getModelFromResultSet(ResultSet rs){
		Budget b = new Budget()

		b.setId(rs.getInt("ID"))
		b.setName(rs.getString("NAME"))
		b.setRegDate(rs.getDate("REGDATE"))
		b.setOwner(null) // TODO get user
		b.setStatus(BudgetStatusType.getType(rs.getInt("STATUS")))

		return b
	}
}
