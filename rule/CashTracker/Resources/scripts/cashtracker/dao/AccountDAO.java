package cashtracker.dao;

import java.util.List;

import javax.persistence.Query;

import cashtracker.model.Account;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class AccountDAO extends DAO {

	public AccountDAO(_Session session) {
		super(session);
	}

	@SuppressWarnings("unchecked")
	public List <Account> findAll() {
		String jpql = "SELECT a FROM Account AS a ORDER BY a.name";
		Query q = em.createQuery(jpql);
		return q.getResultList();
	}

	public Account findById(long id) {
		String jpql = "SELECT a FROM Account AS a WHERE a.id = :id";
		Query q = em.createQuery(jpql);
		q.setParameter("id", id);
		return (Account) q.getSingleResult();
	}

	public boolean existsTransactionByAccount(Account m) {
		String jpql = "SELECT t.id FROM Transaction AS t WHERE t.accountFrom = :account or t.accountTo = :account";
		Query q = em.createQuery(jpql);
		q.setParameter("account", m);
		q.setMaxResults(1);
		return !q.getResultList().isEmpty();
	}
}
