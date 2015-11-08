package com.flabser.dataengine.system.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flabser.apptemplate.AppTemplate;
import com.flabser.apptemplate.WorkModeType;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.IApplicationDatabase;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
import com.flabser.restful.pojo.Application;
import com.flabser.rule.Role;
import com.flabser.rule.constants.RunMode;
import com.flabser.script._IContent;
import com.flabser.server.Server;
import com.flabser.solutions.DatabaseType;
import com.flabser.users.ApplicationStatusType;
import com.flabser.users.User;
import com.flabser.users.VisibiltyType;
import com.flabser.util.Util;

public class ApplicationProfile implements _IContent {
	public int id;
	public Date regDate;
	public String appType;

	public String appID;
	public String appName;

	public String owner;
	public DatabaseType dbType = DatabaseType.POSTGRESQL;
	public String dbHost = EnvConst.DATABASE_HOST + ":" + EnvConst.CONN_PORT;

	public String dbName;
	public String defaultURL;
	private ApplicationStatusType status = ApplicationStatusType.UNKNOWN;
	private WorkModeType workingMode = WorkModeType.CLOUD;
	private Date statusDate;
	private VisibiltyType visibilty;
	private ArrayList<UserRole> roles = new ArrayList<>();
	private String desciption;
	private String lastError;

	public ApplicationProfile() {
	}

	public ApplicationProfile(AppTemplate template) {
		this.appType = template.templateType;
		this.appID = "";
		this.appName = template.templateType;
		this.dbName = template.templateType.toLowerCase();
		this.status = ApplicationStatusType.READY_TO_DEPLOY;
		workingMode = WorkModeType.COMMON;
	}

	public ApplicationProfile(int id, String appType, String appID, String appName, String owner, int dbType,
			String dbHost, String dbName, int status, Date statusDate, ArrayList<UserRole> roles, Date regDate,
			String descr, String lastError, int v) {
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
		visibilty = VisibiltyType.getType(v);
		this.setRoles(roles);
		this.regDate = regDate;
		desciption = descr;
		this.lastError = lastError;
	}

	public ApplicationProfile(ResultSet rs) throws SQLException {
		fill(rs);
	}

	@Override
	public StringBuffer toXML(String lang) {
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
		default:
			break;
		}
		return db;
	}

	public boolean save() {
		ISystemDatabase sysDatabase = DatabaseFactory.getSysDatabase();
		boolean result = true;

		if (id == 0) {
			regDate = new Date();
			id = sysDatabase.insert(this);
		} else {
			id = sysDatabase.update(this);
		}

		if (id < 0) {
			result = false;
		} else {
			if (getStatus() == ApplicationStatusType.READY_TO_DEPLOY) {
				try {
					User userOwner = sysDatabase.getUser(owner);
					IApplicationDatabase appDb = sysDatabase.getApplicationDatabase();
					appDb.registerUser(userOwner.getDBLogin(), userOwner.getDbPwd());
					int res = appDb.createDatabase(getDbName(), userOwner.getDBLogin());
					if (res == 0 || res == 1) {
						IDatabase db = getDatabase();
						if (db != null) {
							setStatus(ApplicationStatusType.ON_LINE);
							AppTemplate app = Environment.availableTemplates.get(appType).getAppTemlate();
							for (Role role : app.globalSetting.roleCollection.getRolesList()) {
								if (role.isOn == RunMode.ON) {
									addRole(role.name, role.description);
								}
							}
						} else {
							setStatus(ApplicationStatusType.DEPLOING_FAILED);
							result = false;
						}
					} else {
						setStatus(ApplicationStatusType.DATABASE_NOT_CREATED);
						result = false;
					}
				} catch (Exception e) {
					Server.logger.errorLogEntry(e);
					setStatus(ApplicationStatusType.DEPLOING_FAILED);
				}
				sysDatabase.update(this);
				if (id < 0) {
					result = false;
				}
			}
		}
		return result;
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
		status = ApplicationStatusType.getType(rs.getInt("STATUS"));
		// visibilty
	}

	public void setStatus(ApplicationStatusType onLine) {
		status = onLine;
		statusDate = new Date();

	}

	@JsonIgnore
	public String getAppId() {
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

	@JsonIgnore
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

	public WorkModeType getMode() {
		return workingMode;
	}

	public void setMode(WorkModeType mode) {
		this.workingMode = mode;
	}

	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

	public String getLastError() {
		return lastError;
	}

	public void setLastError(String e) {
		this.lastError = e;
	}

	@Override
	public String toString() {
		return appID + "," + appType + "," + owner + "," + status.toString();
	}

}
