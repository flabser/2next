package com.flabser.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.restful.Application;
import com.flabser.script._IContent;
import com.flabser.solutions.DatabaseType;

@JsonRootName("applicationProfile")
public class ApplicationProfile implements _IContent {
	public int id;
	public String appType;
	public String appID;
	public String appName;
	public String owner;
	@JsonIgnore
	public DatabaseType dbType;
	@JsonIgnore
	public String dbHost = "localhost";
	@JsonIgnore
	public String dbLogin;
	@JsonIgnore
	public String dbPwd;
	@JsonIgnore
	public String dbName;
	public String defaultURL;
	public ApplicationStatusType status = ApplicationStatusType.UNKNOWN;
	private Date statusDate;
	private VisibiltyType visibilty = VisibiltyType.PUBLIC;

	public ApplicationProfile() {

	}

	public ApplicationProfile(ResultSet rs) throws SQLException {
		fill(rs);
	}

	@Override
	public StringBuffer toXML() {
		StringBuffer output = new StringBuffer(1000);
		return output.append("<entry><appname>" + appName + "</appname><owner>" + owner + "</owner>" + "<dbhost>" + dbHost + "</dbhost><dbname>" + dbName
				+ "</dbname><dblogin>" + dbLogin + "</dblogin></entry>");
	}

	public String getDbName() {
		return dbName;
	}

	@JsonIgnore
	public String getURI() {
		return dbType.getJDBCPrefix(dbType) + dbHost + "/" + dbName;
	}

	@JsonIgnore
	public IDatabase getDatabase() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DatabasePoolException {
		switch (dbType) {
		case POSTGRESQL:
			IDatabase db = new com.flabser.solutions.postgresql.Database();
			db.init(this);
			return db;
		default:
			return null;
		}
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
		return appType.toLowerCase() + ".init.DDEScripts";
		// TODO Need to write a class resolver that is implementation of
		// IAppDatabaseInit
	}

	public void fill(ResultSet rs) throws SQLException {
		id = rs.getInt("ID");
		appType = rs.getString("APPTYPE");
		appID = rs.getString("APPID");
		appName = rs.getString("APPNAME");
		owner = rs.getString("OWNER");
		dbType = DatabaseType.getType(rs.getInt("DBTYPE"));
		dbHost = rs.getString("DBHOST");
		dbName = rs.getString("DBNAME");
		dbLogin = rs.getString("DBLOGIN");
		dbPwd = rs.getString("DBPWD");
	}

	@JsonIgnore
	public void setStatus(ApplicationStatusType onLine) {
		status = onLine;
		statusDate = new Date();

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
		Application app = new Application();
		app.appType = appType;

		return app;

	}

}