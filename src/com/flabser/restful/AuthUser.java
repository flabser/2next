package com.flabser.restful;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.users.AuthFailedExceptionType;


@JsonRootName("authUser")
public class AuthUser {

	private String login;
	private String pwd;
	private AuthFailedExceptionType status;
	private String error;
	private String[] roles;

	public AuthUser() {
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
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
}
