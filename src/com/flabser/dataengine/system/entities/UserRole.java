package com.flabser.dataengine.system.entities;

import com.flabser.rule.constants.RunMode;


public class UserRole {

	private int id;
	private String name;
	private String description;
	private int appId;
	private RunMode isOn;

	public UserRole(int id, String name, String description, int appId, boolean isOn) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.appId = appId;
		this.setIsOn(RunMode.ON);
	}

	public UserRole(String name, String description, RunMode on) {
		this.name = name;
		this.description = description;
		this.setIsOn(on);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public RunMode getIsOn() {
		return isOn;
	}

	public void setIsOn(RunMode isOn) {
		this.isOn = isOn;
	}
	
}
