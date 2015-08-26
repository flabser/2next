package com.flabser.dataengine;

import javax.persistence.EntityManager;

import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.pool.IDBConnectionPool;
import com.flabser.dataengine.system.entities.ApplicationProfile;


public interface IDatabase {

	void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException, ClassNotFoundException, DatabasePoolException;

	int getVersion();

	IDeployer getDeployer();

	EntityManager getEntityManager();


	void shutdown();


	IDBConnectionPool getPool();

	IFTIndexEngine getFTSearchEngine();
}
