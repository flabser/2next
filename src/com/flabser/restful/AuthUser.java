package com.flabser.restful;

import java.util.HashMap;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.dataengine.system.entities.UserRole;
import com.flabser.users.AuthFailedExceptionType;
import com.flabser.users.AuthModeType;
import com.flabser.users.UserStatusType;

@JsonRootName("authUser")
public class AuthUser {

	private String login;
	private String name;
	private String pwd;
	private UserStatusType status = UserStatusType.UNKNOWN;
	private AuthFailedExceptionType error;
	private String redirect;
	private String[] roles = new String[0];
	private HashMap<String, Application> applications = new HashMap<String, Application>();
	private String defaultApp;
	private AuthModeType authMode;

	public AuthUser() {
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public UserStatusType getStatus() {
		return status;
	}

	public void setStatus(UserStatusType status) {
		this.status = status;
	}

	public AuthFailedExceptionType getError() {
		return error;
	}

	public void setError(AuthFailedExceptionType error) {
		this.error = error;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String r) {
		this.redirect = r;
	}

	public HashMap<String, Application> getApplications() {
		return applications;
	}

	public void setApplications(HashMap<String, ApplicationProfile> apps) {
		for (ApplicationProfile ap : apps.values()) {
			Application a = new Application(ap);
			a.setAppID(ap.appID);
			a.setAppName(ap.appName);
			a.setAppType(ap.appType);
			a.setOwner(ap.owner);
			a.setVisibilty(ap.getVisibilty());
			a.setStatus(ap.status);
			this.applications.put(ap.appID, a);
		}

	}

	public String getDefaultApp() {
		return defaultApp;
	}

	public void setDefaultApp(String defaultApp) {
		this.defaultApp = defaultApp;
	}

	@JsonIgnore
	public void setRoles(HashSet<UserRole> roles) {
		// TODO Auto-generated method stub

	}

	public AuthModeType getAuthMode() {
		return authMode;
	}

	public void setAuthMode(AuthModeType authMode) {
		this.authMode = authMode;
	}

}
