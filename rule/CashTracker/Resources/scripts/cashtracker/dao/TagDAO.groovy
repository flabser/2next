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

	public List <Tag> findAll() {
		List <Tag> result = db.select("SELECT * FROM tags", Tag.class, user)
		return result
	}

	public Tag findById(long id) {
		List <Tag> list = db.select("select * from tags where id = $id", Tag.class, user)
		Tag result = list.size() ? list[0] : null
		return result
	}

	public int addTag(Tag m) {
		String sql = "insert into tags (name) values ('${m.name}')"
		return db.insert(sql, user)
	}

	public void updateTag(Tag m) {
		String sql = "update tags set name = '${m.name}' where id = ${m.id}"
		db.update(sql, user)
	}

	public void deleteTag(Tag m) {
		String sql = "delete from tags where id = ${m.id}"
		db.delete(sql, user)
	}
}
