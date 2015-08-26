package nubis.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import nubis.model.Profile;

import com.flabser.script._Session;
import com.flabser.users.User;


public class ProfileDAO {

	private EntityManager em;
	private User user;

	public ProfileDAO(_Session session) {
		this.user = session.getAppUser();
		this.em = session.getDatabase().getEntityManager();
	}

	@SuppressWarnings("unchecked")
	public List <Profile> findAll() {
		String jpql = "SELECT a FROM Profile AS a";
		Query q = em.createQuery(jpql);
		return q.getResultList();
	}

	public Profile findById(long id) {
		String jpql = "SELECT a FROM Profile AS a WHERE a.id = :id";
		Query q = em.createQuery(jpql);
		q.setParameter("id", id);
		return (Profile) q.getSingleResult();
	}

	public Profile add(Profile m) {
		em.getTransaction().begin();
		em.persist(m);
		em.getTransaction().commit();
		return m;
	}

	public Profile update(Profile m) {
		em.getTransaction().begin();
		em.merge(m);
		em.getTransaction().commit();
		return m;
	}

	public void delete(Profile m) {
		em.getTransaction().begin();
		em.remove(m);
		em.getTransaction().commit();
	}
}
