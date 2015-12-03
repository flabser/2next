package com.flabser.dataengine;

import java.util.HashMap;
import java.util.Map;

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
import com.flabser.server.Server;
import com.flabser.users.User;

public abstract class DatabaseCore {

	protected ApplicationProfile appProfile;
	protected IDBConnectionPool pool;
	protected EntityManagerFactory factory;

	protected void initConnectivity(String driver, ApplicationProfile appProfile)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, DatabasePoolException {
		pool = new DBConnectionPool();
		String dbLogin = null;
		String dbPwd = null;
		if (appProfile.getMode() == WorkModeType.CLOUD) {
			ISystemDatabase sysDb = DatabaseFactory.getSysDatabase();
			User user = sysDb.getUser(appProfile.owner);
			if (user != null) {
				dbLogin = user.getDBLogin();
				dbPwd = user.getDefaultDbPwd();
			} else {
				throw new ApplicationException(appProfile.appType,
						"Owner of the application cannot get access to database \"" + appProfile.getURI() + "\"");
			}
		} else if (appProfile.getMode() == WorkModeType.COMMON) {
			dbLogin = EnvConst.DB_USER;
			dbPwd = EnvConst.DB_PWD;
		} else {
			throw new ApplicationException(appProfile.appType,
					"The mode \"" + appProfile.getMode() + "\" of the application does not support");
		}

		pool.initConnectionPool(driver, appProfile.getURI(), dbLogin, dbPwd);
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(PersistenceUnitProperties.JDBC_DRIVER, driver);
		properties.put(PersistenceUnitProperties.JDBC_USER, dbLogin);
		properties.put(PersistenceUnitProperties.JDBC_PASSWORD, dbPwd);
		// properties.put(PersistenceUnitProperties.JDBC_PROPERTY, dbPwd);

		properties.put(PersistenceUnitProperties.JDBC_URL, appProfile.getURI());

		// INFO,
		// OFF,
		// ALL,
		// CONFIG (developing)
		properties.put(PersistenceUnitProperties.LOGGING_LEVEL, "ALL");
		// properties.put(PersistenceUnitProperties.LOGGING_LOGGER, "Server");
		properties.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.CREATE_ONLY);
		// for CREATE_OR_EXTEND need DDL_GENERATION_MODE = (DDL_BOTH_GENERATION
		// | DATABASE)
		// properties.put(PersistenceUnitProperties.DDL_GENERATION_MODE,
		// PersistenceUnitProperties.DDL_BOTH_GENERATION);
		// properties.put(PersistenceUnitProperties.CREATE_JDBC_DDL_FILE,
		// "createDDL.jdbc");
		// properties.put(PersistenceUnitProperties.DROP_JDBC_DDL_FILE,
		// "dropDDL.jdbc");
		// properties.put(PersistenceUnitProperties.SESSION_CUSTOMIZER,
		// "com.flabser.dataengine.Event");
		// properties.put(PersistenceUnitProperties.SCHEMA_GENERATION_SQL_LOAD_SCRIPT_SOURCE,
		// "META-INF/load_script.sql");

		PersistenceProvider pp = new PersistenceProvider();
		factory = pp.createEntityManagerFactory(appProfile.appType, properties);
		if (factory == null) {
			Server.logger
					.warningLogEntry("the entity manager of \"" + appProfile.appType + "\" has not been initialized");
			/*
			 * Server.logger .errorLogEntry(dbPwd + " " + dbLogin + " " +
			 * properties.get(PersistenceUnitProperties.JDBC_URL));
			 */
		}

	}

	public EntityManagerFactory getEntityManagerFactory() {
		return factory;
	}

}
