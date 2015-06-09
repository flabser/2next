package com.flabser.script;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.IDatabase;
import com.flabser.env.Environment;
import com.flabser.rule.GlobalSetting;
import com.flabser.script.actions._ActionBar;
import com.flabser.script.mail._MailAgent;
import com.flabser.users.User;
import com.flabser.users.UserSession;


public class _Session {
	private IDatabase dataBase;
	private User user;
	private AppEnv env;

	public _Session(AppEnv env, UserSession userSession) {
		this.env = env;
		dataBase = userSession.getDataBase();
		this.user = userSession.currentUser;
	}

	public _AppEntourage getAppEntourage() {
		return new _AppEntourage(this, env);
	}

	public IDatabase getDatabase() {
		return dataBase;
	}
	
	public GlobalSetting getGlobalSettings() {
		return env.globalSetting;
	}
		
	public String getFullAppURI() {
		return Environment.getFullHostName() + "/" + env.appType;
	}
	
	public String getCurrentUserID() {
		return user.getLogin();
	}
	
	public _ActionBar createActionBar() {
		return new _ActionBar(this);
	}
	
	public _MailAgent getMailAgent() {
		return new _MailAgent(this);
	}

	public _URL getURLOfLastPage() throws _Exception {
		return null;
		//return new _URL(user.getSession().history.getLastPageEntry().URL);
	}

	public User getUser() {
		return user;
	}	

	public String toString() {
		return "userid=" + user.getLogin() + ", database=" + dataBase.toString();
	}

}
