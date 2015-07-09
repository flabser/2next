package com.flabser.dataengine.system;

import java.sql.SQLException;

public interface IApplicationDatabase {
	int removeDatabase(String host, String name) throws SQLException;
	int createDatabase(String host, String name, String owner,  String dbPwd) throws SQLException;



}
