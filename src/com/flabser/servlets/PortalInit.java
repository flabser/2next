package com.flabser.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.env.Environment;
import com.flabser.log.Log4jLogger;

public class PortalInit extends HttpServlet{ 

	private static final long serialVersionUID = -8913620140247217298L;
	private boolean isValid;

	public void init (ServletConfig config)throws ServletException{
		ServletContext context = config.getServletContext();
		String app = context.getServletContextName();
		AppEnv.logger = new Log4jLogger(app);
		AppEnv env = null;


		if (app.equalsIgnoreCase("Administrator")){
			try{
				env = new AppEnv(app);
				Environment.systemBase = new com.flabser.dataengine.system.SystemDatabase();	
				isValid = true;
			}catch(DatabasePoolException e) {
                AppEnv.logger.errorLogEntry(e);
				AppEnv.logger.fatalLogEntry("Server has not connected to system database");
			}catch(Exception e){
				AppEnv.logger.errorLogEntry(e);
			}
		}else{
			String global = Environment.webAppToStart.get(app).global;
			env = new AppEnv(app, global);		
			Environment.addApplication(env);
			isValid = true;
		}

		if(isValid)	context.setAttribute("portalenv", env);

	}
}
