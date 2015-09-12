package cashtracker.dao;

import java.util.List;

import javax.persistence.Query;

import cashtracker.model.Budget;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.dataengine.jpa.IAppEntity;
import com.flabser.script._Session;


public class BudgetDAO extends DAO {

	public BudgetDAO(_Session session) {
		super(session);
	}

	@SuppressWarnings("unchecked")
	public List <Budget> findAll() {
		String jpql = "SELECT b FROM Budget AS b";
		Query q = em.createQuery(jpql);
		return q.getResultList();
	}

	public void delete() {
		em.getTransaction().begin();
		em.createQuery("DELETE FROM Budget").executeUpdate();
		em.getTransaction().commit();
	}

	@Override
	public IAppEntity findById(long id) {
		return null;
	}
}
