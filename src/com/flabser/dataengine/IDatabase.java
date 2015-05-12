package com.flabser.dataengine;

import java.sql.ResultSet;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.activity.IActivity;
import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.users.User;

public interface IDatabase {
	
	void init(String dbURL, String userName, String password) throws InstantiationException, IllegalAccessException, ClassNotFoundException, DatabasePoolException;
	int getVersion();   
	AppEnv getParent();
	IFTIndexEngine getFTSearchEngine();
	IActivity getActivity();
	
	ResultSet select(String condition, User user);
	ResultSet insert(String condition, User user);
	ResultSet update(String condition, User user);
	ResultSet delete(String condition, User user);	
	
	void shutdown();
	

}
