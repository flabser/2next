package com.flabser.dataengine.system.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.apptemplate.AppTemplate;
import com.flabser.apptemplate.ModeType;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.env.EnvConst;
import com.flabser.restful.Application;
import com.flabser.rule.constants.RunMode;
import com.flabser.script._IContent;
import com.flabser.server.Server;
import com.flabser.solutions.DatabaseType;
import com.flabser.users.ApplicationStatusType;
import com.flabser.users.VisibiltyType;
import com.flabser.util.Util;

@JsonRootName("applicationProfile")
public class ApplicationProfile implements _IContent {
	public int id;
	public String appType;

	public String appID;
	public String appName;

	public String owner;
	@JsonIgnore
	public DatabaseType dbType = DatabaseType.POSTGRESQL;
	@JsonIgnore
	public String dbHost = EnvConst.DATABASE_HOST + ":" + EnvConst.CONN_PORT;

	@JsonIgnore
	public String dbName;
	public String defaultURL;
	public ApplicationStatusType status = ApplicationStatusType.UNKNOWN;
	private ModeType mode = ModeType.CLOUD;
	private Date statusDate;
	private VisibiltyType visibilty;
	private ArrayList<UserRole> roles = new ArrayList<>();
	private String desciption;

	public ApplicationProfile() {
	}

	public ApplicationProfile(AppTemplate template) {
		this.appType = template.appType;
		this.appID = "";
		this.appName = template.appType;
		dbType = DatabaseType.SYSTEM;
		this.dbName = EnvConst.DATABASE_NAME;
		this.status = ApplicationStatusType.READY_TO_DEPLOY;
		mode = ModeType.COMMON;
	}

	public ApplicationProfile(int id, String appType, String appID, String appName, String owner, int dbType,
			String dbHost, String dbName, int status, Date statusDate, ArrayList<UserRole> roles) {
		this.id = id;
		this.appType = appType;
		this.appID = appID;
		this.appName = appName;
		this.owner = owner;
		this.dbType = DatabaseType.getType(dbType);
		this.dbHost = dbHost;
		this.dbName = dbName;
		this.status = ApplicationStatusType.getType(status);
		this.statusDate = statusDate;
		// visibilty
		this.setRoles(roles);
	}

	public ApplicationProfile(ResultSet rs) throws SQLException {
		fill(rs);
	}

	@Override
	public StringBuffer toXML() {
		StringBuffer output = new StringBuffer(1000);
		return output.append("<entry><appname>" + appName + "</appname><owner>" + owner + "</owner><dbhost>" + dbHost
				+ "</dbhost><dbname>" + dbName + "</dbname></entry>");
	}

	public String getDbName() {
		return dbName;
	}

	@JsonIgnore
	public String getURI() {
		return dbType.getJDBCPrefix(dbType) + dbHost + "/" + dbName;
	}

	@JsonIgnore
	public IDatabase getDatabase() {
		IDatabase db = null;
		switch (dbType) {
		case POSTGRESQL:
			db = new com.flabser.solutions.postgresql.Database();
			try {
				db.init(this);
				return db;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (DatabasePoolException e) {
				Server.logger.errorLogEntry(e.getMessage());
			}
			break;
		case SYSTEM:
			db = (IDatabase) DatabaseFactory.getSysDatabase();
			try {
				db.init(this);
				return db;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (DatabasePoolException e) {
				Server.logger.errorLogEntry(e.getMessage());
			}
		}
		return db;
	}

	public boolean save() {
		ISystemDatabase sysDatabase = DatabaseFactory.getSysDatabase();

		if (id == 0) {
			id = sysDatabase.insert(this);
		} else {
			id = sysDatabase.update(this);
		}

		if (id < 0) {
			return false;
		} else {
			return true;
		}
	}

	@JsonIgnore
	public String getDbInitializerClass() {
		return appType.toLowerCase() + ".init.FirstAction";
		// TODO Need to write a class resolver that is implementation of
		// IAppDatabaseInit
	}

	@Deprecated
	public void fill(ResultSet rs) throws SQLException {
		id = rs.getInt("ID");
		appType = rs.getString("APPTYPE");
		appID = rs.getString("APPID");
		appName = rs.getString("APPNAME");
		owner = rs.getString("OWNER");
		dbType = DatabaseType.getType(rs.getInt("DBTYPE"));
		dbHost = rs.getString("DBHOST");
		dbName = rs.getString("DBNAME");
		// visibilty
	}

	@JsonIgnore
	public void setStatus(ApplicationStatusType onLine) {
		status = onLine;
		statusDate = new Date();

	}

	@JsonIgnore
	public String appId() {
		if (appID == null || appID.isEmpty()) {
			appID = Util.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890");
			return appID;
		} else {
			return appID;
		}
	}

	public ApplicationStatusType getStatus() {
		return status;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public String getAppID() {
		return appID;
	}

	public VisibiltyType getVisibilty() {
		return visibilty;
	}

	public void setVisibilty(VisibiltyType visibilty) {
		this.visibilty = visibilty;
	}

	public String getDefaultURL() {
		return defaultURL;
	}

	public void setDefaultURL(String defaultURL) {
		this.defaultURL = defaultURL;
	}

	public Application getPOJO() {
		Application app = new Application(this);
		app.setAppType(appType);

		return app;
	}

	public void addRole(String name, String descr) {
		roles.add(new UserRole(0, name, descr, this.id, RunMode.ON));
	}

	public void setRoles(ArrayList<UserRole> roles) {
		this.roles = roles;
	}

	public ArrayList<UserRole> getRoles() {
		return roles;
	}

	public ModeType getMode() {
		return mode;
	}

	public void setMode(ModeType mode) {
		this.mode = mode;
	}

	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

}
