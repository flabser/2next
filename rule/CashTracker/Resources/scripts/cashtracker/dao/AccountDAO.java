package cashtracker.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;

import cashtracker.model.Account;
import cashtracker.model.Transaction;


public class AccountDAO extends DAO <Account, Long> {

	public AccountDAO(_Session session) {
		super(Account.class, session);
	}

	public List <Account> findAllEnabled() {
		String jpql = "SELECT a FROM Account AS a WHERE a.enabled = true ORDER BY a.name";

		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			TypedQuery <Account> q = em.createQuery(jpql, Account.class);
			return q.getResultList();
		} finally {
			em.close();
		}
	}

	public boolean existsTransactionByAccount(Account m) {
		String jpql = "SELECT t.id FROM Transaction AS t WHERE t.account = :account OR t.transferAccount = :account";

		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createQuery(jpql, Transaction.class);
			q.setParameter("account", m);
			q.setMaxResults(1);
			return !q.getResultList().isEmpty();
		} finally {
			em.close();
		}
	}
}
