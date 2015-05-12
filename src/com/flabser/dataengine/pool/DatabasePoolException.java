package com.flabser.dataengine.pool;

public class DatabasePoolException  extends Exception {
	public DatabasePoolExceptionType id;
	public String user;

	private static final long serialVersionUID = 6120594147766886379L;
	private String errorText;
	
	public DatabasePoolException(DatabasePoolExceptionType error) {
		super();
		id = error;
		switch(id){ 
		case DATABASE_CONNECTION_REFUSED:
			errorText = "Database connection refused " ;
		case DATABASE_AUTHETICATION_FAILED:
			errorText = "password authentication failed ";
		case DATABASE_SQL_ERROR:
			errorText = "Database SQL error ";
		
		}		
	}
	
	public DatabasePoolException(DatabasePoolExceptionType error, String dbID) {
		super();
		id = error;
		switch(id){ 
		case DATABASE_SQL_ERROR:
			errorText = "Database SQL error (" + dbID + ")";
		
		}		
	}
	
	public String getMessage(){
		return errorText;
	}
	
	public String toString(){
		return errorText;
	}
}
