package com.flabser.server;

import java.util.ArrayList;

import javax.servlet.ServletException;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.env.Environment;
import com.flabser.env.Site;

public class ContexLoader  implements Runnable {

	@Override
	public void run() {
		ISystemDatabase sysDb = DatabaseFactory.getSysDatabase();
		for (Site site:Environment.availableTemplates.values()){
			ArrayList<ApplicationProfile> apps = sysDb.getAllApps("apptype='" + site.getAppBase() + "'", 0, 0);
			for (ApplicationProfile ap : apps){
				try {
					Server.webServerInst.addApplication(ap.getAppID(), site);
				} catch (ServletException e) {
					Server.logger.errorLogEntry(e);
				}
			}
		}
		Server.logger.infoLogEntry("users contexts are ready");

	}

}
