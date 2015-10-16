package com.flabser.dataengine.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.flabser.script._Session;
import com.flabser.users.User;

public abstract class DAO<T extends IAppEntity, K> implements IDAO<T, K> {

	protected EntityManagerFactory factory;
	/**
	 * em was deprecated Use factory instead this one to get the EntityManager
	 * in a body of the method
	 *
	 * @deprecated
	 */
	@Deprecated
	protected EntityManager em;
	private Class<T> entityClass;
	protected User user;

	public DAO(Class<T> entityClass, _Session session) {
		this.user = session.getUser();
		factory = session.getDatabase().getEntityManagerFactory();
		this.em = session.getDatabase().getEntityManager();
		this.entityClass = entityClass;
	}

	@Override
	public T findById(K id) {
		EntityManager em = factory.createEntityManager();
		try {
			String jpql = "SELECT m FROM " + entityClass.getName() + " AS m WHERE m.id = :id";
			TypedQuery<T> q = em.createQuery(jpql, entityClass);
			q.setParameter("id", id);
			return q.getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public List<T> findAll() {
		EntityManager em = factory.createEntityManager();
		try {
			TypedQuery<T> q = em.createNamedQuery(getQueryNameForAll(), entityClass);
			return q.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public T add(T entity) {
		EntityManager em = factory.createEntityManager();
		try {
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				entity.setAuthor(user.id);
				em.persist(entity);
				t.commit();
				return entity;
			} finally {
				if (t.isActive()) {
					t.rollback();
				}
			}
		} finally {
			em.close();
		}
	}

	@Override
	public T update(T entity) {
		EntityManager em = factory.createEntityManager();
		try {
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				em.merge(entity);
				t.commit();
				return entity;
			} finally {
				if (t.isActive()) {
					t.rollback();
				}
			}
		} finally {
			em.close();
		}
	}

	@Override
	public void delete(T entity) {
		EntityManager em = factory.createEntityManager();
		try {
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				em.remove(entity);
				t.commit();
			} finally {
				if (t.isActive()) {
					t.rollback();
				}
			}
		} finally {
			em.close();
		}
	}

	public Long getCount() {
		EntityManager em = factory.createEntityManager();
		try {
			Query q = em.createQuery("SELECT count(m) FROM " + entityClass.getName() + " AS m");
			return (Long) q.getSingleResult();
		} finally {
			em.close();
		}
	}

	public String getQueryNameForAll() {
		String queryName = entityClass.getSimpleName() + ".findAll";
		return queryName;
	}
}
