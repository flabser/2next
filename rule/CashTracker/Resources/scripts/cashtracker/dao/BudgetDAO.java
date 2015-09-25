package cashtracker.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import cashtracker.model.Budget;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class BudgetDAO extends DAO <Budget> {

	public BudgetDAO(_Session session) {
		super(Budget.class, session);
	}

	public List <Budget> findAll() {
		String jpql = "SELECT b FROM Budget AS b";
		TypedQuery <Budget> q = em.createQuery(jpql, Budget.class);
		return q.getResultList();
	}

	public void delete() {
		em.getTransaction().begin();
		em.createQuery("DELETE FROM Budget").executeUpdate();
		em.getTransaction().commit();
	}
}
