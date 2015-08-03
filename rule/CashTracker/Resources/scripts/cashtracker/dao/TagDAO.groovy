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
		return "SELECT * FROM tags";
	}

	public String getCreateQuery() {
		return "insert into tags...";
	}

	public String getUpdateQuery() {
		return "update tags...";
	}

	public String getDeleteQuery() {
		return "delete from tags where id = ";
	}

	public List <Tag> findAll() {
		List <Tag> result = db.select(getSelectQuery(), Tag.class, user)
		return result
	}

	public Tag findById(long id) {
		List <Tag> list = db.select(getSelectQuery() + " where id = $id", Tag.class, user)
		Tag result = list.size() ? list[0] : null
		return result
	}

	public int add(Tag m) {
		String sql = "insert into tags (name, color) values ('${m.name}', ${m.color})"
		return db.insert(sql, user)
	}

	public void update(Tag m) {
		String sql = "update tags set name = '${m.name}', color = ${m.color} where id = ${m.id}"
		db.update(sql, user)
	}

	public void delete(Tag m) {
		String sql = getDeleteQuery() + m.id
		db.delete(sql, user)
	}
}
