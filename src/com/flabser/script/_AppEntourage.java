package com.flabser.script;


import com.flabser.appenv.AppEnv;
import com.flabser.server.Server;


public class _AppEntourage {
	private AppEnv env;
	private _Session ses;

	public _AppEntourage(_Session ses, AppEnv env) {
		this.ses = ses;
		this.env = env;
	}

	public String getServerVersion(){
		return Server.serverVersion;
	}

	public String getBuildTime(){
		return Server.compilationTime;
	}

	public String getGeneralName(){
		return env.globalSetting.implemantion;	
	}
	
	public String getAppName(){
		return env.appType;
	}

	
	

	
}
