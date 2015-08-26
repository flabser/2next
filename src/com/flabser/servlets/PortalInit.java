package com.flabser.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.dataengine.IAppDatabaseInit;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.IDeployer;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
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
			String global = Environment.availableTemplates.get(app).global;
			template = new AppTemplate(app, global);
			if (template.appType.equals(EnvConst.WORKSPACE_APP_NAME)) {
				ApplicationProfile appProfile = new ApplicationProfile(template);
				IDatabase db = appProfile.getDatabase();
				IDeployer ad = db.getDeployer();
				try {
					Class<?> appDatabaseInitializerClass = Class.forName(appProfile.getDbInitializerClass());
					IAppDatabaseInit dbInitializer = (IAppDatabaseInit) appDatabaseInitializerClass.newInstance();
					dbInitializer.initApplication(appProfile.getPOJO());
					if (ad.deploy(dbInitializer) == 0) {
						appProfile.setStatus(ApplicationStatusType.ON_LINE);
						Environment.addCommonApp(new ApplicationProfile(template));
					} else {
						appProfile.setStatus(ApplicationStatusType.DEPLOING_FAILED);
					}
				} catch (Exception e) {
					Server.logger.errorLogEntry(e);
					appProfile.setStatus(ApplicationStatusType.DATABASE_NOT_CREATED);
				}

			}
			Environment.addAppTemplate(template);
			isValid = true;
		}

		if (isValid) {
			context.setAttribute(EnvConst.TEMPLATE_ATTR, template);
		}

	}
}
