package com.flabser.dataengine;

import java.util.ArrayList;

import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.restful.data.IEntity;
import com.flabser.users.User;

public interface IDatabase {

	void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			DatabasePoolException;

	int getVersion();

	IDeployer getDeployer();

	IFTIndexEngine getFTSearchEngine() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DatabasePoolException;

	ArrayList<IEntity> select(String condition, Class<IEntity> objClass, User user);

	int insert(String condition, User user);

	int update(String condition, User user);

	int delete(String condition, User user);

	void shutdown();

}
