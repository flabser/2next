package cashtracker.dao

import cashtracker.model.CostCenter

import com.flabser.dataengine.IDatabase
import com.flabser.script._Session
import com.flabser.users.User


public class CostCenterDAO {

	private IDatabase db
	private User user

	public CostCenterDAO(_Session session) {
		this.db = session.getDatabase()
		this.user = session.getAppUser()
	}

	public String getSelectQuery() {
		return "SELECT id, name FROM costcenters"
	}

	public String getCreateQuery() {
		return "INSERT INTO costcenters (name) VALUES (?)"
	}

	public String getUpdateQuery() {
		return "UPDATE costcenters SET name = ? WHERE id = ?"
	}

	public String getDeleteQuery() {
		return "DELETE FROM costcenters WHERE id = "
	}

	public List <CostCenter> findAll() {
		List <CostCenter> result = db.select(getSelectQuery(), CostCenter.class, user)
		return result
	}

	public CostCenter findById(long id) {
		List <CostCenter> list = db.select(getSelectQuery() + " WHERE id = $id", CostCenter.class, user)
		CostCenter result = list.size() ? list[0] : null
		return result
	}

	public int add(CostCenter m) {
		String sql = "INSERT INTO costcenters (name) VALUES ('${m.name}')"
		return db.insert(sql, user)
	}

	public void update(CostCenter m) {
		String sql = "UPDATE costcenters SET name = '${m.name}' WHERE id = ${m.id}"
		db.update(sql, user)
	}

	public void delete(CostCenter m) {
		String sql = getDeleteQuery() + m.id
		db.delete(sql, user)
	}
}
