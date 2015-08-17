package cashtracker.dao;

import java.util.List;

import cashtracker.model.Budget;

import com.flabser.dataengine.IDatabase;
import com.flabser.restful.data.IAppEntity;
import com.flabser.script._Session;
import com.flabser.users.User;


public class BudgetDAO {

	private IDatabase db;
	private User user;

	public BudgetDAO(_Session session) {
		this.db = session.getDatabase();
		this.user = session.getAppUser();
	}

	public String getSelectQuery() {
		return "SELECT b FROM Budget AS b";
	}

	public String getDeleteQuery() {
		return "DELETE FROM Budget AS b";
	}

	public List <IAppEntity> findAll() {
		List <IAppEntity> result = db.select(getSelectQuery(), user);
		return result;
	}

	public Budget add(Budget m) {
		return (Budget) db.insert(m, user);
	}

	public Budget update(Budget m) {
		return (Budget) db.update(m, user);
	}

	public void delete() {
		db.delete(getDeleteQuery(), user);
	}
}
