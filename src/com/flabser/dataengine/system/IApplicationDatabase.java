package com.flabser.dataengine.system;

import java.sql.SQLException;

public interface IApplicationDatabase {
	int createDatabase(String host, String name) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException;
	int removeDatabase(String host, String name) throws SQLException;
	
}
