package cashtracker.dao;

import java.util.List;

import javax.persistence.Query;

import cashtracker.helper.PageRequest;
import cashtracker.model.Tag;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class TagDAO extends DAO <Tag> {

	public TagDAO(_Session session) {
		super(Tag.class, session);
	}

	@SuppressWarnings("unchecked")
	public List <Tag> findAll() {
		String jpql = "SELECT t FROM Tag AS t ORDER BY t.name";
		Query q = em.createQuery(jpql);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List <Tag> findAll(PageRequest pr) {
		String jpql = "SELECT t FROM Tag AS t ORDER BY t.name";
		Query q = em.createQuery(jpql);
		q.setFirstResult(pr.getOffset());
		q.setMaxResults(pr.getLimit());
		return q.getResultList();
	}

	public boolean existsTransactionByTag(Tag m) {
		String jpql = "SELECT t.id FROM Transaction AS t WHERE :tag MEMBER OF t.tags";
		Query q = em.createQuery(jpql);
		q.setParameter("tag", m);
		q.setMaxResults(1);
		return !q.getResultList().isEmpty();
	}
}
