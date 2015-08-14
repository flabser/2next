package com.flabser.tests;

import java.util.HashMap;

import org.junit.Before;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.env.Environment;
import com.flabser.log.SimpleLogger;
import com.flabser.script._Session;
import com.flabser.server.Server;
import com.flabser.users.User;
import com.flabser.users.UserSession;
import com.flabser.users.UserSession.ActiveApplication;

public class InitEnv {
	protected UserSession us;
	protected AppTemplate at;
	protected _Session ses;
	protected ApplicationProfile ap;
	protected ActiveApplication aa;
	protected IDatabase db;
	protected User user;
	private String appType = "CashTracker";

	@Before
	public void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DatabasePoolException {
		Server.logger = new SimpleLogger();
		Environment.systemBase = new com.flabser.dataengine.system.SystemDatabase();
		ISystemDatabase systemDatabase = DatabaseFactory.getSysDatabase();
		user = systemDatabase.checkUserHash(Settings.login, Settings.pwd, null);
		us = new UserSession(user);
		HashMap<String, ApplicationProfile> hh = us.currentUser.getApplicationProfiles(appType);
		ap = (ApplicationProfile) hh.values().toArray()[0];
		us.init(ap.appID);
		AppTemplate at = new AppTemplate(appType, "global.xml");
		ses = new _Session(at, us);
		aa = us.getActiveApplication(appType);
		db = aa.getDataBase();

	}
}
