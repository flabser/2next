package com.flabser.solutions.cashtracker;

import java.sql.ResultSet;

import com.flabser.dataengine.DatabaseCore;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.IDeployer;
import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.users.ApplicationProfile;
import com.flabser.users.User;

public class Database extends DatabaseCore implements IDatabase {
	public static final String driver = "org.postgresql.Driver";
	
	@Override
	public void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, DatabasePoolException {
		pool = getPool(driver, appProfile);		
		pool.initConnectionPool(driver, appProfile.dbURL, appProfile.dbLogin, appProfile.dbPwd);
		
	}
		
	@Override
	public int getVersion() {
		return 1;
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

	@Override
	public IDeployer getDeployer() {
		return new Deployer();
	}



}
