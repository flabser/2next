package com.flabser.dataengine.system.entities;

import java.util.HashSet;

public class UserGroup  {

	private int id;
	private String name;
	private String description;
	private HashSet<UserRole> roles = new HashSet<>();

	public UserGroup(int id, String name, String description, HashSet<UserRole> roles) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.roles = roles;
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

	public HashSet<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(HashSet<UserRole> roles) {
		this.roles = roles;
	}
}
