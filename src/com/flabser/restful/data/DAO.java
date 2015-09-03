package com.flabser.restful.data;

import java.util.Date;

import javax.persistence.EntityManager;

import com.flabser.script._Session;
import com.flabser.users.User;

public class DAO {
	protected EntityManager em;
	protected User user;

	public DAO(_Session session) {
		this.user = session.getAppUser();
		this.em = session.getDatabase().getEntityManager();
	}

	public IAppEntity add(IAppEntity entity) {
		em.getTransaction().begin();
		entity.setAuthor(user.id);
		entity.setRegDate(new Date());
		em.persist(entity);
		em.getTransaction().commit();
		return entity;
	}

	public IAppEntity update(IAppEntity entity) {
		em.getTransaction().begin();
		em.merge(entity);
		em.getTransaction().commit();
		return entity;
	}

	public void delete(IAppEntity entity) {
		em.getTransaction().begin();
		em.remove(entity);
		em.getTransaction().commit();
	}
}
