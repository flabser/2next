package com.flabser.dataengine;

import java.util.ArrayList;

import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.restful.data.IAppEntity;
import com.flabser.users.User;


public interface IDatabase {

	void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, DatabasePoolException;

	int getVersion();

	IDeployer getDeployer();

	IFTIndexEngine getFTSearchEngine() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			DatabasePoolException;

	@Deprecated
	ArrayList <IAppEntity> select(String condition, Class <IAppEntity> objClass, User user);

	int insert(String condition, User user);

	int update(String condition, User user);

	int delete(String condition, User user);

	void shutdown();

	IAppEntity insert(IAppEntity a, User user);

	IAppEntity update(IAppEntity a, User user);

	java.util.List <IAppEntity> select(String condition, User user);

	void delete(IAppEntity a, User user);
}
