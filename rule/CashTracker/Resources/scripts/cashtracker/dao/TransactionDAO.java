package cashtracker.dao;

import java.util.List;

import cashtracker.model.Account;
import cashtracker.model.Category;
import cashtracker.model.CostCenter;
import cashtracker.model.Transaction;

import com.flabser.dataengine.IDatabase;
import com.flabser.restful.data.IAppEntity;
import com.flabser.script._Session;
import com.flabser.users.User;


public class TransactionDAO {

	private IDatabase db;
	private User user;

	public TransactionDAO(_Session session) {
		this.db = session.getDatabase();
		this.user = session.getAppUser();
	}

	public String getSelectQuery() {
		return "SELECT t FROM Transaction AS t";
	}

	public List <IAppEntity> findAll() {
		List <IAppEntity> result = db.select(getSelectQuery() + " ORDER BY t.date", user);
		return result;
	}

	public Transaction findById(long id) {
		String sql = getSelectQuery() + " WHERE t.id = " + id + " ORDER BY t.date";
		List <IAppEntity> list = db.select(sql, user);
		Transaction result = list.size() > 0 ? (Transaction) list.get(0) : null;
		return result;
	}

	public List <IAppEntity> findAllByAccount(Account m) {
		String sql = getSelectQuery() + " WHERE t.account = " + m.getId() + " ORDER BY t.date";
		List <IAppEntity> result = db.select(sql, user);
		return result;
	}

	public List <IAppEntity> findAllByCostCenter(CostCenter m) {
		String sql = getSelectQuery() + " WHERE t.cost_center = " + m.getId() + " ORDER BY t.date";
		List <IAppEntity> result = db.select(sql, user);
		return result;
	}

	public List <IAppEntity> findAllByCategory(Category m) {
		String sql = getSelectQuery() + " WHERE t.category = " + m.getId() + " ORDER BY t.date";
		List <IAppEntity> result = db.select(sql, user);
		return result;
	}

	public Transaction add(Transaction m) {
		m.setUserId((long) user.id);
		return (Transaction) db.insert(m, user);
	}

	public Transaction update(Transaction m) {
		return (Transaction) db.update(m, user);
	}

	public void delete(Transaction m) {
		db.delete(m, user);
	}
}
