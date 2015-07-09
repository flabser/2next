package com.flabser.users;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.env.Environment;

public class ApplicationProfile {
	public int id;
	public String appName;
	public String owner;
	public String dbHost = "localhost";
	public String dbLogin;
	public String dbPwd;
	public String dbName;

	public ApplicationProfile() {

	}

	public ApplicationProfile(ResultSet rs) throws SQLException {
		id = rs.getInt("ID");
		appName = rs.getString("APPNAME");
		owner = rs.getString("OWNER");
		dbHost = rs.getString("DBHOST");
		dbName = rs.getString("DBNAME");
		dbLogin = rs.getString("DBLOGIN");
		dbPwd = rs.getString("DBPWD");
	}

	public StringBuffer toXML() {
		StringBuffer output = new StringBuffer(1000);
		return output.append("<entry><appname>" + appName + "</appname><owner>" + owner + "</owner>"
				+ "<dbhost>" + dbHost + "</dbhost><dbname>" + dbName + "</dbname><dblogin>" + dbLogin + "</dblogin></entry>");
	}

	public String getImpl() {
		AppEnv env = Environment.getApplication(appName);
		return env.globalSetting.implementation;
	}

	public String getDbName() {
		return dbName;
	}

	public String getURI() {
		return "jdbc:postgresql://" + dbHost + "/" + dbName;
	}

	public boolean save() {
		ISystemDatabase sysDatabase = DatabaseFactory.getSysDatabase();

		if (id == 0) {
			id = sysDatabase.insert(this);
		} else {
			id = sysDatabase.update(this);
		}

		if (id < 0) {
			return false;
		} else {
			return true;
		}
	}

	public void fill(ResultSet rs) {
		// TODO Auto-generated method stub

	}

}