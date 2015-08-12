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
		return """SELECT id, name, currency_code, opening_balance, amount_control, enabled,
					writers, readers, note, include_in_totals, sort_order FROM accounts"""
	}

	public String getCreateQuery() {
		return """INSERT INTO accounts
					(name, currency_code, opening_balance, amount_control, enabled,
					 writers, readers, note, include_in_totals, sort_order)
				  VALUES
					(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"""
	}

	public String getUpdateQuery() {
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

	public int add(Account m) {
		String sql = """INSERT INTO accounts
							(name, currency_code, opening_balance, amount_control, enabled,
							writers, readers, note, include_in_totals, sort_order)
						VALUES
							('${m.name}', '${m.currencyCode}', ${m.openingBalance}, ${m.amountControl}, ${m.enabled},
							'{${m.writers?.join(",")}}',
							'{${m.readers?.join(",")}}',
							'${m.note}', ${m.includeInTotals}, ${m.sortOrder})"""
		return db.insert(sql, user)
	}

	public void update(Account m) {
		String sql = """UPDATE accounts
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
						WHERE id = ${m.id}"""
		db.update(sql, user)
	}

	public void delete(Account m) {
		String sql = getDeleteQuery() + m.id
		db.delete(sql, user)
	}
}
