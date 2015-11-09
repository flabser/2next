package task.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;

import task.model.Issue;
import task.model.Tag;


public class TagDAO extends DAO <Tag, Long> {

	public TagDAO(_Session session) {
		super(Tag.class, session);
	}

	public boolean existsChildTag(Tag m) {
		String jpql = "SELECT m FROM Tag AS m WHERE m.parent = :tag";

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

	public boolean existsIssuesByTag(Tag m) {
		String jpql = "SELECT m.id FROM Issue AS m WHERE :tag MEMBER OF m.tags";

		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createQuery(jpql, Issue.class);
			q.setParameter("tag", m);
			q.setMaxResults(1);
			return !q.getResultList().isEmpty();
		} finally {
			em.close();
		}
	}
}
