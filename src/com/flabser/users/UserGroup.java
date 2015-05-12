package com.flabser.users;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UserGroup  {

	private String name;
	private String owner;
	private String description;
	private Set<String> members = new HashSet<String>();
	private HashSet<UserRole> roles = new HashSet<UserRole>();

	

	public String getName(){
		return this.name;
	}

	public void setOwner(String emp){
		owner = emp;
       
	}

	public String getOwner(){
		return owner;
	}

	public void setName(String name){
		if (!"".equalsIgnoreCase(name) && name != null) {
			if (!name.startsWith("[")) {
				if (name.length() <= 31) {
					name = "[" + name;
				} else {
					name = "[" + name.substring(0, 31);
				}
			}
			if (!name.endsWith("]")) {
				if (name.length() <= 31) {
					name = name + "]";
				} else {
					name = name.substring(0, 31) + "]";
				}				
			}
			this.name = name;
			
		}		
	}

	public String getDescription(){
		return description;
	}

	public void setDescription(String description){
		this.description = description;
		
	}

	public void addRole(UserRole role) {
		
	}

	public void addMember(String user){
		
	}

	public void deleteMember(String user){
		members.remove(user);
	}

	public void resetMembers() {
		members.clear();
	}
	
	public Set<String> getMembers(){
		return members;
	}

	public void deleteRole(UserRole role) {
		roles.remove(role);
	}

	public HashSet<String> getAllRoles(){
		HashSet<String> roles = new HashSet<String>();
		for (UserRole role : this.getRoles()) {
			roles.add(role.getName());
		}
	
		return roles;
	}
	
	public HashSet<UserRole> getRoles() {
		return roles;
	}

	public boolean hasRole(String roleName) {
		for (UserRole role : roles) {
			if (role.getName().equalsIgnoreCase(roleName)) {
				return true;
			}
		}
		return false;
	}

	

	public boolean hasRole(UserRole role) {
		return roles.contains(role);
	}

	public void setRoles(ArrayList<UserRole> newRoles) {		
		
	}
	
	public void setMembers(Set<String> newMembers){
	
	}

	
	public int isRead(){		
		return 1;
	}

	
	public String toString(){
		return this.getName() + " " + this.getDescription();
	}
	
	public boolean equals(Object o) {
		return false;
		
	}
}
