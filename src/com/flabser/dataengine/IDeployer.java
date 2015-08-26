package com.flabser.dataengine;

import com.flabser.dataengine.pool.DatabasePoolException;

public interface IDeployer {
	void init(IDatabase parentDb) throws InstantiationException, IllegalAccessException, ClassNotFoundException, DatabasePoolException;	
	int deploy(IAppDatabaseInit dbInit);
	int remove();
}
