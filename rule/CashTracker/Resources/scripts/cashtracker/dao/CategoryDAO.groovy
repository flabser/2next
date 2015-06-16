package cashtracker.dao;

import java.sql.ResultSet;
import java.util.List;

import cashtracker.model.Category;

import com.flabser.dataengine.IDatabase;
import com.flabser.script._Session
import com.flabser.users.User;


public class CategoryDAO {

	private IDatabase db;
	private User user;

	public CategoryDAO(_Session session) {
		this.db = session.getDatabase();
		this.user = session.getUser();
	}

	public List <Category> findAll() {
		List <Category> result = db.select("SELECT * FROM category", Category.class, user);
		return result;
	}

	public Category findById(long id) {
		List <Category> list = db.select("select * from category where id = $id", Category.class, user)

		Category result = null

		if (list.size() > 0) {
			result = list[0]
		}

		return result
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
