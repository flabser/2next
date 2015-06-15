package cashtracker.dao

import java.sql.ResultSet
import java.util.List

import cashtracker.model.Account
import cashtracker.model.Budget

import com.flabser.dataengine.IDatabase
import com.flabser.script._Session
import com.flabser.users.User


public class AccountDAO {

	private IDatabase db
	private User user

	public AccountDAO(_Session session) {
		this.db = session.getDatabase()
		this.user = session.getUser()
	}

	public List <Account> findAll() {
		ResultSet rs = db.select("SELECT * FROM account", user)
		List <Account> result = []

		if (rs.next()) {
			result << getModelFromResultSet(rs)
		}

		return result
	}

	public Account findById(long id) {
		String sql = "select * from account where id = $id"
		ResultSet rs = db.select(sql, user)
		Account result = null

		if (rs.next()) {
			result = getModelFromResultSet(rs)
		}

		return result
	}

	public int addAccount(Account a) {
		String sql = """insert into account (name, type, amountcontrol, owner, observers)
							values ('${a.name}', ${a.type}, ${a.amountControl}, '${a.owner}', '${a.observers}')"""
		return db.insert(sql, user)
	}

	public void updateAccount(Account a) {
		String sql = """update account
							set name = '${a.name}',
								type = ${a.type},
								amountcontrol = '${a.amountControl}',
								owner = '${a.owner}',
								observers = '${a.observers}'
							where id = ${a.id}"""
		db.update(sql, user)
	}

	public void deleteAccount(Account a) {
		String sql = "delete from account where id = ${a.id}"
		db.delete(sql, user)
	}

	private Budget getModelFromResultSet(ResultSet rs){
		Account a = new Account()

		a.setId(rs.getInt("id"))
		a.setName(rs.getString("name"))
		a.setAmountControl(rs.getBigDecimal("amountcontrol"))
		a.setOwner(null) // TODO
		a.setType(rs.getInt("type"))
		a.setObservers(rs.getString("observers"))

		return a
	}
}
