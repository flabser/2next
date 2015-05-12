package com.flabser.dataengine;

import java.sql.ResultSet;
import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.activity.IActivity;
import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.pool.IDBConnectionPool;
import com.flabser.users.User;

public abstract class DatabaseCore  implements IDatabase {
	protected IDBConnectionPool pool;
		
	abstract public void init(String dbURL, String userName, String password) throws InstantiationException, IllegalAccessException, ClassNotFoundException, DatabasePoolException;	
	abstract public int getVersion();
	abstract public AppEnv getParent();
	abstract public IFTIndexEngine getFTSearchEngine();
	abstract public ResultSet select(String condition, User user);
	abstract public ResultSet insert(String condition, User user);
	abstract public ResultSet update(String condition, User user);
	abstract public ResultSet delete(String condition, User user);
	abstract public void shutdown();

	@Override
	public IActivity getActivity() {
		return null;
	}
}
