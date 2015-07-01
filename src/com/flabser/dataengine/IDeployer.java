package com.flabser.dataengine;

import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.users.ApplicationProfile;

public interface IDeployer {
	void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException, ClassNotFoundException, DatabasePoolException;	
	int deploy(IAppDatabaseInit dbInit);
	int remove();
}
