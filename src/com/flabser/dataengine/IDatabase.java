package com.flabser.dataengine;

import java.util.ArrayList;

import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.script._IObject;
import com.flabser.users.ApplicationProfile;
import com.flabser.users.User;


public interface IDatabase {

	void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, DatabasePoolException;

	int getVersion();

	IDeployer getDeployer();

	IFTIndexEngine getFTSearchEngine();

	ArrayList <Object[]> select(String condition, User user);

	@SuppressWarnings("rawtypes")
	ArrayList <_IObject> select(String condition, Class objClass, User user);

	int insert(String condition, User user);

	int update(String condition, User user);

	int delete(String condition, User user);

	void shutdown();

}
