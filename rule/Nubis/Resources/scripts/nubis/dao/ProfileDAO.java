package nubis.dao;

import java.util.List;

import javax.persistence.Query;

import nubis.model.Profile;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class ProfileDAO extends DAO {

	public ProfileDAO(_Session session) {
		super(session);
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
}
