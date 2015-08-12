package com.flabser.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
import com.flabser.log.Log4jLogger;
import com.flabser.rule.GlobalSetting;
import com.flabser.server.Server;

public class PortalInit extends HttpServlet {

	private static final long serialVersionUID = -8913620140247217298L;
	private boolean isValid;

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		String app = context.getServletContextName();
		Server.logger = new Log4jLogger("");
		AppTemplate env = null;

		if (app.equalsIgnoreCase("Administrator")) {
			try {
				env = new AppTemplate(app);
				env.globalSetting = new GlobalSetting();
				env.globalSetting.entryPoint = "";
				Environment.systemBase = new com.flabser.dataengine.system.SystemDatabase();
				isValid = true;
			} catch (DatabasePoolException e) {
				Server.logger.errorLogEntry(e);
				Server.logger.fatalLogEntry("server has not connected to system database");
			} catch (Exception e) {
				Server.logger.errorLogEntry(e);
			}
		} else {
			/*
			 * System.out.println("app=" + app); String appContextName =
			 * app.substring(0, app.indexOf("/"));
			 * System.out.println(appContextName); String global =
			 * Environment.webAppToStart.get(appContextName).global; env = new
			 * AppEnv(appContextName, global); Environment.addApplication(env);
			 * isValid = true;
			 */

			String global = Environment.webAppToStart.get(app).global;
			env = new AppTemplate(app, global);
			Environment.addAppTemplate(env);
			isValid = true;
		}

		if (isValid) {
			context.setAttribute(EnvConst.TEMPLATE_ATTR, env);
		}

	}
}
