package cashtracker.dao;

import java.util.List;

import javax.persistence.EntityManager;

import cashtracker.model.Budget;

import com.flabser.dataengine.IDatabase;
import com.flabser.restful.data.IAppEntity;
import com.flabser.script._Session;
import com.flabser.users.User;


public class BudgetDAO {

	private EntityManager em;
	private IDatabase db;
	private User user;

	public BudgetDAO(_Session session) {
		this.db = session.getDatabase();
		this.user = session.getAppUser();
		this.em = db.getEntityManager();
	}

	public String getSelectQuery() {
		return "SELECT b FROM Budget AS b";
	}

	public List <IAppEntity> findAll() {
		List <IAppEntity> result = db.select(getSelectQuery(), user);
		return result;
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
		em.createQuery("DELETE FROM Budget AS b").executeUpdate();
		em.getTransaction().commit();
	}
}
