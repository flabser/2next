package com.flabser.dataengine.system.entities;

import java.util.Date;

import com.flabser.restful.pojo.AppUser;

public interface IUser {

	long getID();

	Date getRegDate();

	String getLogin();

	AppUser getPOJO();

}
