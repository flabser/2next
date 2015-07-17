package com.flabser.solutions.postgresql;

import com.flabser.dataengine.DatabaseCore;
import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.users.ApplicationProfile;

public class FTIndexEngine extends DatabaseCore implements IFTIndexEngine {
	ApplicationProfile appProfile;

	@Override
	public void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException,
	ClassNotFoundException, DatabasePoolException {
		this.appProfile = appProfile;
		pool = getPool(Database.driver, appProfile);
	}

}
