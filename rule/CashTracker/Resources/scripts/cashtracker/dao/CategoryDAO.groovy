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
		this.user = session.getAppUser();
	}

	public String getSelectQuery() {
		return "SELECT c FROM Category AS c";
	}

	/*public String getCreateQuery() {
		return """INSERT INTO categories
					(transaction_type, parent_id, name, enabled, note, color, sort_order)
				  VALUES (?, ?, ?, ?, ?, ?, ?)"""
	}

	public String getUpdateQuery() {
		return """UPDATE categories
					SET
						transaction_type = ?,
						parent_id = ?,
						name = ?,
						enabled = ?,
						note = ?,
						color = ?,
						sort_order = ?
					WHERE id = ?"""
	}

	public String getDeleteQuery() {
		return "DELETE FROM categories WHERE id = ";
	}*/

	public List <Category> findAll() {
		List <Category> result = db.select(getSelectQuery(), user);
		return result;
	}

	public Category findById(long id) {
		List <Category> list = db.select(getSelectQuery() + " WHERE c.id = $id", user)
		Category result = list.size() ? list[0] : null
		return result
	}

	public Category add(Category m) {
		/*def parentId = m.parentCategory?.id?:null
		String sql = """INSERT INTO categories
							(parent_id, transaction_type,
							name, enabled, note, color, sort_order)
						VALUES
							(${parentId}, null,
							'${m.name}', ${m.enabled}, '${m.note}', ${m.color}, ${m.sortOrder})""";*/
		return db.insert(m, user);
	}

	public Category update(Category m) {
		/*def parentId = m.parentCategory?.id?:null
		String sql = """UPDATE categories
						SET
							parent_id = ${parentId},
							transaction_type = null,
							name = '${m.name}',
							enabled = ${m.enabled},
							note = '${m.note}',
							color = ${m.color},
							sort_order = ${m.sortOrder}
						WHERE id = ${m.id}"""*/
		return db.update(m, user);
	}

	public void delete(Category m) {
		// String sql = getDeleteQuery() + m.id
		db.delete(m, user)
	}
}
