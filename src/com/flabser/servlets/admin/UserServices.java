package com.flabser.servlets.admin;

import java.sql.SQLException;
import java.util.*;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.IDeployer;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.IApplicationDatabase;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.exception.RuleException;
import com.flabser.localization.LocalizatorException;
import com.flabser.runtimeobj.RuntimeObjUtil;
import com.flabser.users.User;
import com.flabser.users.ApplicationProfile;

public class UserServices {
	private ISystemDatabase sysDatabase;
	private int count;

	public UserServices() {
		sysDatabase = DatabaseFactory.getSysDatabase();
	}

	public String getUserAsXML(String id) throws RuleException, LocalizatorException {
		String xmlContent = "", ea = "";
		User user = sysDatabase.getUser(Integer.parseInt(id));

		for (ApplicationProfile app : user.enabledApps.values()) {
			ea += app.toXML();
		}

		if (user.getLogin() != null) {
			xmlContent += "<login>" + user.getLogin() + "</login><id>" + user.id + "</id>"
					+ "<username>" + user.getUserName() + "</username><email>" + user.getEmail() + "</email>" 
					+ "<password>" + user.getPassword() + "</password><isadmin>" + user.isSupervisor() + "</isadmin>"
							+ "<hash>" + user.getHash() + "</hash><enabledapps>" + ea + "</enabledapps>";

		}

		return xmlContent;
	}

	public String getBlankUserAsXML() throws RuleException, LocalizatorException {
		String xmlContent = "";
		return xmlContent;
	}

	int deleteUser(String id) {
		int docID = Integer.parseInt(id);
		return sysDatabase.deleteUser(docID);
	}

	public String getUserListWrapper(String keyWord, int pageNum, int pageSize) {
		String condition = "", xmlFragment = "";
		if (keyWord != null) {
			condition = "USERID LIKE '" + keyWord + "%'";
		}
		count = sysDatabase.getAllUsersCount(condition);
		ArrayList<User> fl = sysDatabase
				.getAllUsers(condition, RuntimeObjUtil.calcStartEntry(pageNum, pageSize), pageSize);

		Iterator<User> it = fl.iterator();
		while (it.hasNext()) {
			User user = it.next();
			xmlFragment += "<entry id=\"" + user.id + "\" ><login>" + user.getLogin() + "</login>"
					+ "<issupervisor>" + user.isSupervisor() + "</issupervisor><email>" + user.getEmail()
					+ "</email><username>" + user.getUserName() + "</username></entry>";
		}

		return xmlFragment;
	}

	public int getCount() {
		return count;
	}

	public String deploy(String userID) throws SQLException, InstantiationException, IllegalAccessException,
			ClassNotFoundException, DatabasePoolException {
		String result = "";
		User user = sysDatabase.getUser(Integer.parseInt(userID));
		IApplicationDatabase appDb = sysDatabase.getApplicationDatabase();
		for (ApplicationProfile appProfile : user.enabledApps.values()) {
			int res = appDb.createDatabase(appProfile.dbHost, appProfile.getDbName(), appProfile.owner, appProfile.dbPwd);
			if (res == 0 || res == 1) {				
					Class cls = Class.forName(appProfile.getImpl());
					IDatabase dataBase = (IDatabase) cls.newInstance();
					IDeployer ad = dataBase.getDeployer();
					ad.init(appProfile);
					ad.deploy(null);
			}
		}
		return result;
	}

	public String remove(String userID) throws SQLException, InstantiationException, IllegalAccessException,
			ClassNotFoundException, DatabasePoolException {
		String result = "";
		User user = sysDatabase.getUser(Integer.parseInt(userID));
		IApplicationDatabase appDb = sysDatabase.getApplicationDatabase();
		for (ApplicationProfile appProfile : user.enabledApps.values()) {
			appDb.removeDatabase("localhost", appProfile.getDbName());
			
		}
		return result;
	}

}
