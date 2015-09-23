package nubis.dao;

import java.util.List;

import javax.persistence.Query;

import nubis.model.Profile;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class ProfileDAO extends DAO <Profile> {

	public ProfileDAO(_Session session) {
		super(Profile.class, session);
	}

	@SuppressWarnings("unchecked")
	public List <Profile> findAll() {
		String jpql = "SELECT a FROM Profile AS a";
		Query q = em.createQuery(jpql);
		return q.getResultList();
	}
}
