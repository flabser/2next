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

	public boolean existsChildTag(Tag m) {
		String jpql = "SELECT m FROM Tag AS c WHERE m.parent = :tag";

		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createQuery(jpql, Tag.class);
			q.setParameter("tag", m);
			q.setMaxResults(1);
			return !q.getResultList().isEmpty();
		} finally {
			em.close();
		}
	}
}
