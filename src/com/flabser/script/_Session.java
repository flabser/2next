package com.flabser.script;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.env.Environment;
import com.flabser.exception.RuleException;
import com.flabser.exception.WebFormValueException;
import com.flabser.rule.GlobalSetting;
import com.flabser.rule.page.PageRule;
import com.flabser.runtimeobj.page.Page;
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

	public GlobalSetting getGlobalSettings() {
		return env.globalSetting;
	}

	public String getAppURL() {
		return user.getSession().host + "/" + env.appType;
	}
	
	public String getFullAppURI() {
		return Environment.getFullHostName() + "/" + env.appType;
	}
	
	public String getCurrentUserID() {
		return user.getUserID();
	}
	
	public _ActionBar createActionBar() {
		return new _ActionBar(this);
	}
	
	public _MailAgent getMailAgent() {
		return new _MailAgent(this);
	}

	public _Page getPage(String id, _WebFormData webFormData) throws _Exception {
		PageRule rule;
		try {
			rule = (PageRule) env.ruleProvider.getRule("page", id);
			Page page = new Page(env, user.getSession(), rule);
			return new _Page(page, webFormData);
		} catch (RuleException e) {
			throw new _Exception(_ExceptionType.SCRIPT_ENGINE_ERROR, e.getMessage() + " function: _Session.getPage("
					+ id + ")");
		
		}
	}

	public _URL getURLOfLastPage() throws _Exception {
		return null;
		//return new _URL(user.getSession().history.getLastPageEntry().URL);
	}

	public User getUser() {
		return user;
	}	

	public String toString() {
		return "userid=" + user.getUserID() + ", database=" + dataBase.toString();
	}

}
