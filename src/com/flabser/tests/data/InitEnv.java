package com.flabser.tests.data;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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

public class InitEnv {
	UserSession us;
	String appType = "CashTracker";
	AppTemplate at;
	_Session ses;
	IDatabase db;
	EntityManagerFactory factory;
	EntityManager em;

	@Before
	public void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DatabasePoolException {
		factory = Persistence.createEntityManagerFactory("JPA");
		em = factory.createEntityManager();
		System.out.println(factory.getProperties());

		Server.logger = new SimpleLogger();
		Environment.systemBase = new com.flabser.dataengine.system.SystemDatabase();
		ISystemDatabase systemDatabase = DatabaseFactory.getSysDatabase();
		User user = systemDatabase.checkUserHash(Settings.login, Settings.pwd, null);
		us = new UserSession(user);
		HashMap<String, ApplicationProfile> hh = us.currentUser.getApplicationProfiles(appType);
		ApplicationProfile ap = (ApplicationProfile) hh.values().toArray()[0];
		us.init(ap.appID);
		AppTemplate at = new AppTemplate(appType, "global.xml");
		ses = new _Session(at, us);
		db = us.getDataBase(appType);

	}
}
