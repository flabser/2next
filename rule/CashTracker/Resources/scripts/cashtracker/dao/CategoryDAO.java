package cashtracker.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import cashtracker.model.Category;

import com.flabser.script._Session;
import com.flabser.users.User;


public class CategoryDAO {

	private EntityManager em;
	private User user;

	public CategoryDAO(_Session session) {
		this.user = session.getAppUser();
		this.em = session.getDatabase().getEntityManager();
	}

	@SuppressWarnings("unchecked")
	public List <Category> findAll() {
		String jpql = "SELECT c FROM Category AS c ORDER BY c.sortOrder, c.name";
		Query q = em.createQuery(jpql);
		return q.getResultList();
	}

	public Category findById(long id) {
		String jpql = "SELECT c FROM Category AS c WHERE c.id = :id";
		Query q = em.createQuery(jpql);
		q.setParameter("id", id);
		return (Category) q.getSingleResult();
	}

	public boolean existsTransactionByCategory(Category m) {
		String jpql = "SELECT t.id FROM Transaction AS t WHERE t.category = :category";
		Query q = em.createQuery(jpql);
		q.setParameter("category", m);
		q.setMaxResults(1);
		return !q.getResultList().isEmpty();
	}

	public boolean existsChildCategory(Category m) {
		return !m.getChildren().isEmpty();
	}

	public Category add(Category m) {
		em.getTransaction().begin();
		em.persist(m);
		em.getTransaction().commit();
		return m;
	}

	public Category update(Category m) {
		em.getTransaction().begin();
		em.merge(m);
		em.getTransaction().commit();
		return m;
	}

	public void delete(Category m) {
		em.getTransaction().begin();
		em.remove(m);
		em.getTransaction().commit();
	}
}
