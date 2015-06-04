package com.flabser.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import com.flabser.appenv.AppEnv;
import com.flabser.env.Environment;

public class ApplicationProfile{
	public String appName;
	public String owner;
	public String defaultURL;
	public String dbHost = "localhost";
	public String dbLogin;
	public String dbPwd;
	private String dbName;
	private HashSet<User> members = new HashSet<User>();
	
	public ApplicationProfile(){
		
	}

	
	public ApplicationProfile(ResultSet rs) throws SQLException {
		appName = rs.getString("APP");
		dbHost	= rs.getString("DBHOST");
		dbName = rs.getString("DBNAME");
		dbLogin = rs.getString("DBLOGIN");
		dbPwd = rs.getString("DBPWD");
	}
	
	
	public StringBuffer toXML(){
		StringBuffer output = new StringBuffer(1000);
		return output.append("<entry><appname>" + appName + "</appname><loginmode></loginmode>" + output + "</entry>");
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
	
}