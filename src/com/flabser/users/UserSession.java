package com.flabser.users;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.omg.CORBA.UserException;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.Const;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.env.Environment;
import com.flabser.exception.RuleException;
import com.flabser.runtimeobj.caching.ICache;
import com.flabser.runtimeobj.page.Page;
import com.flabser.script.concurrency._AJAXHandler;
import com.flabser.scriptprocessor.page.IAsyncScript;
import com.flabser.servlets.BrowserType;
import com.flabser.servlets.Cookies;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

public class UserSession implements Const, ICache {
	public User currentUser;
	public HistoryEntryCollection history;
	public String lang, skin = "";
	public int pageSize;
	public String host = "localhost";
	private IDatabase dataBase;

	private HttpSession jses;
	public BrowserType browserType;
	private Cookies appCookies;

	public UserSession(User user, String implemantion, String appID)
			throws UserException, ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			DatabasePoolException {
		currentUser = user;
		currentUser.setSession(this);
		if (!currentUser.getUserID().equalsIgnoreCase(Const.sysUser)) {
			initHistory();
		}
		Class cls = Class.forName(implemantion);
		dataBase = (IDatabase) cls.newInstance();
		UserApplicationProfile app = user.enabledApps.get(appID);
		dataBase.init(app.dbURL, user.getUserID(), app.dbPwd);
	}

	public UserSession(User user) throws UserException {
		currentUser = user;
		currentUser.setSession(this);
		if (!currentUser.getUserID().equalsIgnoreCase(Const.sysUser)) {
			initHistory();
		}

	}

	public UserSession(ServletContext context, HttpServletRequest request,	HttpServletResponse response, HttpSession jses)	throws AuthFailedException, UserException {
		this.jses = jses;
		appCookies = new Cookies(request);
		lang = appCookies.currentLang;
		skin = appCookies.currentSkin;
		pageSize = appCookies.pageSize;

		AppEnv env = (AppEnv) context.getAttribute("portalenv");
		ISystemDatabase systemDatabase = DatabaseFactory.getSysDatabase();

		int userHash = Integer.parseInt(appCookies.authHash);
		if (userHash == 0) {
			AppEnv.logger.normalLogEntry("Authorization failed, login or password is incorrect/");
			throw new AuthFailedException(AuthFailedExceptionType.NO_USER_SESSION, "");
		} else {
			currentUser = new User();
			UserApplicationProfile userAppProfile = currentUser.enabledApps.get(env.appType);			
			initHistory();
		}

		currentUser.setSession(this);
	}

	public void setObject(String name, StringBuffer obj) {
		HashMap<String, StringBuffer> cache = null;
		if (jses != null) {
			cache = (HashMap<String, StringBuffer>) jses.getAttribute("cache");
		}
		if (cache == null) {
			cache = new HashMap<>();
		}
		cache.put(name, obj);
		if (jses != null)
			jses.setAttribute("cache", cache);

	}

	public Object getObject(String name) {
		try {
			HashMap<String, StringBuffer> cache = (HashMap<String, StringBuffer>) jses
					.getAttribute("cache");
			return cache.get(name);
		} catch (Exception e) {
			return null;
		}
	}

	public void setLang(String lang, HttpServletResponse response) {
		this.lang = lang;
		Cookie cpCookie = new Cookie("lang", lang);
		cpCookie.setMaxAge(99999);
		cpCookie.setPath("/");
		response.addCookie(cpCookie);
	}

	public void setPageSize(String size, HttpServletResponse response) {
		try {
			pageSize = Integer.parseInt(size);
		} catch (NumberFormatException e) {
			pageSize = 30;
			size = "30";
		}

		Cookie cpCookie = new Cookie("pagesize", size);
		cpCookie.setMaxAge(999991);
		response.addCookie(cpCookie);
	}

	public void addHistoryEntry(String type, String url, String title)
			throws UserException {
		HistoryEntry entry = new HistoryEntry(type, url, title);
		history.add(entry);
	}

	public class HistoryEntryCollection {
		// type of collection has been changed from linked list to
		// LinkedBlockingDeque for better thread safe
		private LinkedBlockingDeque<HistoryEntry> history = new LinkedBlockingDeque<HistoryEntry>();
		private LinkedBlockingDeque<HistoryEntry> pageHistory = new LinkedBlockingDeque<HistoryEntry>();

		public void add(HistoryEntry entry) throws UserException {
			if (history.size() == 0 || (!history.getLast().equals(entry))) {
				history.add(entry);
				if (entry.isPageURL)
					pageHistory.add(entry);
			}

			if (history.size() > 10) {
				history.removeFirst();
				try {
					pageHistory.removeFirst();
				} catch (NoSuchElementException e) {

				}
			}

		}

		public String toString() {
			String v = "";
			for (HistoryEntry entry : history) {
				v += entry.toString() + "\n";
			}
			return v;
		}

		public Object getEntries() {
			// TODO Auto-generated method stub
			return null;
		}

		public HistoryEntry getLastEntry() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	public class HistoryEntry {
		public String URL;
		public String URLforXML;
		public String title;
		public String type;
		public Date time;
		public boolean isPageURL;

		HistoryEntry(String type, String url, String title) {
			URL = url;
			URLforXML = url;
			this.title = title;
			this.type = type;
			time = new Date();
			isPageURL = isPage(url);
		}

		public boolean equals(Object obj) {
			HistoryEntry entry = (HistoryEntry) obj;
			return entry.URLforXML.equalsIgnoreCase(URLforXML);
		}

		public int hashCode() {
			return this.URLforXML.hashCode();
		}

		public String toString() {
			return URLforXML;
		}

		private boolean isPage(String url) {
			return url.indexOf("type=page") > (-1);
		}
	}

	private void initHistory() throws UserException {
		history = new HistoryEntryCollection();
	}

	@Override
	public StringBuffer getPage(Page page, Map<String, String[]> formData)
			throws ClassNotFoundException, RuleException {
		String cid = page.getID() + "_";
		Object obj = getObject(cid);
		String c[] = formData.get("cache");
		if (c != null) {
			String cache = c[0];
			if (obj == null || cache.equalsIgnoreCase("reload")) {
				StringBuffer buffer = page.getContent(formData);
				setObject(cid, buffer);
				return buffer;
			} else {
				return (StringBuffer) obj;
			}
		} else {
			if (obj == null) {
				StringBuffer buffer = page.getContent(formData);
				setObject(cid, buffer);
				return buffer;
			} else {
				return (StringBuffer) obj;
			}
		}

	}

	@Override
	public void flush() {
		HashMap<String, StringBuffer> cache = (HashMap<String, StringBuffer>) jses
				.getAttribute("cache");
		if (cache != null)
			cache.clear();
	}

	public void addDynmaicClass(String id, _AJAXHandler instance) {
		// TODO Auto-generated method stub

	}

	public IAsyncScript getDynmaicClass(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public IDatabase getDataBase() {
		return dataBase;
	}

}
