package com.flabser.script;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.IDatabase;
import com.flabser.exception.RuleException;
import com.flabser.rule.GlobalSetting;
import com.flabser.rule.page.PageRule;
import com.flabser.runtimeobj.page.Page;
import com.flabser.script.actions._ActionBar;
import com.flabser.script.mail._MailAgent;
import com.flabser.users.User;
import com.flabser.users.UserSession;


public class _Session extends _ScriptingObject {

	private _Database db;
	private IDatabase dataBase;
	private User user;
	private SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	private AppEnv env;
	private String formSesID;


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

	public String getCurrentDateAsString() {
		return dateformat.format(new Date());
	}


	public String getAppURL() {
		return user.getSession().host + "/" + user.env.appType;
	}


	public String getCurrentHost() {
		return user.getSession().host;
	}

	public String getCurrentUserID() {
		return user.getUserID();
	}

	public String getCurrentDateAsString(int plusDays) {
		return dateformat.format(getDatePlusDays(plusDays));
	}

	public _ActionBar createActionBar() {
		return new _ActionBar(this);
	}

	public Date getDatePlusDays(int plusDays) {
		Calendar date = new GregorianCalendar();
		date.setTime(new Date());
		date.add(Calendar.DAY_OF_YEAR, plusDays);
		return date.getTime();
	}

	public String getCurrentMonth() {
		Calendar date = new GregorianCalendar();
		date.setTime(new Date());
		return Integer.toString(date.get(Calendar.MONTH) + 1);
	}

	public String getCurrentYear() {
		Calendar date = new GregorianCalendar();
		date.setTime(new Date());
		return Integer.toString(date.get(Calendar.YEAR));
	}

	public _Database getCurrentDatabase() {
		return db;
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

	public String getLastURL() {
		return formSesID;
		//return user.getSession().history.getLastEntry().URL;
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

	public String getFormSesID() {
		return formSesID;
	}

	public void setFormSesID(String formSesID) {
		this.formSesID = formSesID;
	}


	public Set <String> getExpandedThread() {
		return new HashSet <String>();
	}

	public void setDocumentInConext(_Document doc) {
		// TODO Auto-generated method stub
		
	}

}
