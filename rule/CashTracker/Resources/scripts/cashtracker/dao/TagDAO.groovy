package cashtracker.dao

import cashtracker.model.Tag

import com.flabser.dataengine.IDatabase
import com.flabser.script._Session
import com.flabser.users.User


public class TagDAO {

	private IDatabase db
	private User user

	public TagDAO(_Session session) {
		this.db = session.getDatabase()
		this.user = session.getUser()
	}

	public String getSelectQuery() {
		return "SELECT id, name, color FROM tags"
	}

	public String getCreateQuery() {
		return "INSERT INTO tags (name, color) VALUES (?, ?)"
	}

	public String getUpdateQuery() {
		return "UPDATE tags SET name = ?, color = ? WHERE id = ?"
	}

	public String getDeleteQuery() {
		return "DELETE FROM tags WHERE id = ";
	}

	public List <Tag> findAll() {
		List <Tag> result = db.select(getSelectQuery(), Tag.class, user)
		return result
	}

	public Tag findById(long id) {
		List <Tag> list = db.select(getSelectQuery() + " WHERE id = $id", Tag.class, user)
		Tag result = list.size() ? list[0] : null
		return result
	}

	public int add(Tag m) {
		String sql = "INSERT INTO tags (name, color) VALUES ('${m.name}', ${m.color})"
		return db.insert(sql, user)
	}

	public void update(Tag m) {
		String sql = "UPDATE tags SET name = '${m.name}', color = ${m.color} WHERE id = ${m.id}"
		db.update(sql, user)
	}

	public void delete(Tag m) {
		String sql = getDeleteQuery() + m.id
		db.delete(sql, user)
	}
}
