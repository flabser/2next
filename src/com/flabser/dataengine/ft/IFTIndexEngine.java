package com.flabser.dataengine.ft;

import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.users.ApplicationProfile;


public interface IFTIndexEngine {

	void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException,
	ClassNotFoundException, DatabasePoolException;


}
