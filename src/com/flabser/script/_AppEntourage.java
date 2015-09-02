package com.flabser.script;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
import com.flabser.rule.Lang;
import com.flabser.server.Server;
import com.flabser.users.User;

public class _AppEntourage {
	private AppTemplate env;
	private _Session ses;

	public _AppEntourage(_Session ses, AppTemplate env) {
		this.ses = ses;
		this.env = env;
	}

	public String getServerVersion() {
		return Server.serverVersion;
	}

	public String getBuildTime() {
		return Server.compilationTime;
	}

	public String getAppName() {
		return env.templateType;
	}

	public ArrayList<String[]> getAvailableLangs() throws _Exception {
		ArrayList<String[]> list = new ArrayList<String[]>();

		for (Lang l : env.globalSetting.langsList) {
			String[] p = { l.isOn.toString(), l.id, l.name };
			list.add(p);
		}
		return list;
	}

	public ArrayList<String[]> getAvailableApps() throws _Exception {
		User user = ses.getAppUser();
		ArrayList<String[]> list = new ArrayList<String[]>();

		for (ApplicationProfile app : user.getApplications()) {
			String p[] = { app.appName, app.owner, app.appID };
			list.add(p);
		}
		return list;
	}

	public Collection<AppTemplate> getAvailableTemplates() {
		@SuppressWarnings("unchecked")
		HashMap<String, AppTemplate> at = (HashMap<String, AppTemplate>) Environment.getAppTemplates().clone();
		at.remove(EnvConst.WORKSPACE_APP_NAME);
		return at.values();
	}

}
