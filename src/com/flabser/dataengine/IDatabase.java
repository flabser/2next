package com.flabser.dataengine;

import javax.persistence.EntityManagerFactory;

import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.pool.IDBConnectionPool;
import com.flabser.dataengine.system.entities.ApplicationProfile;


public interface IDatabase {

	void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException, ClassNotFoundException, DatabasePoolException;

	int getVersion();


	void shutdown();


	IDBConnectionPool getPool();

	IFTIndexEngine getFTSearchEngine();

	EntityManagerFactory getEntityManagerFactory();
}
