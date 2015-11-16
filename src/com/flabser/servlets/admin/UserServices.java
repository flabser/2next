package com.flabser.servlets.admin;

import java.sql.SQLException;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.IApplicationDatabase;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.exception.RuleException;
import com.flabser.localization.LocalizatorException;
import com.flabser.users.User;

public class UserServices {
	private ISystemDatabase sysDatabase;
	private int count;

	public UserServices() {
		sysDatabase = DatabaseFactory.getSysDatabase();
	}

	public String getUserAsXML(String id) throws RuleException, LocalizatorException {
		String xmlContent = "", ea = "";
		User user = sysDatabase.getUser(Integer.parseInt(id));

		for (ApplicationProfile app : user.getApplications()) {
			ea += app.toXML("ENG");
		}

		if (user.getLogin() != null) {
			xmlContent += "<login>" + user.getLogin() + "</login><id>" + user.id + "</id>" + "<username>"
					+ user.getUserName() + "</username><email>" + user.getEmail() + "</email>" + "<password>"
					+ user.getPwd() + "</password><isadmin>" + user.isSupervisor() + "</isadmin>" + "<hash>"
					+ user.getLoginHash() + "</hash><enabledapps>" + ea + "</enabledapps>";

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

	public int getCount() {
		return count;
	}

	public String remove(String userID) throws SQLException, InstantiationException, IllegalAccessException,
			ClassNotFoundException, DatabasePoolException {
		String result = "";
		User user = sysDatabase.getUser(Integer.parseInt(userID));
		IApplicationDatabase appDb = sysDatabase.getApplicationDatabase();
		for (ApplicationProfile appProfile : user.getApplications()) {
			appDb.removeDatabase(appProfile.getDbName());

		}
		return result;
	}

}
