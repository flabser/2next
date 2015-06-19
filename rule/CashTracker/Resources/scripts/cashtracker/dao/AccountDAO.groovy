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
		this.user = session.getUser()
	}

	public List <Account> findAll() {
		List <Account> result = db.select("SELECT * FROM accounts", Account.class, user)
		return result
	}

	public Account findById(long id) {
		List <Account> list = db.select("select * from accounts where id = $id", Account.class, user)
		Account result = list.size() ? list[0] : null
		return result
	}

	public int addAccount(Account a) {
		String sql = """insert into accounts
							(name, type, currency_code, opening_balance, amount_control,
							owner, observers, include_in_totals, note, sort_order)
						values
							('${a.name}', ${a.type}, '${a.currencyCode}', ${a.openingBalance}, ${a.amountControl},
							'${a.owner}', '${a.observers}', ${a.includeInTotals}, '${a.note}', ${a.sortOrder})"""
		return db.insert(sql, user)
	}

	public void updateAccount(Account a) {
		String sql = """update accounts
						set
							name = '${a.name}',
							type = ${a.type},
							currency_code = '${a.currencyCode}',
							opening_balance = ${a.openingBalance},
							amount_control = ${a.amountControl},
							owner = '${a.owner}',
							observers = '${a.observers}',
							include_in_totals = ${a.includeInTotals},
							note = '${a.note}',
							sort_order = ${a.sortOrder}
						where id = ${a.id}"""
		db.update(sql, user)
	}

	public void deleteAccount(Account a) {
		String sql = "delete from accounts where id = ${a.id}"
		db.delete(sql, user)
	}
}
