package com.flabser.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.apptemplate.WorkModeType;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.env.Environment;
import com.flabser.exception.RuleException;
import com.flabser.exception.WebFormValueException;
import com.flabser.localization.LanguageType;
import com.flabser.restful.pojo.AppUser;
import com.flabser.restful.pojo.Application;
import com.flabser.rule.Role;
import com.flabser.runtimeobj.caching.ICache;
import com.flabser.runtimeobj.page.Page;
import com.flabser.script.mail._MailAgent;
import com.flabser.server.Server;
import com.flabser.users.ApplicationStatusType;
import com.flabser.users.AuthModeType;
import com.flabser.users.User;

public class _Session implements ICache {

	private IDatabase dataBase;
	private AppTemplate env;
	private User currentUser;
	private AuthModeType authMode;
	private LanguageType lang;
	private HashMap<String, _Page> cache = new HashMap<String, _Page>();
	private HttpSession jses;
	private String contexID;
	private static final int MONTH_TIME = 60 * 60 * 24 * 365;
	public ArrayList<_Session> descendants = new ArrayList<_Session>();

	public _Session(AppTemplate env, HttpSession jses, String contextID, User user) {
		this.env = env;
		currentUser = user;
		this.contexID = contextID;
	}

	public void setUser(User user) {
		currentUser = user;

	}

	public User getCurrentUser() {
		return currentUser;
	}

	public _AppEntourage getAppEntourage() {
		return new _AppEntourage(this, env);
	}

	public IDatabase getDatabase() {

		if (dataBase == null) {
			if (currentUser.getLogin() != User.ANONYMOUS_USER) {
				if (env.globalSetting.getWorkMode() == WorkModeType.COMMON) {
					// TODO commented till a common application will be use some
					// database
					// ApplicationProfile app = new ApplicationProfile(env);
					// dataBase = app.getDatabase();
				} else if (env.globalSetting.getWorkMode() == WorkModeType.CLOUD) {
					ApplicationProfile ap = DatabaseFactory.getSysDatabase().getApp(contexID);
					if (ap != null && ap.getStatus() == ApplicationStatusType.ON_LINE) {
						dataBase = ap.getDatabase();
					} else {
						Server.logger.errorLogEntry(
								"database not available or user has not had permissions to access to this one");
					}
				}
			} else {
				Server.logger.errorLogEntry("\"" + User.ANONYMOUS_USER + "\" cannot get some database insatnce");
			}
		}

		return dataBase;
	}

	public ArrayList<Role> getRolesList() {
		@SuppressWarnings("unchecked")
		ArrayList<Role> rolesList = (ArrayList<Role>) env.globalSetting.roleCollection.getRolesList().clone();
		return rolesList;
	}

	public String getBaseAppURL() {
		return env.getHostName();
	}

	public void setAuthMode(AuthModeType authMode) {
		this.authMode = authMode;

	}

	public AuthModeType getAuthMode() {
		return authMode;
	}

	public String getWorkspaceURL() {
		if (authMode == AuthModeType.DIRECT_LOGIN) {
			return "";
		} else {
			return env.getWorkspaceURL();
		}
	}

	public _MailAgent getMailAgent() {
		return new _MailAgent(this);
	}

	public User getUser() {
		return getCurrentUser();
	}

	public AppUser getAppUser() {
		AppUser aUser = new AppUser();
		aUser.setLogin(getCurrentUser().getLogin());
		aUser.setName(getCurrentUser().getUserName());
		aUser.setEmail(getCurrentUser().getEmail());
		aUser.setRoles(getCurrentUser().getUserRoles());
		HashMap<String, Application> applications = new HashMap<String, Application>();
		for (ApplicationProfile ap : getCurrentUser().getApplicationProfiles().values()) {
			if (!ap.appType.equalsIgnoreCase(Environment.workspaceName)) {
				Application a = new Application(ap);
				a.setAppID(ap.appID);
				a.setAppName(ap.appName);
				a.setAppType(ap.appType);
				a.setOwner(ap.owner);
				a.setVisibilty(ap.getVisibilty());
				a.setStatus(ap.getStatus());
				a.setDescription(ap.getDesciption());
				applications.put(ap.appID, a);
			}
		}
		aUser.setApplications(applications);
		aUser.setRoles(getCurrentUser().getUserRoles());
		aUser.setAuthMode(authMode);
		aUser.setStatus(getCurrentUser().getStatus());
		aUser.setAttachedFile(getCurrentUser().getAvatar());
		return aUser;
	}

	public void switchLang(LanguageType lang) {
		this.lang = lang;
		for (_Session child : descendants) {
			child.switchLang(lang);
		}
	}

	public String getLang() {
		return lang.name();

	}

	public String getAppType() {
		return env.templateType;
	}

	public String getLocalizedWord(String word) {
		return env.vocabulary.getWord(word, lang.name());
	}

	public HttpSession getJses() {
		return jses;
	}

	@Override
	public _Page getPage(Page page, Map<String, String[]> formData)
			throws ClassNotFoundException, RuleException, WebFormValueException {
		String cid = page.getID() + "_";
		Object obj = getObject(cid);
		String c[] = formData.get("cache");
		if (c != null) {
			String cache = c[0];
			if (obj == null || cache.equalsIgnoreCase("reload")) {
				_Page buffer = page.getContent(formData);
				setObject(cid, buffer);
				return buffer;
			} else {
				return (_Page) obj;
			}
		} else {
			if (obj == null) {
				_Page buffer = page.getContent(formData);
				setObject(cid, buffer);
				return buffer;
			} else {
				return (_Page) obj;
			}
		}

	}

	@Override
	public void flush() {
		if (cache != null) {
			cache.clear();
		}
	}

	public _Session clone(AppTemplate env, HttpSession jses, String contextID) {
		_Session newSes = new _Session(env, jses, contextID, currentUser);
		newSes.authMode = AuthModeType.LOGIN_THROUGH_TOKEN;
		newSes.switchLang(lang);
		addDescendant(newSes);
		return newSes;
	}

	@Override
	public String toString() {
		return "userid=" + currentUser.getLogin() + ", lang=" + lang + ", authMode=" + authMode;
	}

	private void setObject(String name, _Page obj) {
		cache.put(name, obj);
	}

	private Object getObject(String name) {
		try {
			return cache.get(name);
		} catch (Exception e) {
			return null;
		}
	}

	private void addDescendant(_Session descendant) {
		this.descendants.add(descendant);
	}

}
