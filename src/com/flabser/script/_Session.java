package com.flabser.script;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.IDatabase;
import com.flabser.env.Environment;
import com.flabser.rule.GlobalSetting;
import com.flabser.rule.Role;
import com.flabser.script.actions._ActionBar;
import com.flabser.script.mail._MailAgent;
import com.flabser.users.User;
import com.flabser.users.UserSession;

import java.util.ArrayList;


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

	public ArrayList<Role> getRolesList(){
		ArrayList <Role> rolesList = (ArrayList<Role>) env.globalSetting.roleCollection.getRolesList().clone();
		return rolesList;
	}
		
	public String getFullAppURI() {
		return Environment.getFullHostName() + "/" + env.appType;
	}
	
	public _ActionBar createActionBar() {
		return new _ActionBar(this);
	}
	
	public _MailAgent getMailAgent() {
		return new _MailAgent(this);
	}

	public User getUser() {
		return user;
	}	

	public String toString() {
		return "userid=" + user.getLogin() + ", database=" + dataBase.toString();
	}

}
