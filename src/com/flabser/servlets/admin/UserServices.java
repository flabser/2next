package com.flabser.servlets.admin;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.exception.RuleException;
import com.flabser.localization.LocalizatorException;

public class UserServices {
	private ISystemDatabase sysDatabase;
	private int count;

	public UserServices() {
		sysDatabase = DatabaseFactory.getSysDatabase();
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

}
