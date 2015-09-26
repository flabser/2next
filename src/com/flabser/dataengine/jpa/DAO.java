package com.flabser.dataengine.jpa;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.flabser.script._Session;
import com.flabser.users.User;


public abstract class DAO<T extends IAppEntity, K> implements IDAO <T, K> {

	protected EntityManager em;
	private Class <T> entityClass;
	protected User user;

	public DAO(Class <T> entityClass, _Session session) {
		this.user = session.getAppUser();
		this.em = session.getDatabase().getEntityManager();
		this.entityClass = entityClass;
	}

	public String getEntityClassName() {
		return entityClass.getName();
	}

	@Override
	public T findById(K id) {
		String jpql = "SELECT m FROM " + getEntityClassName() + " AS m WHERE m.id = :id";
		TypedQuery <T> q = em.createQuery(jpql, entityClass);
		q.setParameter("id", id);
		return q.getSingleResult();
	}

	public T add(T entity) {
		em.getTransaction().begin();
		entity.setAuthor(user.id);
		em.persist(entity);
		em.getTransaction().commit();
		return entity;
	}

	public T update(T entity) {
		em.getTransaction().begin();
		em.merge(entity);
		em.getTransaction().commit();
		return entity;
	}

	public void delete(T entity) {
		em.getTransaction().begin();
		em.remove(entity);
		em.getTransaction().commit();
	}

	public Long getCount() {
		Query q = em.createQuery("SELECT count(m) FROM " + getEntityClassName() + " AS m");
		return (Long) q.getSingleResult();
	}
}
