package com.flabser.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.script._IContent;
import com.flabser.users.ApplicationStatusType;
import com.flabser.users.VisibiltyType;

public class Application implements _IContent {
	private String appID;
	private String appName;
	private String owner;
	private String defaultURL;
	private VisibiltyType visibilty;
	private String appType;
	private ApplicationStatusType status;

	@JsonIgnore
	private ApplicationProfile applicationProfile;

	public Application(ApplicationProfile applicationProfile) {
		this.applicationProfile = applicationProfile;
	}

	public String getDefaultURL() {
		return defaultURL;
	}

	public void setDefaultURL(String defaultURL) {
		this.defaultURL = defaultURL;
	}

	public void setAppType(String appType) {
		this.appType = appType;
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

	public ApplicationStatusType getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatusType status) {
		this.status = status;
	}

	@Override
	public StringBuffer toXML() {
		StringBuffer output = new StringBuffer(1000);
		return output.append("<apptype>" + appType + "</apptype><appname>" + getAppName() + "</appname>" + "<owner>" + getOwner() + "</owner><appid>"
				+ getAppID() + "</appid><status>" + status + "</status>");
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setVisibilty(VisibiltyType visibilty) {
		this.visibilty = visibilty;
	}

	@JsonIgnore
	public void addRole(String name, String descr) {
		applicationProfile.addRole(name, descr);

	}

}