package com.flabser.dataengine.system;

public class DDEScripts {

	public static String getUsersDDE() {
		String createTable = "CREATE TABLE users(ID serial NOT NULL, "
				+ "username character varying(128),  "
				+ "login character varying(32),  "
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
				+ "preferredlang varchar(10),"
				+ "CONSTRAINT users_pkey PRIMARY KEY (ID), CONSTRAINT users_id_unique UNIQUE (ID))";

		return createTable;
	}

	public static String getAppsDDE() {
		String dde = "create table APPS(ID serial NOT NULL, "
				+ "APPTYPE varchar(32), "
				+ "APPID varchar(16), "
				+ "APPNAME varchar(64), "
				+ "owner varchar(32), "
				+ "DBTYPE integer, "
				+ "dbhost varchar(64), "
				+ "dbname varchar(64), "
				+ "dblogin varchar(64), "
				+ "dbpwd varchar(32), "
				+ "status integer, "
				+ "statusdate timestamp without time zone, "
				+ "CONSTRAINT apps_pkey PRIMARY KEY (ID), CONSTRAINT apps_id_unique UNIQUE (ID))";
		return dde;
	}

	public static String getUserAppsDDE() {
		String dde = "CREATE TABLE USERAPPS (" + " ID serial NOT NULL,"
				+ " USERID int NOT NULL," + " APPID int,"
				+ " UNIQUE (USERID, APPID))";
		return dde;
	}

	public static String getUserRolesDDE() {
		String dde = "CREATE TABLE USERROLES (" + " ID serial NOT NULL,"
				+ " USERID int NOT NULL," + " APPID int,"
				+ " NAME varchar(32)," + " UNIQUE (USERID, APPID, NAME))";
		return dde;
	}

	public static String getUsersActivityDDE() {
		String dde = "create table USERSACTIVITY(ID serial NOT NULL, "
				+ " TYPE int NOT NULL," + " DBURI varchar(128), "
				+ " USERID varchar(32), " + " CLIENTIP char(15), "
				+ " EVENTTIME timestamp," + " VIEWTEXT varchar(1024))";
		return dde;
	}

	public static String getHolidaysDDE() {
		String dde = "CREATE TABLE HOLIDAYS (ID serial NOT NULL, "
				+ " COUNTRY VARCHAR(10) , " + " TITLE VARCHAR(64), "
				+ " REPEAT INT, " + " STARTDATE TIMESTAMP, "
				+ " CONTINUING INT, " + " ENDDATE TIMESTAMP, "
				+ " IFFALLSON INT, " + " COMMENT VARCHAR(64))";
		return dde;
	}

}
