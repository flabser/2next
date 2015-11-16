package com.flabser.dataengine.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.flabser.dataengine.activity.IActivity;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.dataengine.system.entities.IUser;
import com.flabser.dataengine.system.entities.Invitation;
import com.flabser.restful.pojo.AppUser;
import com.flabser.users.User;

public interface ISystemDatabase {

	IActivity getActivity();

	User checkUserHash(String userID, String pwd, String hash);

	@Deprecated
	User getUser(int id);

	User getUser(String id);

	User getUserByVerifyCode(String userID);

	int update(User user);

	int insert(User user);

	int getUsersCount(String condition);

	int getAllUsersCount(String condition);

	HashMap<String, IUser> getAllAdministrators();

	int deleteUser(int id);

	ArrayList<IUser> getUsers(String keyWord);

	ArrayList<IUser> getAllUsers(String condition, int calcStartEntry, int pageSize);

	ArrayList<AppUser> getAppUsers(User user, String contextID, int calcStartEntry, int pageSize);

	IApplicationDatabase getApplicationDatabase()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException;

	int insert(ApplicationProfile applicationProfile);

	int update(ApplicationProfile applicationProfile);

	int deleteApplicationProfile(int id);

	ArrayList<ApplicationProfile> getAllApps(String string, int calcStartEntry, int pageSize);

	ApplicationProfile getApp(String string);

	ApplicationProfile getApp(int id);

	User getUser(long id);

	byte[] getUserAvatarStream(long id);

	long insert(Invitation user);

	long update(Invitation invitation);

	List<AppUser> getInvitedUsers(User currentUser, String contexID, int calcStartEntry, int i);

}
