package com.flabser.solutions.postgresql;

import java.text.SimpleDateFormat;

import com.flabser.dataengine.DatabaseCore;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.pool.IDBConnectionPool;
import com.flabser.dataengine.system.entities.ApplicationProfile;


public class Database extends DatabaseCore implements IDatabase {

	public static final SimpleDateFormat sqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final String driver = "org.postgresql.Driver";

	@Override
	public void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException, ClassNotFoundException, DatabasePoolException {
		super.appProfile = appProfile;
		initConnectivity(driver, appProfile);
	}

	@Override
	public int getVersion() {
		return 1;
	}



	@Override
	public void shutdown() {
		pool.closeAll();
	}

	@Override
	public IDBConnectionPool getPool() {
		return pool;
	}

	@Override
	public IFTIndexEngine getFTSearchEngine() {
		// TODO Auto-generated method stub
		return null;
	}


}
