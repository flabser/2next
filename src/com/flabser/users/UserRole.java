package com.flabser.users;

import com.flabser.rule.Role;

import java.io.Serializable;

@Deprecated
public class UserRole implements Serializable {
	public static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private String appID;

	public String getApplication() {
		return appID;
	}

	public void setApplication(String application) {
		this.appID = application;
	}

	public UserRole(Role role) {
		name = role.name;
		description = role.description;
		appID = role.appID;
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

	public int isRead() {
		return 1;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof UserRole)) {
			return false;
		}

		UserRole userRole = (UserRole) o;

		if (appID != null ? !appID.equals(userRole.appID) : userRole.appID != null) {
			return false;
		}
		if (name != null ? !name.equals(userRole.name) : userRole.name != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (appID != null ? appID.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {

		return name;
	}

}
