package cashtracker.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;

import cashtracker.model.Budget;


public class BudgetDAO extends DAO <Budget, Long> {

	public BudgetDAO(_Session session) {
		super(Budget.class, session);
	}

	public void delete() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				em.createQuery("DELETE FROM Budget as m", Budget.class).executeUpdate();
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
}
