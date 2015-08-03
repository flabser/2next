package cashtracker.dao;

import cashtracker.model.Category

import com.flabser.dataengine.IDatabase
import com.flabser.script._Session
import com.flabser.users.User


public class CategoryDAO {

	private IDatabase db;
	private User user;

	public CategoryDAO(_Session session) {
		this.db = session.getDatabase();
		this.user = session.getUser();
	}

	public List <Category> findAll() {
		List <Category> result = db.select("SELECT * FROM categories", Category.class, user);
		return result;
	}

	public Category findById(long id) {
		List <Category> list = db.select("select * from categories where id = $id", Category.class, user)
		Category result = list.size() ? list[0] : null
		return result
	}

	public int add(Category m) {
		String sql = """insert into categories
							(transaction_type, parent_id,
							name, note, color, sort_order)
						values
							(${m.transactionType}, ${m.parentCategory.id},
							'${m.name}', '${m.note}', ${m.color}, ${m.sortOrder})""";
		return db.insert(sql, user);
	}

	public void update(Category m) {
		String sql = """update categories
						set
							transaction_type = ${m.transactionType},
							parent_id = ${m.parentCategory.id},
							name = '${m.name}',
							note = '${m.note}',
							color = ${m.color},
							sort_order = ${m.sortOrder}
						where id = ${m.id}"""
		db.update(sql, user);
	}

	public void delete(Category m) {
		String sql = "delete from categories where id = ${m.id}"
		db.delete(sql, user)
	}
}
