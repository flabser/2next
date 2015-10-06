package cashtracker.dao;

import cashtracker.model.Budget;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class BudgetDAO extends DAO <Budget, Long> {

	public BudgetDAO(_Session session) {
		super(Budget.class, session);
	}

	public void delete() {
		em.getTransaction().begin();
		em.createQuery("DELETE FROM Budget as m", Budget.class).executeUpdate();
		em.getTransaction().commit();
	}
}
