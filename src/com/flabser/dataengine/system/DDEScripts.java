package com.flabser.dataengine.system;

public class DDEScripts {
	
	public static String getUsersDDE(){
		String createTable="CREATE TABLE users( docid serial NOT NULL, "
				+ "userid character varying(32),  "
				+ "email character varying(32),  "
				+ "pwd character varying(32),  "
				+ "regdate timestamp without time zone DEFAULT now(), "
				+ "isappadmin integer, "
				+ "isadmin integer, "
				+ "isobserver integer, "
				+ "loginhash integer, "
				+ "publickey character varying(6144), "
				+ "pwdhash character varying(1024), "
				+ "lastDefaultURL varchar(128), " 
				+ "CONSTRAINT users_pkey PRIMARY KEY (docid), CONSTRAINT users_userid_unique UNIQUE (userid))";

		createTable += ";CREATE TABLE temp_users(id serial NOT NULL, "
				+ "userid character varying(10), "
				+ "pwd character varying(32), "
				+ "regdate timestamp without time zone DEFAULT now(), "
				+ "starttime timestamp without time zone, "
				+ "lifetime integer DEFAULT 0,"
				+ "CONSTRAINT temp_users_primary_key PRIMARY KEY (id), CONSTRAINT temp_users_userid_key UNIQUE (userid))";

		return createTable;
	}

	public static String getEnabledAppDDE(){
		String dde = "create table ENABLEDAPPS(ID serial NOT NULL, " +
				"DOCID int, " +
				"APP varchar(32), " +
				"defaultURL varchar(128), " +
				"dburl varchar(128), " +
				"dbpwd varchar(32), " +				
				"LOGINMODE int, " +
				"FOREIGN KEY (DOCID) REFERENCES USERS(DOCID))";
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
