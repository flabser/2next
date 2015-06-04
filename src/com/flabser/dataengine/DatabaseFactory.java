package com.flabser.dataengine;

import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.env.Environment;

public class DatabaseFactory{
	
	public static ISystemDatabase getSysDatabase(){
		return Environment.systemBase;
	}
	
}
