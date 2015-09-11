package com.flabser.restful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.flabser.dataengine.jpa.Attachment;
import com.flabser.dataengine.system.entities.UserRole;
import com.flabser.exception.AuthFailedExceptionType;
import com.flabser.users.AuthModeType;
import com.flabser.users.UserStatusType;

@JsonRootName("authUser")
public class AppUser {
	private String login;
	private String name;
	private String email;
	private String pwd;
	private UserStatusType status = UserStatusType.UNKNOWN;
	private AuthFailedExceptionType error;
	private String redirect;
	private String defaultApp;
	private AuthModeType authMode;
	private ArrayList<String> appRoles = new ArrayList<String>();
	private HashMap<String, Application> applications = new HashMap<String, Application>();
	private Attachment attachedFile;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@JsonGetter("roles")
	public ArrayList<String> getAppRoles() {
		appRoles.add("test1");
		appRoles.add("test2");

		return appRoles;
	}

	@JsonSetter("roles")
	public void setAppRoles(ArrayList<String> roles) {
		this.appRoles = roles;
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

	public void setApplications(HashMap<String, Application> applications) {
		this.applications = applications;
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

	public Attachment getAttachedFile() {
		return attachedFile;
	}

	public void setAttachedFile(Attachment attachedFile) {
		this.attachedFile = attachedFile;
	}

	@Override
	public String toString() {
		return login + ", email=" + email;
	}

}
