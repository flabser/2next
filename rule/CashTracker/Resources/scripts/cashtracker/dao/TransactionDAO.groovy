package cashtracker.dao

import cashtracker.model.Account
import cashtracker.model.Category
import cashtracker.model.CostCenter
import cashtracker.model.Transaction

import com.flabser.dataengine.IDatabase
import com.flabser.script._Session
import com.flabser.users.User


public class TransactionDAO {

	private final static String selectAll = """
		select
		   t.*,
		   a_from.*,
		   a_to.*,
		   c.*,
		   cc.*
		 from transactions as t
		   left join accounts as a_from on a_from.id = t.account_from
		   left join accounts as a_to on a_to.id = t.account_to
		   left join categories as c on c.id = t.category
		   left join costcenters as cc on cc.id = t.cost_center"""

	private IDatabase db
	private User user

	public TransactionDAO(_Session session) {
		this.db = session.getDatabase()
		this.user = session.getUser()
	}

	public List <Transaction> findAll() {
		List <Transaction> result = db.select("select * from transactions", Transaction.class, user)
		return result
	}

	public Transaction findById(long id) {
		String sql = "select * from transactions where id = $id"
		List <Transaction> list = db.select(sql, Transaction.class, user)
		Transaction result = list.size() ? list[0] : null
		return result
	}

	public List <Transaction> findAllByAccount(Account m) {
		String sql ="select * from transactions where account_id = ${m.id}"
		List <Transaction> list = db.select(sql, Transaction.class, user)
		Transaction result = list.size() ? list[0] : null
		return result
	}

	public List <Transaction> findAllByCostCenter(CostCenter m) {
		String sql ="select * from transactions where cost_center_id = ${m.id}"
		List <Transaction> list = db.select(sql, Transaction.class, user)
		Transaction result = list.size() ? list[0] : null
		return result
	}

	public List <Transaction> findAllByCategory(Category m) {
		String sql ="select * from transactions where category_id = ${m.id}"
		List <Transaction> list = db.select(sql, Transaction.class, user)
		Transaction result = list.size() ? list[0] : null
		return result
	}

	public int addTransaction(Transaction m) {
		String sql = """insert into transactions
							("USER", transaction_type, transaction_state,
							reg_date, account_from, account_to,
							amount, exchange_rate, category, cost_center,
							tags, repeat, every, repeat_step, start_date, end_date,
							basis, note, include_in_reports)
						values ('${m.user.login}', ${m.transactionType.code}, ${m.transactionState.code},
								'${m.regDate}', ${m.accountFrom.id}, ${m.accountTo.id},
								${m.amount}, ${m.exchangeRate}, ${m.category}, ${m.costCenter},
								${m.tags}, ${m.repeat}, ${m.every}, ${m.repeatStep}, ${m.startDate}, ${m.endDate},
								${m.basis}, ${m.note}, ${m.includeInReports})"""
		return db.insert(sql, user)
	}

	public void updateTransaction(Transaction m) {
		String sql = """update transactions
						set
							"USER" = '${m.user.login}',
							transaction_type = ${m.transactionType.code},
							transaction_state = ${m.transactionState.code},
							reg_date = '${m.regDate}',
							account_from = ${m.accountFrom.id},
							account_to = ${m.accountTo.id},
							amount = ${m.amount},
							exchange_rate = ${m.exchangeRate},
							category = ${m.category},
							cost_center = ${m.costCenter},
							tags = ${m.tags},
							repeat = ${m.repeat},
							every = ${m.every},
							repeat_step = ${m.repeatStep},
							start_date = '${m.startDate}',
							end_date = ${m.endDate},
							basis = '${m.basis}',
							note = '${m.note}',
							include_in_reports = ${m.includeInReports}
						where id = ${m.id}"""
		db.update(sql, user)
	}

	public void deleteTransaction(Transaction m) {
		String sql = "delete from transactions where id = ${m.id}"
		db.delete(sql, user)
	}
}
