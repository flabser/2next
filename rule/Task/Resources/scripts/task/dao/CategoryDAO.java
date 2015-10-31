package task.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import task.model.Category;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class CategoryDAO extends DAO <Category, Long> {

	public CategoryDAO(_Session session) {
		super(Category.class, session);
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
