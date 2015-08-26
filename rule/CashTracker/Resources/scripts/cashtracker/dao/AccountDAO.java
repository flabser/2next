package cashtracker.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import cashtracker.model.Account;

import com.flabser.dataengine.IDatabase;
import com.flabser.restful.data.IAppEntity;
import com.flabser.script._Session;
import com.flabser.users.User;


public class AccountDAO {

	private IDatabase db;
	private User user;

	public AccountDAO(_Session session) {
		this.db = session.getDatabase();
		this.user = session.getAppUser();
	}

	public String getSelectQuery() {
		return "SELECT a FROM Account AS a";
	}

	public List <IAppEntity> findAll() {
		List <IAppEntity> result = db.select(getSelectQuery() + " ORDER BY a.name", user);
		return result;
	}

	public Account findById(long id) {
		List <IAppEntity> list = db.select(getSelectQuery() + " WHERE a.id = " + id + " ORDER BY a.name", user);
		Account result = list.size() > 0 ? (Account) list.get(0) : null;
		return result;
	}

	public boolean existsTransactionByAccount(Account m) {
		String jpql = "SELECT t.id FROM Transaction AS t WHERE t.accountFrom = :account or t.accountTo = :account";

		EntityManager em = db.getEntityManager();
		Query q = em.createQuery(jpql);
		q.setParameter("account", m);
		q.setMaxResults(1);
		return q.getResultList().size() > 0;
	}

	public Account add(Account m) {
		return (Account) db.insert(m, user);
	}

	public Account update(Account m) {
		return (Account) db.update(m, user);
	}

	public void delete(Account m) {
		db.delete(m, user);
	}
}
