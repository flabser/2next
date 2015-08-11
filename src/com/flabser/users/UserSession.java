package com.flabser.users;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;

import org.omg.CORBA.UserException;

import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.env.EnvConst;
import com.flabser.exception.ApplicationException;
import com.flabser.exception.RuleException;
import com.flabser.exception.WebFormValueException;
import com.flabser.restful.Application;
import com.flabser.restful.AuthUser;
import com.flabser.runtimeobj.caching.ICache;
import com.flabser.runtimeobj.page.Page;
import com.flabser.script._Page;

public class UserSession implements ICache {

	public User currentUser;
	public HistoryEntryCollection history;
	public int pageSize;
	public String host = "localhost";

	private AuthModeType authMode;
	private HashMap<String, ActiveApplication> acitveApps = new HashMap<String, ActiveApplication>();

	private String lang;
	private HashMap<String, _Page> cache = new HashMap<String, _Page>();

	public UserSession(User user) {
		currentUser = user;
		authMode = AuthModeType.DIRECT_LOGIN;
		initHistory();
	}

	public void init(String appID) throws ApplicationException {
		ApplicationProfile appProfile = currentUser.getApplicationProfile(appID);
		if (appProfile != null) {
			if (appProfile.getStatus() == ApplicationStatusType.ON_LINE) {
				IDatabase db = appProfile.getDatabase();
				if (db != null) {
					ActiveApplication aa = new ActiveApplication(appProfile, db);
					acitveApps.put(appProfile.appType, aa);
					acitveApps.put(appProfile.appID, aa);
				} else {
					appProfile.setStatus(ApplicationStatusType.DEPLOING_FAILED);
					appProfile.save();
				}
			} else {
				throw new ApplicationException(appProfile.appType, "application \"" + appProfile.appType + "/" + appProfile.getAppID()
						+ "\" cannot init its database");
			}
		}
	}

	public void setLang(String lang) {
		if (!currentUser.getLogin().equals(User.ANONYMOUS_USER)) {
			currentUser.setPersistentValue("lang", lang);
		}
		this.lang = lang;
	}

	public String getLang() {
		if (currentUser.getLogin().equals(User.ANONYMOUS_USER)) {
			return lang;
		} else {
			Object o = currentUser.getPesistentValue("lang");
			if (o == null) {
				// TODO it uncomment when cookies will be realised
				// return lang;
				return "ENG";
			} else {
				return (String) o;
			}
		}
	}

	public void addHistoryEntry(String type, String url) throws UserException {
		HistoryEntry entry = new HistoryEntry(type, url);
		history.add(entry);
	}

	public boolean isBootstrapped(String appID) {
		ActiveApplication aa = acitveApps.get(appID);
		if (aa == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void flush() {
		if (cache != null) {
			cache.clear();
		}
	}

	public IDatabase getDataBase(String appType) {
		ActiveApplication aa = acitveApps.get(appType);
		if (aa == null) {
			return null;
		} else {
			return aa.db;
		}

	}

	public AuthModeType getAuthMode() {
		return authMode;
	}

	public void setAuthMode(AuthModeType authMode) {
		this.authMode = authMode;
	}

	private void initHistory() {
		history = new HistoryEntryCollection();
	}

	@Override
	public _Page getPage(Page page, Map<String, String[]> formData) throws ClassNotFoundException, RuleException, WebFormValueException {
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

	public AuthUser getUserPOJO() {
		AuthUser aUser = new AuthUser();
		aUser.setLogin(currentUser.getLogin());
		aUser.setName(currentUser.getUserName());
		aUser.setRoles(currentUser.getUserRoles().toArray(new String[currentUser.getUserRoles().size()]));
		HashMap<String, Application> applications = new HashMap<String, Application>();
		for (ApplicationProfile ap : (currentUser.getApplicationProfiles().values())) {
			if (!ap.appType.equalsIgnoreCase(EnvConst.WORKSPACE_APP_NAME)) {
				Application a = new Application(ap);
				a.setAppID(ap.appID);
				a.setAppName(ap.appName);
				a.setAppType(ap.appType);
				a.setOwner(ap.owner);
				a.setVisibilty(ap.getVisibilty());
				a.setStatus(ap.status);
				applications.put(ap.appID, a);
			}
		}
		aUser.setApplications(applications);
		aUser.setRoles(currentUser.getUserRoles());
		aUser.setAuthMode(authMode);
		aUser.setStatus(currentUser.getStatus());
		return aUser;
	}

	@Override
	public String toString() {
		return currentUser + ", authMode=" + authMode.name() + ", lang=" + lang;
	}

	public class HistoryEntryCollection {

		// type of collection has been changed from linked list to
		// LinkedBlockingDeque for better thread safe
		private LinkedBlockingDeque<HistoryEntry> history = new LinkedBlockingDeque<HistoryEntry>();
		private LinkedBlockingDeque<HistoryEntry> pageHistory = new LinkedBlockingDeque<HistoryEntry>();

		public void add(HistoryEntry entry) throws UserException {
			if (history.size() == 0 || (!history.getLast().equals(entry))) {
				history.add(entry);
				if (entry.isPageURL) {
					pageHistory.add(entry);
				}
			}

			if (history.size() > 10) {
				history.removeFirst();
				try {
					pageHistory.removeFirst();
				} catch (NoSuchElementException e) {

				}
			}

		}

		@Override
		public String toString() {
			String v = "";
			for (HistoryEntry entry : history) {
				v += entry.toString() + "\n";
			}
			return v;
		}

		public HistoryEntry getLastEntry() {
			try {
				return history.getLast();
			} catch (Exception e) {
				// return new HistoryEntry("view",
				// currentUser.getAppEnv().globalSetting.defaultRedirectURL,
				// "");
			}
			return null;
		}

	}

	class ActiveApplication {
		IDatabase db;
		ApplicationProfile appProfile;

		ActiveApplication(ApplicationProfile appProfile, IDatabase db) {
			this.appProfile = appProfile;
			this.db = db;

		}

	}

	public class HistoryEntry {

		public String URL;
		public String URLforXML;
		public String type;
		public Date time;
		public boolean isPageURL;

		HistoryEntry(String type, String url) {
			URL = url;
			URLforXML = url;
			this.type = type;
			time = new Date();
			isPageURL = isPage(url);
		}

		@Override
		public boolean equals(Object obj) {
			HistoryEntry entry = (HistoryEntry) obj;
			return entry.URLforXML.equalsIgnoreCase(URLforXML);
		}

		@Override
		public int hashCode() {
			return this.URLforXML.hashCode();
		}

		@Override
		public String toString() {
			return URLforXML;
		}

		private boolean isPage(String url) {
			return url.indexOf("type=page") > (-1);
		}
	}

}
