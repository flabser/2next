package cashtracker.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cashtracker.helper.PageRequest;
import cashtracker.model.Tag;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class TagDAO extends DAO <Tag> {

	public TagDAO(_Session session) {
		super(Tag.class, session);
	}

	public List <Tag> findAll() {
		String jpql = "SELECT t FROM Tag AS t ORDER BY t.name";
		TypedQuery <Tag> q = em.createQuery(jpql, Tag.class);
		return q.getResultList();
	}

	public List <Tag> find(PageRequest pr) {
		String jpql = "SELECT t FROM Tag AS t ORDER BY t.name";
		TypedQuery <Tag> q = em.createQuery(jpql, Tag.class);
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
