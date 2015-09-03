package com.flabser.dataengine;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.PersistenceProvider;

import com.flabser.apptemplate.WorkModeType;
import com.flabser.dataengine.pool.DBConnectionPool;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.pool.IDBConnectionPool;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.env.EnvConst;
import com.flabser.exception.ApplicationException;
import com.flabser.users.User;

public abstract class DatabaseCore {
	protected ApplicationProfile appProfile;
	protected IDBConnectionPool pool;
	protected EntityManager entityManager;

	protected void initConnectivity(String driver, ApplicationProfile appProfile) throws InstantiationException,
	IllegalAccessException, ClassNotFoundException, DatabasePoolException {
		pool = new DBConnectionPool();
		String dbLogin = null;
		String dbPwd = null;
		if (appProfile.getMode() == WorkModeType.CLOUD) {
			ISystemDatabase sysDb = DatabaseFactory.getSysDatabase();
			User user = sysDb.getUser(appProfile.owner);
			if (user != null) {
				dbLogin = user.getDBLogin();
				dbPwd = user.getDbPwd();
			} else {
				throw new ApplicationException(appProfile.appType,
						"Owner of the application cannot get access to database \"" + appProfile.getURI() + "\"");
			}
		}else if (appProfile.getMode() == WorkModeType.COMMON) {
			dbLogin = EnvConst.DB_USER;
			dbPwd = EnvConst.DB_PWD;
		} else {
			throw new ApplicationException(appProfile.appType, "The mode \"" + appProfile.getMode()
					+ "\" of the pplication does not support");
		}


		pool.initConnectionPool(driver, appProfile.getURI(), dbLogin, dbPwd);
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(PersistenceUnitProperties.JDBC_DRIVER, driver);
		properties.put(PersistenceUnitProperties.JDBC_USER, dbLogin);
		properties.put(PersistenceUnitProperties.JDBC_PASSWORD, dbPwd);
		properties.put(PersistenceUnitProperties.JDBC_URL, appProfile.getURI());

		//properties.put(PersistenceUnitProperties.LOGGING_LEVEL, "");
		properties.put(PersistenceUnitProperties.DDL_GENERATION,
				"drop-and-create-tables");
		properties.put(PersistenceUnitProperties.CREATE_JDBC_DDL_FILE,
				"createDDL.jdbc");
		properties.put(PersistenceUnitProperties.DROP_JDBC_DDL_FILE,
				"dropDDL.jdbc");
		properties.put(PersistenceUnitProperties.DDL_GENERATION_MODE,
				"create");
		//	properties.put(PersistenceUnitProperties.APP_LOCATION,Environment.tmpDir);
		System.out.println(appProfile.appType);
		//properties.put(PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_UNITS,appProfile.appType);
		PersistenceProvider pp = new PersistenceProvider();
		EntityManagerFactory factory = pp.createEntityManagerFactory(appProfile.appType, properties);
		entityManager = factory.createEntityManager();



	}

	public void generatePersistStorage(){
		// properties.put(PersistenceUnitProperties.LOGGING_LEVEL, "");
		// properties.put(PersistenceUnitProperties.DDL_GENERATION,
		// "drop-and-create-tables");
		// properties.put(PersistenceUnitProperties.CREATE_JDBC_DDL_FILE,
		// "createDDL.jdbc");
		// properties.put(PersistenceUnitProperties.DROP_JDBC_DDL_FILE,
		// "dropDDL.jdbc");
		// properties.put(PersistenceUnitProperties.DDL_GENERATION_MODE,
		// "both");
	}
}
