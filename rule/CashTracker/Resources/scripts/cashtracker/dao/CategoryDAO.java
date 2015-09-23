package cashtracker.dao;

import java.util.List;

import javax.persistence.Query;

import cashtracker.model.Category;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class CategoryDAO extends DAO <Category> {

	public CategoryDAO(_Session session) {
		super(Category.class, session);
	}

	@SuppressWarnings("unchecked")
	public List <Category> findAll() {
		String jpql = "SELECT c FROM Category AS c WHERE c.parent IS NULL ORDER BY c.name";
		Query q = em.createQuery(jpql);
		return q.getResultList();
	}

	public boolean existsTransactionByCategory(Category m) {
		String jpql = "SELECT t.id FROM Transaction AS t WHERE t.category = :category";
		Query q = em.createQuery(jpql);
		q.setParameter("category", m);
		q.setMaxResults(1);
		return !q.getResultList().isEmpty();
	}

	public boolean existsChildCategory(Category m) {
		return !m.getChildren().isEmpty();
	}
}
