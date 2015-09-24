package nubis.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import nubis.model.Profile;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class ProfileDAO extends DAO <Profile> {

	public ProfileDAO(_Session session) {
		super(Profile.class, session);
	}

	public List <Profile> findAll() {
		String jpql = "SELECT a FROM Profile AS a";
		TypedQuery <Profile> q = em.createQuery(jpql, Profile.class);
		return q.getResultList();
	}
}
