package com.flabser.users;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.flabser.appenv.AppEnv;
import com.flabser.env.Environment;
import com.flabser.script._IXMLContent;
import com.flabser.solutions.SolutionsType;

public class ApplicationProfile implements _IXMLContent{
	public SolutionsType solution;
	public String appName;
	public String owner;
	public String defaultURL;
	public String dbLogin;
	public String dbURL;
	public String dbPwd;

	public ApplicationProfile(){
		
	}

	
	public ApplicationProfile(ResultSet rs) throws SQLException {
		appName = rs.getString("APP");
	//	defaultURL = rs.getString("DEFAULTURL");
		dbURL = rs.getString("DBURL");
		dbPwd = rs.getString("DBPWD");
	}
	
	
	public StringBuffer toXML(){
		StringBuffer output = new StringBuffer(1000);
		return output.append("<entry><appname>" + solution + "</appname><loginmode></loginmode>" + output + "</entry>");
	}


	public String getImpl() {
		AppEnv env = Environment.getApplication(appName);
		return env.globalSetting.implementation;
	}
	
}