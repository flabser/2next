package com.flabser.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.apptemplate.WorkModeType;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.IApplicationDatabase;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
import com.flabser.env.Site;
import com.flabser.rule.GlobalSetting;
import com.flabser.server.Server;
import com.flabser.users.ApplicationStatusType;

public class PortalInit extends HttpServlet {

	private static final long serialVersionUID = -8913620140247217298L;
	private boolean isValid;

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		String app = context.getServletContextName();
		AppTemplate template = null;

		if (app.equalsIgnoreCase("Administrator")) {
			template = new AppTemplate(app);
			template.globalSetting = new GlobalSetting();
			template.globalSetting.entryPoint = "";
			isValid = true;
		} else {
			Site site = Environment.availableTemplates.get(app);
			if (site != null) {
				template = new AppTemplate(site);
				if (template.globalSetting.getWorkMode() == WorkModeType.COMMON) {
					ApplicationProfile appProfile = new ApplicationProfile(template);
					try {
						ISystemDatabase sysDatabase = DatabaseFactory.getSysDatabase();
						IApplicationDatabase appDb = sysDatabase.getApplicationDatabase();
						int res = appDb.createDatabase(appProfile.getDbName(), EnvConst.DB_USER);
						if (res == 0 || res == 1) {
							appProfile.getDatabase();
							try {
								appProfile.setStatus(ApplicationStatusType.ON_LINE);
							} catch (Exception e) {
								Server.logger.errorLogEntry(e);
								appProfile.setStatus(ApplicationStatusType.DATABASE_NOT_CREATED);
							}
						} else {
							Server.logger.errorLogEntry("database not created");
							appProfile.setStatus(ApplicationStatusType.DATABASE_NOT_CREATED);
						}
					} catch (Exception e) {
						Server.logger.errorLogEntry(e);
						appProfile.setStatus(ApplicationStatusType.DATABASE_NOT_CREATED);
					}

				}
				site.setAppTemlate(template);
				isValid = true;
			} else {
				Server.logger.errorLogEntry("description of \"" + app + "\" has not found in cfg.xml or the application is off");
			}
		}

		if (isValid) {
			context.setAttribute(EnvConst.TEMPLATE_ATTR, template);
		}

	}
}
