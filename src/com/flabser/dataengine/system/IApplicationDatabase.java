package com.flabser.dataengine.system;

import java.sql.SQLException;

public interface IApplicationDatabase {

	int registerUser(String dbUser, String dbPwd) throws SQLException;

	int createDatabase(String name, String owner) throws SQLException;

	int removeDatabase(String name) throws SQLException;
}
