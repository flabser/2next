package cashtracker.dao.impl;

import java.sql.ResultSet;
import java.util.List;

import cashtracker.dao.CostCenterDAO;
import cashtracker.model.CostCenter;

import com.flabser.dataengine.IDatabase;
import com.flabser.script._Session
import com.flabser.users.User;


public class CostCenterDAOImpl implements CostCenterDAO {

	private IDatabase db;
	private User user;

	public CostCenterDAOImpl(_Session session) {
		this.db = session.getDatabase();
		this.user = session.getUser();
	}

	public List <CostCenter> findAll() {
		List <CostCenter> result = null;
		ResultSet rs = db.select("SELECT * FROM costcenter", user);
		// ...
		return result;
	}

	public CostCenter findById(long id) {
		String sql = "select * from costcenter where id = " + id;
		CostCenter result = null;
		ResultSet rs = db.select(sql, user);
		// ...
		return result;
	}

	public int addCostCenter(CostCenter c) {
		String sql = "insert into costcenter (...) values (...)";
		return db.insert(sql, user);
	}

	public void updateCostCenter(CostCenter c) {
		String sql = "update costcenter set ... where id=";
		db.update(sql, user);
	}

	public void deleteCostCenter(CostCenter c) {
		String sql = "delete from costcenter where id = " + c.getId();
		db.delete(sql, user);
	}
}
