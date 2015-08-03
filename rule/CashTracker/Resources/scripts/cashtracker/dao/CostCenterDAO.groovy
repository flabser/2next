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
		this.user = session.getUser()
	}

	public String getSelectQuery() {
		return "SELECT * FROM costcenters";
	}

	public String getCreateQuery() {
		return "insert into costcenters...";
	}

	public String getUpdateQuery() {
		return "update costcenters...";
	}

	public String getDeleteQuery() {
		return "delete from costcenters where id = ";
	}

	public List <CostCenter> findAll() {
		List <CostCenter> result = db.select(getSelectQuery(), CostCenter.class, user)
		return result
	}

	public CostCenter findById(long id) {
		List <CostCenter> list = db.select(getSelectQuery() + " where id = $id", CostCenter.class, user)
		CostCenter result = list.size() ? list[0] : null
		return result
	}

	public int add(CostCenter m) {
		String sql = "insert into costcenters (name) values ('${m.name}')"
		return db.insert(sql, user)
	}

	public void update(CostCenter m) {
		String sql = "update costcenters set name = '${m.name}' where id = ${m.id}"
		db.update(sql, user)
	}

	public void delete(CostCenter m) {
		String sql = getDeleteQuery() + m.id
		db.delete(sql, user)
	}
}
