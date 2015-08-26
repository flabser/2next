package cashtracker.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import cashtracker.model.Budget;

import com.flabser.script._Session;
import com.flabser.users.User;


public class BudgetDAO {

	private EntityManager em;
	private User user;

	public BudgetDAO(_Session session) {
		this.user = session.getAppUser();
		this.em = session.getDatabase().getEntityManager();
	}

	@SuppressWarnings("unchecked")
	public List <Budget> findAll() {
		String jpql = "SELECT b FROM Budget AS b";
		Query q = em.createQuery(jpql);
		return q.getResultList();
	}

	public Budget add(Budget m) {
		em.getTransaction().begin();
		em.persist(m);
		em.getTransaction().commit();
		return m;
	}

	public Budget update(Budget m) {
		em.getTransaction().begin();
		em.merge(m);
		em.getTransaction().commit();
		return m;
	}

	public void delete() {
		em.getTransaction().begin();
		em.createQuery("DELETE FROM Budget").executeUpdate();
		em.getTransaction().commit();
	}
}
