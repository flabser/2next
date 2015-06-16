package cashtracker.dao

import java.sql.ResultSet
import java.util.List

import cashtracker.model.Budget
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
		List <CostCenter> result = db.select("SELECT * FROM costcenter", CostCenter.class, user)
		return result
	}

	public CostCenter findById(long id) {
		List <CostCenter> list = db.select("select * from costcenter where id = $id", CostCenter.class, user)

		CostCenter result = null

		if (list.size() > 0) {
			result = list[0]
		}

		return result
	}

	public int addCostCenter(CostCenter c) {
		String sql = "insert into costcenter (type, name) values (${c.type}, '${c.name}')"
		return db.insert(sql, user)
	}

	public void updateCostCenter(CostCenter c) {
		String sql = "update costcenter set type = ${c.type}, name = '${c.name}' where id = ${c.id}"
		db.update(sql, user)
	}

	public void deleteCostCenter(CostCenter c) {
		String sql = "delete from costcenter where id = ${c.id}"
		db.delete(sql, user)
	}

	private CostCenter getModelFromResultSet(ResultSet rs){
		CostCenter c = new CostCenter()

		c.setType(rs.getInt("type"))
		c.setName(rs.getString("name"))

		return c
	}
}
