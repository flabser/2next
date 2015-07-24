package com.flabser.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.catalina.realm.RealmBase;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.DatabaseUtil;
import com.flabser.dataengine.IAppDatabaseInit;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.IDeployer;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.IApplicationDatabase;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.exception.WebFormValueException;
import com.flabser.exception.WebFormValueExceptionType;
import com.flabser.localization.LanguageType;
import com.flabser.restful.AuthUser;
import com.flabser.server.Server;
import com.flabser.util.Util;

@JsonRootName("user")
public class User {
	public int id;
	public boolean isValid = false;
	public boolean isAuthorized;
	public String lastURL;
	public LanguageType preferredLang = LanguageType.ENG;

	private HashMap<String, HashMap<String, ApplicationProfile>> enabledApps = new HashMap<String, HashMap<String, ApplicationProfile>>();
	private HashMap<String, ApplicationProfile> applications = new HashMap<String, ApplicationProfile>();
	private HashSet<UserRole> roles = new HashSet<UserRole>();
	private transient ISystemDatabase sysDatabase;
	private String login;
	private String userName;
	private HashMap<String, PersistentValue> persistentValues = new HashMap<String, PersistentValue>();

	private Date primaryRegDate;
	private Date regDate;
	private String password;
	private String passwordHash = "";
	private String email = "";

	private int isSupervisor;
	private int hash;
	private String verifyCode;
	private UserStatusType status = UserStatusType.UNKNOWN;
	public final static String ANONYMOUS_USER = "anonymous";

	public User() {
		this.sysDatabase = DatabaseFactory.getSysDatabase();
		login = ANONYMOUS_USER;
	}

	public void fill(ResultSet rs) throws SQLException {
		try {
			id = rs.getInt("ID");
			userName = rs.getString("USERNAME");
			primaryRegDate = rs.getTimestamp("PRIMARYREGDATE");
			regDate = rs.getTimestamp("REGDATE");
			login = rs.getString("LOGIN");
			setEmail(rs.getString("EMAIL"));
			isSupervisor = rs.getInt("ISSUPERVISOR");
			password = rs.getString("PWD");
			passwordHash = rs.getString("PWDHASH");
			setHash(rs.getInt("LOGINHASH"));
			verifyCode = rs.getString("VERIFYCODE");
			status = UserStatusType.getType(rs.getInt("STATUS"));
			isValid = true;
		} catch (Exception e) {
			isValid = false;
		}
	}

	@JsonIgnore
	public String getPasswordHash() {
		return passwordHash;
	}

	@JsonIgnore
	public void setPasswordHash(String password) throws WebFormValueException {
		if (!("".equalsIgnoreCase(password))) {
			if (Util.pwdIsCorrect(password)) {
				this.passwordHash = password.hashCode() + "";
				// this.passwordHash = getMD5Hash(password);
				this.passwordHash = RealmBase.Digest(password, "MD5", "UTF-8");
			} else {
				throw new WebFormValueException(WebFormValueExceptionType.FORMDATA_INCORRECT, "password");
			}
		}
	}

