package cashtracker.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import cashtracker.model.Tag;

import com.flabser.script._Session;
import com.flabser.users.User;


public class TagDAO {

	private EntityManager em;
	private User user;

	public TagDAO(_Session session) {
		this.user = session.getAppUser();
		this.em = session.getDatabase().getEntityManager();
	}

	@SuppressWarnings("unchecked")
	public List <Tag> findAll() {
		String jpql = "SELECT t FROM Tag AS t ORDER BY t.name";
		Query q = em.createQuery(jpql);
		return q.getResultList();
	}

	public Tag findById(long id) {
		String jpql = "SELECT t FROM Tag AS t WHERE t.id = :id";
		Query q = em.createQuery(jpql);
		q.setParameter("id", id);
		return (Tag) q.getSingleResult();
	}

	public boolean existsTransactionByTag(Tag m) {
		String jpql = "SELECT t.id FROM Transaction AS t WHERE :tag MEMBER OF t.tags";
		Query q = em.createQuery(jpql);
		q.setParameter("tag", m);
		q.setMaxResults(1);
		return q.getResultList().size() > 0;
	}

	public Tag add(Tag m) {
		em.getTransaction().begin();
		em.persist(m);
		em.getTransaction().commit();
		return m;
	}

	public Tag update(Tag m) {
		em.getTransaction().begin();
		em.merge(m);
		em.getTransaction().commit();
		return m;
	}

	public void delete(Tag m) {
		em.getTransaction().begin();
		em.remove(m);
		em.getTransaction().commit();
	}
}
