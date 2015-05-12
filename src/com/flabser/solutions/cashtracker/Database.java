package com.flabser.solutions.cashtracker;

import java.sql.ResultSet;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.DatabaseCore;
import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DBConnectionPool;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.users.User;

public class Database extends DatabaseCore {
	private static final String driver = "org.postgresql.Driver";
	
	@Override
	public void init(String dbURL, String userName, String password)throws InstantiationException, IllegalAccessException,ClassNotFoundException, DatabasePoolException {
		pool = new DBConnectionPool(); 
		pool.initConnectionPool(driver, dbURL, userName, password);
		
	}
	
	@Override
	public int getVersion() {
		return 1;
	}

	@Override
	public AppEnv getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFTIndexEngine getFTSearchEngine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet select(String condition, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet insert(String condition, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet update(String condition, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet delete(String condition, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

}
