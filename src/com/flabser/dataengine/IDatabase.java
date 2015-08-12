package com.flabser.dataengine;

import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.restful.data.IApplicationEntity;
import com.flabser.users.User;

import java.util.ArrayList;


public interface IDatabase {

	void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException,
	ClassNotFoundException, DatabasePoolException;

	int getVersion();

	IDeployer getDeployer();

	IFTIndexEngine getFTSearchEngine() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DatabasePoolException;

	ArrayList <Object[]> select(String condition, User user);

	ArrayList <IApplicationEntity> select(String condition, Class<IApplicationEntity> objClass, User user);

	int insert(String condition, User user);

	int update(String condition, User user);

	int delete(String condition, User user);

	void shutdown();

}
