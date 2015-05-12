package com.flabser.dataengine;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.env.Environment;


public class DatabaseFactory implements Const {
	 
		
	public static ISystemDatabase getSysDatabase(){
		return Environment.systemBase;
	}
	
}
