package com.flabser.dataengine;

import com.flabser.dataengine.pool.DBConnectionPool;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.pool.IDBConnectionPool;
import com.flabser.dataengine.system.entities.ApplicationProfile;

public abstract class DatabaseCore {
	protected IDBConnectionPool pool;
	
	protected IDBConnectionPool getPool(String driver, ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException, ClassNotFoundException, DatabasePoolException {
		pool = new DBConnectionPool(); 
		//appProfile.dbLogin = "postgres"; //temporary login
		pool.initConnectionPool(driver, appProfile.getURI(), appProfile.dbLogin, appProfile.dbPwd);
		return pool;
	}
	
	
}
