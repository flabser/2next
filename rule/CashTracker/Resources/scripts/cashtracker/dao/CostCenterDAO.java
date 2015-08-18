package cashtracker.dao;

import java.util.List;

import cashtracker.model.CostCenter;

import com.flabser.dataengine.IDatabase;
import com.flabser.restful.data.IAppEntity;
import com.flabser.script._Session;
import com.flabser.users.User;


public class CostCenterDAO {

	private IDatabase db;
	private User user;

	public CostCenterDAO(_Session session) {
		this.db = session.getDatabase();
		this.user = session.getAppUser();
	}

	public String getSelectQuery() {
		return "SELECT cc FROM CostCenter AS cc";
	}

	public List <IAppEntity> findAll() {
		List <IAppEntity> result = db.select(getSelectQuery(), user);
		return result;
	}

	public CostCenter findById(long id) {
		List <IAppEntity> list = db.select(getSelectQuery() + " WHERE cc.id = " + id, user);
		CostCenter result = list.size() > 0 ? (CostCenter) list.get(0) : null;
		return result;
	}

	public CostCenter add(CostCenter m) {
		return (CostCenter) db.insert(m, user);
	}

	public CostCenter update(CostCenter m) {
		return (CostCenter) db.update(m, user);
	}

	public void delete(CostCenter m) {
		db.delete(m, user);
	}
}
