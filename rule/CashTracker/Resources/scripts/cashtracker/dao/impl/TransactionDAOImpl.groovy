package cashtracker.dao.impl

import java.sql.ResultSet
import java.util.List

import cashtracker.dao.TransactionDAO
import cashtracker.model.Account
import cashtracker.model.Category
import cashtracker.model.CostCenter
import cashtracker.model.Transaction

import com.flabser.dataengine.IDatabase
import com.flabser.script._Session
import com.flabser.users.User


public class TransactionDAOImpl implements TransactionDAO {

	private IDatabase db
	private User user

	public TransactionDAOImpl(_Session session) {
		this.db = session.getDatabase()
		this.user = session.getUser()
	}

	public List <Transaction> findAll() {
		List <Transaction> result = db.select("SELECT * FROM transaction", Transaction.class, user)
		return result
	}

	public Transaction findById(long id) {
		String sql = "select * from transaction where id = $id"
		ResultSet rs = db.select(sql, user)
		Transaction result = null

		if (rs.next()) {
			result = getModelFromResultSet(rs)
		}

		return result
	}

	public List <Transaction> findAllByAccount(Account a) {
		return null
	}

	public List <Transaction> findAllByCostCenter(CostCenter cc) {
		return null
	}

	public List <Transaction> findAllByCategory(Category c) {
		return null
	}

	public int addTransaction(Transaction t) {
		String sql = """insert into transaction
							(type, 
							'USER',
							date,
							regdate,
							category,
							subcategory,
							amount,
							account,
							costcenter,
							repeat,
							every,
							repeatstep,
							enddate,
							basis,
							note)
						values (${t.type}, '${t.author}', '${t.date}', '${t.regDate}', '${t.category}',
								'${t.parentCategory}', '${t.amount}', '${t.account}', '${t.costCenter}',
								${t.repeat}, ${t.every}, ${t.repeatStep}, ${t.endDate}, '${t.basis}', ${t.comment})"""
		return db.insert(sql, user)
	}

	public void updateTransaction(Transaction t) {
		String sql = """update transaction set
							type = ${t.type},
							date = ${t.date},
							category = ${t.category},
							subcategory = ${t.parentCategory},
							amount = ${t.amount},
							account = ${t.account},
							costcenter = ${t.costCenter},
							repeat = ${t.repeat},
							every = ${t.every},
							repeatstep = ${t.repeatStep},
							enddate = ${t.endDate},
							basis = ${t.basis},
							note = ${t.comment},
						where id = ${t.id}"""
		db.update(sql, user)
	}

	public void deleteTransaction(Transaction t) {
		String sql = "delete from transaction where id = ${t.id}"
		db.delete(sql, user)
	}

	private Transaction getModelFromResultSet(ResultSet rs){
		Transaction t = new Transaction()

		t.setId(rs.getInt("id"))
		t.setAuthor(null)
		t.setDate(rs.getDate("date"))
		t.setRegDate(rs.getDate("regdate"))
		t.setCategory(rs.getInt("category"))
		t.setParentCategory(rs.getInt("parentCategory"))
		t.setAmount(rs.getBigDecimal("amount"))
		t.setAccount(rs.getInt("account"))
		t.setCostCenter(rs.getInt("costcenter"))
		t.setRepeat(rs.getBoolean("repeat"))
		t.setEvery(rs.getInt("every"))
		t.setRepeatStep(rs.getInt("repeatstep"))
		t.setEndDate(rs.getDate("enddate"))
		t.setBasis(rs.getString("basis"))
		t.setComment(rs.getString("note"))

		t
	}
}
