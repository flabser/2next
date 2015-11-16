package com.flabser.restful.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.flabser.dataengine.jpa.Attachment;
import com.flabser.dataengine.system.entities.UserRole;
import com.flabser.env.Environment;
import com.flabser.exception.AuthFailedExceptionType;
import com.flabser.users.AuthModeType;
import com.flabser.users.UserStatusType;

@JsonRootName("authUser")
public class AppUser {
	private String login;
	private String name;
	protected String email;
	private String pwd;
	private String pwdNew;
	private String pwdRepeat;
	private UserStatusType status = UserStatusType.UNKNOWN;
	private String error;
	private String redirect;
	private AuthModeType authMode = AuthModeType.UNDEFINED;
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

	@JsonGetter("pwd_new")
	public String getPwdNew() {
		return pwdNew;
	}

	@JsonSetter("pwd_new")
	public void setPwdNew(String pwdNew) {
		this.pwdNew = pwdNew;
	}

	@JsonGetter("pwd_repeat")
	public String getPwdRepeat() {
		return pwdRepeat;
	}

	@JsonSetter("pwd_repeat")
	public void setPwdRepeat(String pwdRepeat) {
		this.pwdRepeat = pwdRepeat;
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

	public String getError() {
		return error;
	}

	public void setError(AuthFailedExceptionType error, String lang) {
		this.error = Environment.vocabulary.getWord(error.name(), lang);
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
