package com.flabser.dataengine.system;

import java.util.ArrayList;
import java.util.HashMap;

import com.flabser.users.TempUser;
import com.flabser.users.User;

public interface ISystemDatabase {
	
	User checkUser(String userID, String pwd, User user);
	User checkUser(String userID, String pwd, String hash, User user);
	User checkUserHash(String userID, String pwd, String hash, User user);

	User getUser(String userID);
	User reloadUserData(User user, String userID);
	User reloadUserData(User user, int hash);

	int update(User user);
	int insert(User user);
	int getUsersCount(String condition);
	int getAllUsersCount(String condition);
	HashMap<String, User> getAllAdministrators();
	boolean deleteUser(int docID);
	ArrayList<User> getUsers(String keyWord);

	int calcStartEntry(int pageNum, int pageSize);
	int insert(TempUser tempUser);
	int update(TempUser tempUser);
	ArrayList<User> getAllUsers(String condition, int calcStartEntry,	int pageSize);
	
	
	
}
