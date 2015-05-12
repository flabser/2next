package com.flabser.dataengine.pool;

import java.sql.Connection;
import com.flabser.dataengine.DatabaseType;


public interface IDBConnectionPool {
	void initConnectionPool(String driver, String dbURL, String userName, String password) throws DatabasePoolException, InstantiationException, IllegalAccessException, ClassNotFoundException; 
	Connection getConnection();	
	void returnConnection(Connection con);
	int getNumActive();
	public String toXML();
	void closeAll();
	void close(Connection conn);
	DatabaseType getDatabaseType();
}
