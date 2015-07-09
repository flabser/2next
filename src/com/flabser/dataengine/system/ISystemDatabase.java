package com.flabser.dataengine.system;

import java.util.ArrayList;
import java.util.HashMap;

import com.flabser.dataengine.activity.IActivity;
import com.flabser.users.ApplicationProfile;
import com.flabser.users.User;

public interface ISystemDatabase {

	IActivity getActivity();

	User checkUser(String userID, String pwd, User user);
	User checkUser(String userID, String pwd, String hash, User user);
	User checkUserHash(String userID, String pwd, String hash, User user);

	User getUser(int id);
	User getUser(String id);
	User getUserByVerifyCode(String userID);


	int update(User user);
	int insert(User user);
	int getUsersCount(String condition);
	int getAllUsersCount(String condition);
	HashMap<String, User> getAllAdministrators();
	int deleteUser(int id);
	ArrayList<User> getUsers(String keyWord);
	ArrayList<User> getAllUsers(String condition, int calcStartEntry,	int pageSize);
	IApplicationDatabase getApplicationDatabase() throws InstantiationException, IllegalAccessException, ClassNotFoundException;

	int insert(ApplicationProfile applicationProfile);
	int update(ApplicationProfile applicationProfile);
	int deleteApplicationProfile(int id);

	ArrayList<ApplicationProfile> getAllApps(String string, int calcStartEntry, int pageSize);


}
