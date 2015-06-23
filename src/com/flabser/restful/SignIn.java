package com.flabser.restful;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.users.AuthFailedExceptionType;

@JsonRootName("signin")
public class SignIn {
	private String login;
	private String pwd;
	private AuthFailedExceptionType status;
	private String error;
	
	@JsonProperty
	public ArrayList<HashMap> signin = new ArrayList<HashMap>();	
	
	public SignIn() {	}


	public SignIn(HashMap<String,Object> fields) {
		signin.add(fields);
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

