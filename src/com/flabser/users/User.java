package com.flabser.users;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.catalina.realm.RealmBase;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.jpa.Attachment;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.dataengine.system.entities.UserGroup;
import com.flabser.dataengine.system.entities.UserRole;
import com.flabser.env.Environment;
import com.flabser.exception.ServerServiceExceptionType;
import com.flabser.exception.WebFormValueException;
import com.flabser.localization.LanguageType;
import com.flabser.restful.pojo.AppUser;
import com.flabser.restful.pojo.Application;
import com.flabser.util.Util;

@JsonRootName("user")
public class User {
	public long id;
	public boolean isValid = false;
	public boolean isAuthorized;
	public String lastURL;
	// TODO need to save in sys database
	public LanguageType preferredLang = LanguageType.ENG;
	public final static String ANONYMOUS_USER = "anonymous";
	public final static String SYSTEM_USER = "system@flabser.com";
	private HashMap<String, HashMap<String, ApplicationProfile>> applicationsMap = new HashMap<>();
	private HashMap<String, ApplicationProfile> applications = new HashMap<>();
	private transient ISystemDatabase sysDatabase;
	private String login;
	private String oldLogin;
	private String userName;
	private HashSet<UserRole> roles = new HashSet<>();
	private HashSet<UserGroup> groups = new HashSet<>();
	private HashMap<String, PersistentValue> persistentValues = new HashMap<>();
	private Date primaryRegDate;
	private Date regDate;
	private Date lastUpdateDate;
	private String password;
	private String passwordHash = "";
	private String email = "";
	private boolean isSupervisor;
	private int loginHash;
	private String verifyCode;
	private UserStatusType status = UserStatusType.UNKNOWN;
	private String dbPwd;
	// private String defaultApp;
	private Attachment avatar;

	public User() {
		this.sysDatabase = DatabaseFactory.getSysDatabase();
		login = ANONYMOUS_USER;
	}

	public User(int id, String userName, Date primaryRegDate, Date regDate, String login, String email,
			boolean isSupervisor, String password, String passwordHash, String defaultDbPwd, int loginHash,
			String verifyCode, UserStatusType status, HashSet<UserGroup> groups, HashSet<UserRole> roles,
			List<ApplicationProfile> applications, boolean isValid, String avatarName) {
		this.sysDatabase = DatabaseFactory.getSysDatabase();
		this.id = id;
		this.userName = userName;
		this.primaryRegDate = primaryRegDate;
		this.regDate = regDate;
		this.login = login;
		oldLogin = login;
		this.email = email;
		this.isSupervisor = isSupervisor;
		this.password = password;
		this.passwordHash = passwordHash;
		this.dbPwd = defaultDbPwd;
		this.loginHash = loginHash;
		this.verifyCode = verifyCode;
		this.status = status;
		this.groups = groups;
		this.roles = roles;
		applications.forEach(this::addApplication);
		this.isValid = isValid;
		avatar = new Attachment();
		avatar.setRealFileName(avatarName);
		avatar.setFieldName("AVATAR");
	}

	@JsonIgnore
	public String getPasswordHash() {
		return passwordHash;
	}

	@JsonIgnore
	public void setPasswordHash(String password) throws WebFormValueException {
		if (!"".equalsIgnoreCase(password)) {
			if (Util.pwdIsStrong(password)) {
				this.passwordHash = password.hashCode() + "";
				// this.passwordHash = getMD5Hash(password);
				this.passwordHash = RealmBase.Digest(password, "MD5", "UTF-8");
			} else {
				throw new WebFormValueException(ServerServiceExceptionType.WEAK_PASSWORD, "password");
			}
		}
	}

	public String getDefaultDbPwd() {
		return dbPwd;
	}

	public void setDefaultDbPwd(String defaultDbPwd) {
		this.dbPwd = defaultDbPwd;
	}

	public boolean isSupervisor() {
		return isSupervisor;
	}

	public void setSupervisor(boolean isSupervisor) {
		this.isSupervisor = isSupervisor;
	}

	public void setRoles(HashSet<UserRole> roles) {
		this.roles = roles;
	}

	public void addApplication(ApplicationProfile ap) {
		HashMap<String, ApplicationProfile> apps = getEnabledApps().get(ap.appType);
		if (apps == null) {
			apps = new HashMap<>();
		}
		apps.put(ap.appID, ap);
		applicationsMap.put(ap.appType, apps);
		applications.put(ap.appID, ap);
	}

	public void setLoginHash(int hash) {
		this.loginHash = hash;
	}

	public int getLoginHash() {
		return loginHash;
	}

