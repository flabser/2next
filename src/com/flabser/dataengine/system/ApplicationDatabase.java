package com.flabser.dataengine.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import com.flabser.dataengine.DatabaseUtil;

public class ApplicationDatabase implements IApplicationDatabase {
	private Properties props = new Properties(); 
	private String dbURL;

	ApplicationDatabase() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		props.setProperty("user", SystemDatabase.dbUser);
		props.setProperty("password", SystemDatabase.dbUserPwd);
		dbURL = SystemDatabase.connectionURL;
		Class.forName(SystemDatabase.jdbcDriver).newInstance();
	}

	@Override
	public int createDatabase(String host, String name, String owner, String dbPwd) throws SQLException {
		if (!hasDatabase(name)) {
			Connection conn = DriverManager.getConnection(dbURL, props);
			try {				
				Statement st  = conn.createStatement();
				st.executeUpdate("CREATE USER  " + owner + " WITH password '" + dbPwd + "'");
				st.executeUpdate("CREATE DATABASE " + name + " WITH OWNER = " + owner + " ENCODING = 'UTF8'" );
				st.executeUpdate("GRANT ALL privileges ON DATABASE " + name + " TO " + owner);
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
	public int removeDatabase(String host, String name) throws SQLException {
		if (hasDatabase(name)) {
			Connection conn = DriverManager.getConnection(dbURL, props);
			try {
				Statement st = conn.createStatement();
				st.executeUpdate("DROP DATABASE " + name);
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