	public boolean isSupervisor() {
		if (isSupervisor == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean hasRole(String roleName) {
		return true;
	}

	public void addRole(UserRole role) {
		roles.add(role);
	}

	public HashSet<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(HashSet<UserRole> roles) {
		this.roles = roles;
	}

	public void addApplication(ApplicationProfile ap) {
		HashMap<String, ApplicationProfile> apps = enabledApps.get(ap.appType);
		if (apps == null) {
			apps = new HashMap<String, ApplicationProfile>();
		}
		apps.put(ap.appID, ap);
		enabledApps.put(ap.appType, apps);
		applications.put(ap.appID, ap);
	}

	public void setHash(int hash) {
		this.hash = hash;
	}

	public int getHash() {
		return hash;
	}

	public HashMap<String, ApplicationProfile> getApplicationProfiles(String appType) {
		return enabledApps.get(appType);
	}

	public HashMap<String, ApplicationProfile> getApplicationProfiles() {
		HashMap<String, ApplicationProfile> allApps = new HashMap<String, ApplicationProfile>();
		for (HashMap<String, ApplicationProfile> a : enabledApps.values()) {
			allApps.putAll(a);
		}
		return allApps;
	}

	public ApplicationProfile getApplicationProfile(String appID) {
		return applications.get(appID);
	}

	public Collection<ApplicationProfile> getApplications() {
		return applications.values();
	}

	public void setPersistentValue(String key, String lang) {
		persistentValues.put(key, new PersistentValue(key, lang));

	}

	public Object getPesistentValue(String key) {
		PersistentValue pv = persistentValues.get(key);
		if (pv != null) {
			return pv.value;
		} else {
			return null;
		}
	}

	public boolean save() {
		try {
			if (id == 0) {
				id = sysDatabase.insert(this);
			} else {
				id = sysDatabase.update(this);
			}

			if (id < 0) {
				return false;
			} else {
				for (HashMap<String, ApplicationProfile> apps : enabledApps.values()) {
					for (ApplicationProfile appProfile : apps.values()) {
						if (appProfile.getStatus() != ApplicationStatusType.ON_LINE) {
							IApplicationDatabase appDb = sysDatabase.getApplicationDatabase();
							int res = appDb.createDatabase(appProfile.dbHost, appProfile.getDbName(), appProfile.dbLogin, appProfile.dbPwd);
							if (res == 0 || res == 1) {
								IDatabase dataBase = appProfile.getDatabase();
								IDeployer ad = dataBase.getDeployer();
								ad.init(appProfile);
								Class<?> appDatabaseInitializerClass = Class.forName(appProfile.getDbInitializerClass());
								IAppDatabaseInit dbInitializer = (IAppDatabaseInit) appDatabaseInitializerClass.newInstance();
								if (ad.deploy(dbInitializer) == 0) {
									appProfile.setStatus(ApplicationStatusType.ON_LINE);
									appProfile.save();
								} else {
									appProfile.setStatus(ApplicationStatusType.DEPLOING_FAILED);
									appProfile.save();
								}
							} else {
								appProfile.setStatus(ApplicationStatusType.DATABASE_NOT_CREATED);
								appProfile.save();
								return false;
							}
						}
					}
				}
			}
			return true;
		} catch (InstantiationException e) {
			Server.logger.errorLogEntry(e);
		} catch (IllegalAccessException e) {
			Server.logger.errorLogEntry(e);
		} catch (ClassNotFoundException e) {
			Server.logger.errorLogEntry(e);
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
		} catch (DatabasePoolException e) {
			Server.logger.errorLogEntry(e);
		}
		return false;

	}

	@Override
	public String toString() {
		return "userID=" + login + ", email=" + email;
	}

	public void setLogin(String l) {
		this.login = l;
	}

	public String getLogin() {
		return login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) throws WebFormValueException {
		if (email != null) {
			if (email.equalsIgnoreCase("")) {
				this.email = "";
			} else if (Util.addrIsCorrect(email)) {
				this.email = email;
			} else {
				throw new WebFormValueException(WebFormValueExceptionType.FORMDATA_INCORRECT, "email");
			}
		}
	}

	@JsonIgnore
	public String getPwd() {
		return password;
	}

	public void setPwd(String password) throws WebFormValueException {
		if (!("".equalsIgnoreCase(password))) {
			if (Util.pwdIsCorrect(password)) {
				this.password = password;
			} else {
				throw new WebFormValueException(WebFormValueExceptionType.FORMDATA_INCORRECT, "password");
			}
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Date getPrimaryRegDate() {
		return primaryRegDate;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public UserStatusType getStatus() {
		return status;
	}

	public void setStatus(UserStatusType status) {
		if ((this.status == UserStatusType.NOT_VERIFIED || this.status == UserStatusType.WAITING_FOR_VERIFYCODE) && status == UserStatusType.REGISTERED) {
			regDate = new Date();
		}
		this.status = status;
	}

	public void setIsSupervisor(int isSupervisor) {
		this.isSupervisor = isSupervisor;
	}

	public int getIsSupervisor() {
		return isSupervisor;
	}

	public void refresh(User u) {
		login = u.getLogin();
		userName = u.getUserName();
		password = u.getPwd();
		email = u.getEmail();
		isSupervisor = u.isSupervisor;

	}

	public boolean delete() {
		return false;

	}

	public AuthUser getAuthUser() {
		AuthUser aUser = new AuthUser();
		aUser.setLogin(login);
		aUser.setName(userName);
		aUser.setRoles(roles);
		aUser.setApplications(applications);
		return aUser;
	}

}
