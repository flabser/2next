package com.flabser.users;

import javax.xml.bind.annotation.XmlTransient;

import com.flabser.rule.Role;

import java.io.Serializable;

public class UserRole implements Serializable{
	public static final long serialVersionUID = 1L;
	

	private String name;
	private String description;
	private String application;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public UserRole(Role role) {
		name = role.name;
		description = role.description;
        application = role.appID;
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
	
	
	public int isRead(){		
		return 1;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole)) return false;

        UserRole userRole = (UserRole) o;

        if (application != null ? !application.equals(userRole.application) : userRole.application != null)
            return false;
        if (name != null ? !name.equals(userRole.name) : userRole.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (application != null ? application.hashCode() : 0);
        return result;
    }

    public String toString() {

		return name;
	}

	
	
}
