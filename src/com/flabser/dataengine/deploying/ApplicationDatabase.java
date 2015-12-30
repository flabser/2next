package com.flabser.dataengine.deploying;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.postgresql.util.PSQLException;

import com.flabser.dataengine.DatabaseUtil;
import com.flabser.dataengine.system.IApplicationDatabase;
import com.flabser.dataengine.system.SystemDatabase;
import com.flabser.env.EnvConst;
import com.flabser.server.Server;

public class ApplicationDatabase implements IApplicationDatabase {
	private Properties props = new Properties();
	private String dbURL;

	public ApplicationDatabase() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		props.setProperty("user", EnvConst.DB_USER);
		props.setProperty("password", EnvConst.DB_PWD);
		dbURL = "jdbc:postgresql://" + EnvConst.DATABASE_HOST + ":" + EnvConst.CONN_PORT + "/" + EnvConst.DATABASE_NAME;
		Class.forName(SystemDatabase.jdbcDriver).newInstance();
	}

	public ApplicationDatabase(String dataBase)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		props.setProperty("user", EnvConst.DB_USER);
		props.setProperty("password", EnvConst.DB_PWD);
		dbURL = "jdbc:postgresql://" + EnvConst.DATABASE_HOST + ":" + EnvConst.CONN_PORT + "/" + dataBase;
		Class.forName(SystemDatabase.jdbcDriver).newInstance();
	}

	@Override
	public int createDatabase(String name, String dbUser) throws SQLException {
		if (!hasDatabase(name)) {
			if (name.equals(EnvConst.DATABASE_NAME)) {
				Server.logger.warningLogEntry("creating system database...");
			}
			Connection conn = DriverManager.getConnection(dbURL, props);
			try {
				Statement st = conn.createStatement();
				String sql = "CREATE DATABASE \"" + name + "\" WITH OWNER = " + dbUser + " ENCODING = 'UTF8'";
				st.executeUpdate(sql);
				st.executeUpdate("GRANT ALL privileges ON DATABASE \"" + name + "\" TO " + dbUser);
				st.close();
				return 0;
			} catch (Throwable e) {
				DatabaseUtil.debugErrorPrint(e);
				return -1;
			}
		} else {
			return 1;
		}
	}

	@Override
	public int registerUser(String dbUser, String dbPwd) throws SQLException {

		Connection conn = DriverManager.getConnection(dbURL, props);
		try {
			Statement st = conn.createStatement();
			try {
				st.executeUpdate("CREATE USER  " + dbUser + " WITH password '" + dbPwd + "'");
				return 0;
			} catch (PSQLException sqle) {
				Server.logger.warningLogEntry("database user \"" + dbUser + "\" already exists");
				return 1;
			} catch (Exception e) {
				Server.logger.errorLogEntry(e.getMessage());
				return 1;
			}
		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
			return -1;
		}

	}

	@Override
	public int removeDatabase(String name) throws SQLException {
		if (hasDatabase(name)) {
			Connection conn = DriverManager.getConnection(dbURL, props);
			try {
				Statement st = conn.createStatement();
				st.executeUpdate("DROP DATABASE " + name);
			} catch (PSQLException e) {
				Server.logger.errorLogEntry("database busy " + e.getErrorCode());
			} catch (Throwable e) {
				DatabaseUtil.debugErrorPrint(e);
				return -1;
			}
		}
		return 0;
	}

	private boolean hasDatabase(String dbName) throws SQLException {
		Connection conn = DriverManager.getConnection(dbURL, props);
		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "SELECT 1 FROM pg_database WHERE datname = '" + dbName + "'";
			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				return true;
			}
			s.close();
			conn.commit();
			return false;
		} catch (Throwable e) {
			return false;
		}
	}

}
