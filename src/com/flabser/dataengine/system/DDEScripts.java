package com.flabser.dataengine.system;

public class DDEScripts {

	public static final String USERS_DDE = "CREATE TABLE users(ID serial NOT NULL, "
			+ "username  varchar(128),  "
			+ "login  varchar(32),  "
			+ "email  varchar(32),  "
			+ "pwd  varchar(32),  "
			+ "primaryregdate timestamp without time zone DEFAULT now(), "
			+ "regdate timestamp without time zone, "
			+ "ISSUPERVISOR boolean, "
			+ "loginhash integer, "
			+ "pwdhash  varchar(64), "
			+ "dbPwd  varchar(32), "
			+ "lastDefaultURL varchar(128), "
			+ "status integer,"
			+ "verifycode  varchar(64),"
			+ "preferredlang varchar(10),"
			+ "avatar bytea,"
			+ "avatarname  varchar(64),"
			+ "apps integer[], "
			+ "roles integer[], "
			+ "groups integer[], "
			+ "CONSTRAINT users_pkey PRIMARY KEY (ID));" +
			"" +
			"create index users_login_hash on users using hash(login text_ops); ";

	public static final String APPS_DDE = "create table APPS("
			+ "ID serial NOT NULL, "
			+ "regdate timestamp without time zone, "
			+ "APPTYPE varchar(32), "
			+ "APPID varchar(16), "
			+ "APPNAME varchar(64), "
			+ "owner varchar(32), "
			+ "DBTYPE integer, "
			+ "dbhost varchar(64), "
			+ "dbname varchar(64), "
			+ "status integer, "
			+ "statusdate timestamp without time zone, "
			+ "visibility integer, "
			+ "description varchar(256), "
			+ "lasterror varchar(256), "
			+ "CONSTRAINT apps_pkey PRIMARY KEY (ID))";

	public static final String INVITATIONS_DDE = "create table INVITATIONS("
			+ "ID serial NOT NULL, "
			+ "regdate timestamp without time zone, "
			+ "EMAIL varchar(32), "
			+ "APPTYPE varchar(16), "
			+ "APPID varchar(16), "
			+ "MESSAGE varchar(64), "
			+ "AUTHOR integer, "
			+ "TEMPLOGIN int, "
			+ "CONSTRAINT invs_pkey PRIMARY KEY (ID))";

	public static final String USERS_ACTIVITY_DDE = "create table USERSACTIVITY(ID serial NOT NULL, "
			+ " TYPE int NOT NULL," + " DBURI varchar(128), "
			+ " USERID varchar(32), " + " CLIENTIP char(15), "
			+ " EVENTTIME timestamp," + " VIEWTEXT varchar(1024))";

	public static final String HOLIDAYS_DDE = "CREATE TABLE HOLIDAYS (ID serial NOT NULL, "
			+ " COUNTRY VARCHAR(10) , " + " TITLE VARCHAR(64), "
			+ " REPEAT INT, " + " STARTDATE TIMESTAMP, "
			+ " CONTINUING INT, " + " ENDDATE TIMESTAMP, "
			+ " IFFALLSON INT, " + " COMMENT VARCHAR(64))";

	public static final String ROLES_DDE =
			"create table roles (" +
					"id serial not null, " +
					"name character varying, " +
					"description character varying, " +
					"app_id integer, " +
					"is_on integer, " +
					"CONSTRAINT roles_pkey PRIMARY KEY (id), " +
					"CONSTRAINT unique_roles_name_app_id UNIQUE (name, app_id)" +
					");";

	public static final String GROUPS_DDE =
			"create table groups (" +
					"id serial not null, " +
					"name varchar, " +
					"description varchar, " +
					"roles_id integer[], " +
					"CONSTRAINT groups_pkey PRIMARY KEY (id)" +
					");";
}






















