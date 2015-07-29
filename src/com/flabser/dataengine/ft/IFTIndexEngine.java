package com.flabser.dataengine.ft;

import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.entities.ApplicationProfile;


public interface IFTIndexEngine {

	void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException,
	ClassNotFoundException, DatabasePoolException;


}
