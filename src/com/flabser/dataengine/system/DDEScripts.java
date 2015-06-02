package com.flabser.dataengine.system;

public class DDEScripts {
	
	public static String getUsersDDE(){
		String createTable="CREATE TABLE users( docid serial NOT NULL, "
				+ "username character varying(128),  "
				+ "userid character varying(32),  "
				+ "email character varying(32),  "
				+ "pwd character varying(32),  "
				+ "primaryregdate timestamp without time zone DEFAULT now(), "
				+ "regdate timestamp without time zone, "
				+ "ISSUPERVISOR integer, "
				+ "loginhash integer, "
				+ "pwdhash character varying(1024), "
				+ "lastDefaultURL varchar(128), "
				+ "status integer,"
				+ "verifycode character varying(64),"
				+ "CONSTRAINT users_pkey PRIMARY KEY (docid), CONSTRAINT users_userid_unique UNIQUE (userid))";

		return createTable;
	}

	public static String getEnabledAppDDE(){
		String dde = "create table APPS(ID serial NOT NULL, " +
				"DOCID int, " +
				"APP varchar(32), " +
				"owner varchar(64), " +
				"dbhost varchar(64), " +
				"dbname varchar(64), " +
				"dblogin varchar(64), " +
				"dbpwd varchar(32), " +				
				"FOREIGN KEY (DOCID) REFERENCES USERS(DOCID))";
		return dde;
	} 

	 public static String getUserRolesDDE() {
	        String dde = "CREATE TABLE USER_ROLES (" +
	                " UID serial NOT NULL," +
	                " EMPID int NOT NULL," +
	                " NAME varchar(64)," +
	                " TYPE int NOT NULL," +
	                " APPID varchar(256)," +
	                " UNIQUE (EMPID, NAME, TYPE, APPID))";
	        return dde;
	    }
	 
	 public static String getGroupsDDE() {
	        String dde = "create table GROUPS(GROUPID serial NOT NULL, " +
	                " GROUPNAME varchar(32) UNIQUE NOT NULL, " +
	                " FORM varchar(32), " +
	                " DESCRIPTION varchar(256), " +
	                " OWNER varchar(32), " +
	                " TYPE int," +
	                " PARENTDOCID int," +
	                " PARENTDOCTYPE int," +
	                " VIEWTEXT varchar(2048), " +	              
	                " DEFAULTRULEID varchar(32))";
	        return dde;
	    }

	    public static String getUserGroupsDDE() {
	        String dde = "create table USER_GROUPS(UID serial NOT NULL, " +
	                " EMPID int NOT NULL," +
	                " GROUPID int NOT NULL," +
	                " TYPE int," +
	                " UNIQUE (EMPID, GROUPID))";
	        return dde;
	    }

	    public static String getUsersActivityDDE() {
	        String dde = "create table USERS_ACTIVITY(ID serial NOT NULL, " +
	                " TYPE int NOT NULL," +
	                " DBID varchar(128), " +
	                " USERID varchar(32), " +
	                " CLIENTIP char(15), " +
	                " EVENTTIME timestamp," +
	                " VIEWTEXT varchar(2048))";
	        return dde;
	    }
	    
	public static String getHolidaysDDE() {
		String dde = "CREATE TABLE HOLIDAYS (ID serial NOT NULL, " +
				" COUNTRY VARCHAR(10) , " +
				" TITLE VARCHAR(64), " +
				" REPEAT INT, " +
				" STARTDATE TIMESTAMP, " +
				" CONTINUING INT, " +
				" ENDDATE TIMESTAMP, " +
				" IFFALLSON INT, " +
				" COMMENT VARCHAR(64))";
		return dde;
	}

}
