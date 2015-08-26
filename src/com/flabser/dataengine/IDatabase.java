package com.flabser.dataengine;

import javax.persistence.EntityManager;

import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.pool.IDBConnectionPool;
import com.flabser.dataengine.system.entities.ApplicationProfile;


public interface IDatabase {

	void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, DatabasePoolException;

	int getVersion();

	IDeployer getDeployer();

	EntityManager getEntityManager();

	IFTIndexEngine getFTSearchEngine() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			DatabasePoolException;

	void shutdown();

	IDBConnectionPool getPool();
}
