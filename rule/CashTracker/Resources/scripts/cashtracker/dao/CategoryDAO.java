package cashtracker.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;

import cashtracker.model.Category;
import cashtracker.model.Transaction;
import cashtracker.model.constants.TransactionType;


public class CategoryDAO extends DAO <Category, Long> {

	public CategoryDAO(_Session session) {
		super(Category.class, session);
	}

	public List <Category> findByTransactionType(TransactionType type) {
		String jpql = "SELECT c FROM Category AS c WHERE c.parent IS NULL AND c.transactionType = :type ORDER BY c.name";

		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			TypedQuery <Category> q = em.createQuery(jpql, Category.class);
			q.setParameter("type", type);
			return q.getResultList();
		} finally {
			em.close();
		}
	}

	public boolean existsTransactionByCategory(Category m) {
		String jpql = "SELECT t.id FROM Transaction AS t WHERE t.category = :category";

		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createQuery(jpql, Transaction.class);
			q.setParameter("category", m);
			q.setMaxResults(1);
			return !q.getResultList().isEmpty();
		} finally {
			em.close();
		}
	}

	public boolean existsChildCategory(Category m) {
		String jpql = "SELECT c FROM Category AS c WHERE c.parent = :category";

		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createQuery(jpql, Category.class);
			q.setParameter("category", m);
			q.setMaxResults(1);
			return !q.getResultList().isEmpty();
		} finally {
			em.close();
		}
	}
}
