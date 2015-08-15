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
		return """SELECT a FROM Account AS a"""
	}

	/*public String getCreateQuery() {
		return """INSERT INTO accounts
					(name, currency_code, opening_balance, amount_control, enabled,
					 writers, readers, note, include_in_totals, sort_order)
				  VALUES
					(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"""
	}*/

	/*public String getUpdateQuery() {
		return """UPDATE accounts
					SET
						name = ?,
						currency_code = ?,
						opening_balance = ?,
						amount_control = ?,
						enabled = ?,
						writers = ?,
						readers = ?,
						note = ?,
						include_in_totals = ?,
						sort_order = ?
					WHERE id = ?"""
	}*/

	public String getDeleteQuery() {
		return "DELETE FROM accounts WHERE id = ";
	}

	public List <Account> findAll() {
		List <Account> result = db.select(getSelectQuery(), user)
		return result
	}

	public Account findById(long id) {
		List <Account> list = db.select(getSelectQuery() + " WHERE a.id = $id", user)
		Account result = list.size() ? list[0] : null
		return result
	}

	public Account add(Account m) {
		/*String sql = """INSERT INTO accounts
							(name, currency_code, opening_balance, amount_control, enabled,
							writers, readers, note, include_in_totals, sort_order)
						VALUES
							('${m.name}', '${m.currencyCode}', ${m.openingBalance}, ${m.amountControl}, ${m.enabled},
							'{${m.writers?.join(",")}}',
							'{${m.readers?.join(",")}}',
							'${m.note}', ${m.includeInTotals}, ${m.sortOrder})"""*/
		return db.insert(m, user)
	}

	public Account update(Account m) {
		/*String sql = """UPDATE accounts
						SET
							name = '${m.name}',
							currency_code = '${m.currencyCode}',
							opening_balance = ${m.openingBalance},
							amount_control = ${m.amountControl},
							enabled = ${m.enabled},
							writers = '{${m.writers?.join(",")}}',
							readers = '{${m.readers?.join(",")}}',
							note = '${m.note}',
							include_in_totals = ${m.includeInTotals},
							sort_order = ${m.sortOrder}
						WHERE id = ${m.id}"""*/
		return db.update(m, user)
	}

	public void delete(Account m) {
		// String sql = getDeleteQuery() + m.id
		db.delete(m, user)
	}
}
