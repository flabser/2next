package cashtracker.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import cashtracker.model.Tag;

import com.flabser.dataengine.IDatabase;
import com.flabser.restful.data.IAppEntity;
import com.flabser.script._Session;
import com.flabser.users.User;


public class TagDAO {

	private IDatabase db;
	private User user;

	public TagDAO(_Session session) {
		this.db = session.getDatabase();
		this.user = session.getAppUser();
	}

	public String getSelectQuery() {
		return "SELECT t FROM Tag AS t";
	}

	public List <IAppEntity> findAll() {
		List <IAppEntity> result = db.select(getSelectQuery() + " ORDER BY t.name", user);
		return result;
	}

	public Tag findById(long id) {
		List <IAppEntity> list = db.select(getSelectQuery() + " WHERE t.id = " + id + " ORDER BY t.name", user);
		Tag result = list.size() > 0 ? (Tag) list.get(0) : null;
		return result;
	}

	public boolean existsTransactionByTag(Tag m) {
		String jpql = "SELECT t.id FROM Transaction AS t WHERE :tag MEMBER OF t.tags";

		EntityManager em = db.getEntityManager();
		Query q = em.createQuery(jpql);
		q.setParameter("tag", m);
		q.setMaxResults(1);
		return q.getResultList().size() > 0;
	}

	public Tag add(Tag m) {
		return (Tag) db.insert(m, user);
	}

	public Tag update(Tag m) {
		return (Tag) db.update(m, user);
	}

	public void delete(Tag m) {
		db.delete(m, user);
	}
}
