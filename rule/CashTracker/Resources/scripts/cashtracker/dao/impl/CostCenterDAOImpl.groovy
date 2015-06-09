package cashtracker.dao.impl

import java.sql.ResultSet
import java.util.List

import cashtracker.dao.CostCenterDAO
import cashtracker.model.Budget
import cashtracker.model.CostCenter

import com.flabser.dataengine.IDatabase
import com.flabser.script._Session
import com.flabser.users.User


public class CostCenterDAOImpl implements CostCenterDAO {

	private IDatabase db
	private User user

	public CostCenterDAOImpl(_Session session) {
		this.db = session.getDatabase()
		this.user = session.getUser()
	}

	public List <CostCenter> findAll() {
		ResultSet rs = db.select("SELECT * FROM costcenter", user)
		List <CostCenter> result = []

		if (rs.next()) {
			result << getModelFromResultSet(rs)
		}

		return result
	}

	public CostCenter findById(long id) {
		String sql = "select * from costcenter where id = $id"
		ResultSet rs = db.select(sql, user)
		CostCenter result = null

		if (rs.next()) {
			result = getModelFromResultSet(rs)
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
