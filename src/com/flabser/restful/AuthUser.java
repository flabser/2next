package com.flabser.restful;

import java.util.HashMap;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.users.ApplicationProfile;
import com.flabser.users.AuthFailedExceptionType;
import com.flabser.users.UserRole;
import com.flabser.users.VisibiltyType;

@JsonRootName("authUser")
public class AuthUser {

	private String login;
	private String name;
	private String pwd;
	private AuthFailedExceptionType status;
	private String error;
	private String redirect;
	private String[] roles = new String[0];
	private HashMap<String, Application> applications = new HashMap<String, Application>();
	private String defaultApp;

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

	public AuthFailedExceptionType getStatus() {
		return status;
	}

	public void setStatus(AuthFailedExceptionType status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
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
			Application a = new Application();
			a.appID = ap.appID;
			a.appName = ap.appName;
			a.appType = ap.appType;
			a.owner = ap.owner;
			a.visibilty = ap.getVisibilty();
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
	public void setRoles(HashSet<UserRole> roles2) {
		// TODO Auto-generated method stub

	}

	class Application {
		public String appID;
		public String appName;
		public String owner;
		public String defaultURL;
		private VisibiltyType visibilty;
		public String appType;

		public String getDefaultURL() {
			return defaultURL;
		}

		public void setDefaultURL(String defaultURL) {
			this.defaultURL = defaultURL;
		}

		public String getAppType() {
			return appType;
		}

		public String getAppID() {
			return appID;
		}

		public String getAppName() {
			return appName;
		}

		public String getOwner() {
			return owner;
		}

		public VisibiltyType getVisibilty() {
			return visibilty;
		}

	}

}
