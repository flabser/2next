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

	public List <CostCenter> findAll() {
		List <CostCenter> result = db.select("SELECT * FROM costcenters", CostCenter.class, user)
		return result
	}

	public CostCenter findById(long id) {
		List <CostCenter> list = db.select("select * from costcenters where id = $id", CostCenter.class, user)
		CostCenter result = list.size() ? list[0] : null
		return result
	}

	public int addCostCenter(CostCenter m) {
		String sql = "insert into costcenters (name) values ('${m.name}')"
		return db.insert(sql, user)
	}

	public void updateCostCenter(CostCenter m) {
		String sql = "update costcenters set name = '${m.name}' where id = ${m.id}"
		db.update(sql, user)
	}

	public void deleteCostCenter(CostCenter m) {
		String sql = "delete from costcenters where id = ${m.id}"
		db.delete(sql, user)
	}
}
