package task.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;

import task.model.Tag;


public class TagDAO extends DAO <Tag, Long> {

	public TagDAO(_Session session) {
		super(Tag.class, session);
	}

	public boolean existsChildCategory(Tag m) {
		String jpql = "SELECT c FROM Category AS c WHERE c.parent = :category";

		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createQuery(jpql, Tag.class);
			q.setParameter("category", m);
			q.setMaxResults(1);
			return !q.getResultList().isEmpty();
		} finally {
			em.close();
		}
	}
}
