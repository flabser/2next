package com.flabser.dataengine;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.PersistenceProvider;

import com.flabser.dataengine.pool.DBConnectionPool;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.pool.IDBConnectionPool;
import com.flabser.dataengine.system.entities.ApplicationProfile;

public abstract class DatabaseCore {
	protected ApplicationProfile appProfile;
	protected IDBConnectionPool pool;
	protected EntityManager entityManager;

	protected void initConnectivity(String driver, ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, DatabasePoolException {
		pool = new DBConnectionPool();
		pool.initConnectionPool(driver, appProfile.getURI(), appProfile.dbLogin, appProfile.dbPwd);

		Map<String, String> properties = new HashMap<String, String>();
		properties.put(PersistenceUnitProperties.JDBC_DRIVER, driver);
		properties.put(PersistenceUnitProperties.JDBC_USER, appProfile.dbLogin);
		properties.put(PersistenceUnitProperties.JDBC_PASSWORD, appProfile.dbPwd);
		properties.put(PersistenceUnitProperties.JDBC_URL, appProfile.getURI());
		PersistenceProvider pp = new PersistenceProvider();
		EntityManagerFactory factory = pp.createEntityManagerFactory("JPA", properties);
		entityManager = factory.createEntityManager();
	}

}
