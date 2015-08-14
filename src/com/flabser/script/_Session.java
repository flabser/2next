package com.flabser.script;

import java.util.ArrayList;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.dataengine.IDatabase;
import com.flabser.env.Environment;
import com.flabser.localization.LanguageType;
import com.flabser.restful.AuthUser;
import com.flabser.rule.Role;
import com.flabser.script.actions._ActionBar;
import com.flabser.script.mail._MailAgent;
import com.flabser.users.User;
import com.flabser.users.UserSession;
import com.flabser.users.UserSession.ActiveApplication;

public class _Session {
	private IDatabase dataBase;
	private AppTemplate env;
	private UserSession userSession;

	public _Session(AppTemplate env, UserSession userSession) {
		this.env = env;
		ActiveApplication aa = userSession.getActiveApplication(env.appType);
		if (aa != null) {
			dataBase = aa.getDataBase();
		}
		this.userSession = userSession;
	}

	public _AppEntourage getAppEntourage() {
		return new _AppEntourage(this, env);
	}

	public IDatabase getDatabase() {
		return dataBase;
	}

	public ArrayList<Role> getRolesList() {
		@SuppressWarnings("unchecked")
		ArrayList<Role> rolesList = (ArrayList<Role>) env.globalSetting.roleCollection.getRolesList().clone();
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

	@Deprecated
	public User getAppUser() {
		return userSession.currentUser;
	}

	public AuthUser getUser() {
		return userSession.getUserPOJO();
	}

	public void switchLang(LanguageType lang) {
		userSession.setLang(lang.name());
	}

	public String getAppType() {
		return env.appType;
	}

	@Override
	public String toString() {
		return "userid=" + userSession.currentUser.getLogin() + ", database=" + dataBase.toString();
	}

}
