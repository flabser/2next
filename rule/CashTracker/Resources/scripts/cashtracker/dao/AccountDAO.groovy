package cashtracker.dao

import cashtracker.model.Account

import com.flabser.dataengine.IDatabase
import com.flabser.script._Session
import com.flabser.users.User


public class AccountDAO {

	private IDatabase db
	private User user

	public AccountDAO(_Session session) {
		this.db = session.getDatabase()
		this.user = session.getAppUser()
	}

	public String getSelectQuery() {
		return """SELECT id, name, currency_code, opening_balance, amount_control,
					owner, observers, include_in_totals, note, sort_order FROM accounts"""
	}

	public String getCreateQuery() {
		return """INSERT INTO accounts
					(name, currency_code, opening_balance, amount_control,
					 owner, observers, include_in_totals, note, sort_order)
				  VALUES
					(?, ?, ?, ?, ?, ?, ?, ?, ?)"""
	}

	public String getUpdateQuery() {
		return """UPDATE accounts
					SET
						name = ?,
						currency_code = ?,
						opening_balance = ?,
						amount_control = ?,
						owner = ?,
						observers = ?,
						include_in_totals = ?,
						note = ?,
						sort_order = ?
					WHERE id = ?"""
	}

	public String getDeleteQuery() {
		return "DELETE FROM accounts WHERE id = ";
	}

	public List <Account> findAll() {
		List <Account> result = db.select(getSelectQuery(), Account.class, user)
		return result
	}

	public Account findById(long id) {
		List <Account> list = db.select(getSelectQuery() + " WHERE id = $id", Account.class, user)
		Account result = list.size() ? list[0] : null
		return result
	}

	public int add(Account a) {
		String sql = """INSERT INTO accounts
							(name, currency_code, opening_balance, amount_control,
							owner, observers, include_in_totals, note, sort_order)
						VALUES
							('${a.name}', '${a.currencyCode}', ${a.openingBalance}, ${a.amountControl},
							'${a.owner}', '${a.observers}', ${a.includeInTotals}, '${a.note}', ${a.sortOrder})"""
		return db.insert(sql, user)
	}

	public void update(Account a) {
		String sql = """UPDATE accounts
						SET
							name = '${a.name}',
							currency_code = '${a.currencyCode}',
							opening_balance = ${a.openingBalance},
							amount_control = ${a.amountControl},
							owner = '${a.owner}',
							observers = '${a.observers}',
							include_in_totals = ${a.includeInTotals},
							note = '${a.note}',
							sort_order = ${a.sortOrder}
						WHERE id = ${a.id}"""
		db.update(sql, user)
	}

	public void delete(Account a) {
		String sql = getDeleteQuery() + a.id
		db.delete(sql, user)
	}
}
