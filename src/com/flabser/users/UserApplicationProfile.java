package com.flabser.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.flabser.script._IXMLContent;

public class UserApplicationProfile implements _IXMLContent{
	public String appName;
	public String defaultURL;
	public String dbURL;
	public String dbPwd;


	
	public UserApplicationProfile(ResultSet rs) throws SQLException {
		appName = rs.getString("APP");
		defaultURL = rs.getString("DEFAULTURL");
		dbURL = rs.getString("DBURL");
		dbPwd = rs.getString("DBPWD");
	}
	
	
	public String toXML(){
		String qaText = "";		
		return "<entry><appname>" + appName + "</appname><loginmode></loginmode>" + qaText + "</entry>";
	}
	
}