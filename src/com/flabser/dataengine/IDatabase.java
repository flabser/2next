package com.flabser.dataengine;

import java.util.ArrayList;

import javax.persistence.EntityManager;

import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.pool.IDBConnectionPool;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.restful.data.IAppEntity;
import com.flabser.users.User;


public interface IDatabase {

	void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException,
	ClassNotFoundException, DatabasePoolException;

	int getVersion();

	IDeployer getDeployer();

	EntityManager getEntityManager();


	@Deprecated
	ArrayList<IAppEntity> select(String condition, Class<IAppEntity> objClass, User user);

	@Deprecated
	int insert(String condition, User user);

	@Deprecated
	int update(String condition, User user);

	@Deprecated
	int delete(String condition, User user);

	void shutdown();


	IDBConnectionPool getPool();

	IFTIndexEngine getFTSearchEngine() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
	DatabasePoolException;
}
