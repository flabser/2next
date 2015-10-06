package com.flabser.users;

import java.util.HashMap;
import java.util.Map;

import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.env.Environment;
import com.flabser.exception.ApplicationException;
import com.flabser.exception.RuleException;
import com.flabser.exception.WebFormValueException;
import com.flabser.restful.AppUser;
import com.flabser.restful.Application;
import com.flabser.runtimeobj.caching.ICache;
import com.flabser.runtimeobj.page.Page;
import com.flabser.script._Page;

public class UserSession implements ICache {

	public User currentUser;
	public int pageSize;

	private AuthModeType authMode;
	private HashMap<String, ActiveApplication> acitveApps = new HashMap<String, ActiveApplication>();

	private String lang;
	private HashMap<String, _Page> cache = new HashMap<String, _Page>();

	public UserSession(User user) {
		currentUser = user;
		authMode = AuthModeType.DIRECT_LOGIN;
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
				throw new ApplicationException(appProfile.appType, "application \"" + appProfile.appType + "/"
						+ appProfile.getAppID() + "\" cannot init its database");
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
				return "ENG";
			} else {
				return (String) o;
			}
		}
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

	public ActiveApplication getActiveApplication(String appType) {
		ActiveApplication aa = acitveApps.get(appType);
		return aa;
	}

	public AuthModeType getAuthMode() {
		return authMode;
	}

	public void setAuthMode(AuthModeType authMode) {
		this.authMode = authMode;
	}

	@Override
	public _Page getPage(Page page, Map<String, String[]> formData) throws ClassNotFoundException, RuleException,
	WebFormValueException {
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

	public AppUser getUserPOJO() {
		AppUser aUser = new AppUser();
		aUser.setLogin(currentUser.getLogin());
		aUser.setName(currentUser.getUserName());
		aUser.setEmail(currentUser.getEmail());
		aUser.setRoles(currentUser.getUserRoles());
		HashMap<String, Application> applications = new HashMap<String, Application>();
		for (ApplicationProfile ap : currentUser.getApplicationProfiles().values()) {
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
		aUser.setRoles(currentUser.getUserRoles());
		aUser.setAuthMode(authMode);
		aUser.setStatus(currentUser.getStatus());
		aUser.setAttachedFile(currentUser.getAvatar());
		return aUser;
	}

	@Override
	public String toString() {
		return currentUser + ", authMode=" + authMode.name() + ", lang=" + lang;
	}

	public class ActiveApplication {
		private IDatabase db;
		private ApplicationProfile appProfile;

		ActiveApplication(ApplicationProfile appProfile, IDatabase db) {
			this.appProfile = appProfile;
			this.db = db;

		}

		public IDatabase getDataBase() {
			return db;
		}

		public ApplicationProfile getParent() {
			return appProfile;

		}

	}

}
