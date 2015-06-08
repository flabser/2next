package cashtracker.dao.impl;

import java.sql.ResultSet;
import java.util.List;

import cashtracker.dao.CategoryDAO;
import cashtracker.model.Category;

import com.flabser.dataengine.IDatabase;
import com.flabser.script._Session
import com.flabser.users.User;


public class CategoryDAOImpl implements CategoryDAO {

	private IDatabase db;
	private User user;

	public CategoryDAOImpl(_Session session) {
		this.db = session.getDatabase();
		this.user = session.getUser();
	}

	public List <Category> findAll() {
		List <Category> result = null;
		ResultSet rs = db.select("SELECT * FROM category", user);
		// ...
		return result;
	}

	public Category findById(long id) {
		String sql = "select * from category where id = " + id;
		Category result = null;
		ResultSet rs = db.select(sql, user);
		// ...
		return result;
	}

	public int addCategory(Category c) {
		String sql = "insert into category (...) values (...)";
		return db.insert(sql, user);
	}

	public void updateCategory(Category c) {
		String sql = "update category set ... where id=";
		db.update(sql, user);
	}

	public void deleteCategory(Category c) {
		String sql = "delete from category where id = " + c.getId();
		db.delete(sql, user);
	}
}
