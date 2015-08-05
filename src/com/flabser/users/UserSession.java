package com.flabser.users;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;

import javax.servlet.http.HttpSession;

import org.omg.CORBA.UserException;

import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.exception.RuleException;
import com.flabser.exception.WebFormValueException;
import com.flabser.runtimeobj.caching.ICache;
import com.flabser.runtimeobj.page.Page;
import com.flabser.script._Page;

public class UserSession implements ICache {

	public final static String SESSION_ATTR = "usersession";

	public User currentUser;
	public HistoryEntryCollection history;
	public int pageSize;
	public String host = "localhost";

	private AuthModeType authMode;
	// private HttpSession jses;
	private HashMap<String, ActiveApplication> acitveApps = new HashMap<String, ActiveApplication>();

	private String lang;
	private HashMap<String, _Page> cache = new HashMap<String, _Page>();

	public UserSession(User user, String appID, HttpSession jses) throws UserException, ClassNotFoundException, InstantiationException, IllegalAccessException,
			DatabasePoolException {
		currentUser = user;
		// this.jses = jses;
		initHistory();
		ApplicationProfile appProfile = user.getApplicationProfile(appID);
		if (appProfile != null) {
			// dataBase = appProfile.getDatabase();
			acitveApps.put(appProfile.appType, new ActiveApplication(appProfile, appProfile.getDatabase()));
		}
	}

	public UserSession(User user, HttpSession jses) {
		currentUser = user;
		// this.jses = jses;
		initHistory();
	}

	public void init(String appID) {
		ApplicationProfile appProfile = currentUser.getApplicationProfile(appID);
		if (appProfile != null) {
			acitveApps.put(appProfile.appType, new ActiveApplication(appProfile, appProfile.getDatabase()));
			acitveApps.put(appProfile.appID, new ActiveApplication(appProfile, appProfile.getDatabase()));
		}
	}

	public void setLang(String lang) {
		if (!currentUser.getLogin().equals(User.ANONYMOUS_USER)) {
			currentUser.setPersistentValue("lang", lang);
		}
		// jses.setAttribute("lang", lang);
	}

	public String getLang() {
		if (currentUser.getLogin().equals(User.ANONYMOUS_USER)) {
			return lang;
		} else {
			Object o = currentUser.getPesistentValue("lang");
			if (o == null) {
				return lang;
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
