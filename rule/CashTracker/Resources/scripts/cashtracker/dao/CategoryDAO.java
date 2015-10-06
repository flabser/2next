package cashtracker.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cashtracker.model.Category;
import cashtracker.model.constants.TransactionType;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class CategoryDAO extends DAO <Category, Long> {

	public CategoryDAO(_Session session) {
		super(Category.class, session);
	}

	public List <Category> findByTransactionType(TransactionType type) {
		String jpql = "SELECT c FROM cashtracker.model.Category AS c WHERE c.parent IS NULL AND :type MEMBER OF c.transactionTypes ORDER BY c.name";
		TypedQuery <Category> q = em.createQuery(jpql, Category.class);
		q.setParameter("type", type);
		return q.getResultList();
	}

	public boolean existsTransactionByCategory(Category m) {
		String jpql = "SELECT t.id FROM cashtracker.model.Transaction AS t WHERE t.category = :category";
		Query q = em.createQuery(jpql);
		q.setParameter("category", m);
		q.setMaxResults(1);
		return !q.getResultList().isEmpty();
	}

	public boolean existsChildCategory(Category m) {
		String jpql = "SELECT c FROM cashtracker.model.Category AS c WHERE c.parent = :category";
		Query q = em.createQuery(jpql);
		q.setParameter("category", m);
		q.setMaxResults(1);
		return !q.getResultList().isEmpty();
	}
}