	public HashMap<String, ApplicationProfile> getApplicationProfiles(String appType) {
		return getEnabledApps().get(appType);
	}

	public AppUser getPOJO() {
		AppUser aUser = new AppUser();
		aUser.setLogin(getLogin());
		aUser.setName(getUserName());
		aUser.setEmail(getEmail());
		aUser.setRoles(getUserRoles());
		HashMap<String, Application> applications = new HashMap<String, Application>();
		for (ApplicationProfile ap : getApplicationProfiles().values()) {
			if (!ap.appType.equalsIgnoreCase(Environment.workspaceName)) {
				Application a = new Application(ap);
				a.setAppID(ap.appID);
				a.setAppName(ap.appName);
				a.setAppType(ap.appType);
				a.setOwner(ap.owner);
				a.setVisibilty(ap.getVisibilty());
				a.setStatus(ap.getStatus());
				a.setDescription(ap.getDesciption());
				applications.put(ap.appID, a);
			}
		}
		aUser.setApplications(applications);
		aUser.setRoles(getUserRoles());
		aUser.setStatus(getStatus());
		aUser.setAttachedFile(getAvatar());
		return aUser;
	}

	public HashMap<String, ApplicationProfile> getApplicationProfiles() {
		HashMap<String, ApplicationProfile> allApps = new HashMap<String, ApplicationProfile>();
		for (HashMap<String, ApplicationProfile> a : getEnabledApps().values()) {
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

	public LanguageType getPreferredLang() {
		return preferredLang;
	}

	public void setPreferredLang(LanguageType preferredLang) {
		this.preferredLang = preferredLang;
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

		if (id == 0) {
			primaryRegDate = new Date();
			lastUpdateDate = primaryRegDate;
			loginHash = (login + password).hashCode();
			if (dbPwd == null) {
				dbPwd = Util.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890");
			}
			id = sysDatabase.insert(this);
		} else {
			lastUpdateDate = new Date();
			id = sysDatabase.update(this);
		}

		if (id < 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public String toString() {
		return "userID=" + login + ", email=" + email + ", " + status.name();
	}

	public void setLogin(String l) {
		this.login = l;
	}

	public String getLogin() {
		return login;
	}

	public String getOldLogin() {
		return oldLogin;
	}

	@JsonGetter("dbLogin")
	public String getDBLogin() {
		return login.replace("@", "").replace(".", "").replace("-", "").toLowerCase();
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
				throw new WebFormValueException(ServerServiceExceptionType.FORMDATA_INCORRECT, "email");
			}
		}
	}

	@JsonIgnore
	public String getPwd() {
		return password;
	}

	public void setPwd(String password) throws WebFormValueException {
		if (!"".equalsIgnoreCase(password)) {
			if (Util.pwdIsStrong(password)) {
				this.password = password;
				setPasswordHash(password);
			} else {
				throw new WebFormValueException(ServerServiceExceptionType.WEAK_PASSWORD, "password");
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

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public UserStatusType getStatus() {
		return status;
	}

	public void setStatus(UserStatusType status) {
		if ((this.status == UserStatusType.NOT_VERIFIED || this.status == UserStatusType.WAITING_FOR_VERIFYCODE)
				&& status == UserStatusType.REGISTERED) {
			regDate = new Date();
		}
		this.status = status;
	}

	public void refresh(AppUser appUser) throws WebFormValueException {
		login = appUser.getLogin();
		userName = appUser.getName();
		if (!appUser.getPwdNew().equals("") && appUser.getPwdNew().equals(appUser.getPwdRepeat())) {
			setPwd(appUser.getPwdNew());
			status = UserStatusType.REGISTERED;
		}
		email = appUser.getEmail();
		avatar = appUser.getAttachedFile();

	}

	public void refresh(User u) throws WebFormValueException {
		login = u.getLogin();
		userName = u.getUserName();
		String p = u.getPwd();
		if (p != null && !p.equals("")) {
			setPwd(u.getPwd());
		}
		email = u.getEmail();
		status = u.getStatus();
		isSupervisor = u.isSupervisor;

	}

	public Attachment getAvatar() {
		return avatar;
	}

	public HashMap<String, HashMap<String, ApplicationProfile>> getEnabledApps() {
		return applicationsMap;
	}

	public HashSet<UserRole> getUserRoles() {
		return roles;
	}

	public void setUserRoles(HashSet<UserRole> userRoles) {
		this.roles = userRoles;
	}

	public HashSet<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(HashSet<UserGroup> groups) {
		this.groups = groups;
	}

	public long getID() {
		return id;
	}

}
