package cashtracker.dao;

import java.util.List;

import cashtracker.model.Category;

import com.flabser.dataengine.IDatabase;
import com.flabser.restful.data.IAppEntity;
import com.flabser.script._Session;
import com.flabser.users.User;


public class CategoryDAO {

	private IDatabase db;
	private User user;

	public CategoryDAO(_Session session) {
		this.db = session.getDatabase();
		this.user = session.getAppUser();
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

	public Category add(Category m) {
		return (Category) db.insert(m, user);
	}

	public Category update(Category m) {
		return (Category) db.update(m, user);
	}

	public void delete(Category m) {
		db.delete(m, user);
	}
}
