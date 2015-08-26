package com.flabser.dataengine;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.PersistenceProvider;

import com.flabser.apptemplate.ModeType;
import com.flabser.dataengine.pool.DBConnectionPool;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.pool.IDBConnectionPool;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.exception.ApplicationException;
import com.flabser.users.User;

public abstract class DatabaseCore {
	protected ApplicationProfile appProfile;
	protected IDBConnectionPool pool;
	protected EntityManager entityManager;

	protected void initConnectivity(String driver, ApplicationProfile appProfile) throws InstantiationException,
	IllegalAccessException, ClassNotFoundException, DatabasePoolException {
		pool = new DBConnectionPool();
		if (appProfile.getMode() == ModeType.CLOUD) {
			ISystemDatabase sysDb = DatabaseFactory.getSysDatabase();
			User user = sysDb.getUser(appProfile.owner);
			if (user != null) {
				pool.initConnectionPool(driver, appProfile.getURI(), user.getDBLogin(), user.getDbPwd());
				Map<String, String> properties = new HashMap<String, String>();
				properties.put(PersistenceUnitProperties.JDBC_DRIVER, driver);
				properties.put(PersistenceUnitProperties.JDBC_USER, user.getDBLogin());
				properties.put(PersistenceUnitProperties.JDBC_PASSWORD, user.getDbPwd());
				properties.put(PersistenceUnitProperties.JDBC_URL, appProfile.getURI());
				PersistenceProvider pp = new PersistenceProvider();
				EntityManagerFactory factory = pp.createEntityManagerFactory("JPA", properties);
				entityManager = factory.createEntityManager();
			} else {
				throw new ApplicationException(appProfile.appType,
						"Owner of the application cannot get access to database \"" + appProfile.getURI() + "\"");
			}
		} else {
			throw new ApplicationException(appProfile.appType, "The mode \"" + appProfile.getMode()
					+ "\" of the pplication does not support");
		}

	}
}
