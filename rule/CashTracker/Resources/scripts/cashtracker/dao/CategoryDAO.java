package cashtracker.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import cashtracker.model.Category;

import com.flabser.dataengine.IDatabase;
import com.flabser.restful.data.IAppEntity;
import com.flabser.script._Session;
import com.flabser.users.User;


public class CategoryDAO {

	private EntityManager em;
	private IDatabase db;
	private User user;

	public CategoryDAO(_Session session) {
		this.db = session.getDatabase();
		this.user = session.getAppUser();
		this.em = db.getEntityManager();
	}

	public String getSelectQuery() {
		return "SELECT c FROM Category AS c";
	}

	public List <IAppEntity> findAll() {
		List <IAppEntity> result = db.select(getSelectQuery() + " ORDER BY c.sortOrder, c.name", user);
		return result;
	}

	public Category findById(long id) {
		List <IAppEntity> list;
		list = db.select(getSelectQuery() + " WHERE c.id = " + id + " ORDER BY c.sortOrder, c.name", user);
		Category result = list.size() > 0 ? (Category) list.get(0) : null;
		return result;
	}

	public boolean existsTransactionByCategory(Category m) {
		String jpql = "SELECT t.id FROM Transaction AS t WHERE t.category = :category";

		Query q = em.createQuery(jpql);
		q.setParameter("category", m);
		q.setMaxResults(1);
		return q.getResultList().size() > 0;
	}

	public boolean existsChildCategory(Category m) {
		return m.getChildren().size() > 0;
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
