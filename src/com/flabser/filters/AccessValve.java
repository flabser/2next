package com.flabser.filters;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.env.Environment;
import com.flabser.env.SessionPool;
import com.flabser.server.Server;
import com.flabser.servlets.Cookies;
import com.flabser.users.ApplicationProfile;
import com.flabser.users.UserSession;

public class AccessValve extends ValveBase {

	@Override
	public void invoke(Request request, Response response) throws IOException, ServletException {
		try {
			HttpServletRequest http = request;
			String requestURI = http.getRequestURI();
			Server.logger.normalLogEntry(" valve " + http.getMethod() + " " + http.getRequestURI());
			if (requestURI.contains("session") || requestURI.contains("page") || requestURI.contains("Provider") || requestURI.contains("js")
					|| requestURI.contains("css") || requestURI.contains("SharedResources") || requestURI.contains("img")) {
				getNext().invoke(request, response);
			} else {

				RequestURL ru = new RequestURL(requestURI);
				String appType = ru.appName;
				String appID = ru.appID;

				HttpSession jses = http.getSession(false);
				if (jses != null) {
					UserSession us = (UserSession) jses.getAttribute(UserSession.SESSION_ATTR);
					if (us != null) {

						AppEnv env = Environment.getApplication(appType);
						if (env.appType.equals(AppEnv.ADMIN_APP_NAME)) {
							if (us.currentUser.isSupervisor()) {
								getNext().invoke(request, response);
							} else {
								HttpServletResponse httpResponse = response;
								httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
								Server.logger.warningLogEntry("User is not Administrator");
							}
						} else {
							if (us.isBootstrapped(appID)) {
								Server.logger.warningLogEntry("session alive ...");
								getNext().invoke(request, response);
							} else {
								HashMap<String, ApplicationProfile> hh = us.currentUser.getApplicationProfiles(env.appType);
								if (hh != null) {
									Server.logger.warningLogEntry("application initializing ...");
									us.init(appID);
									try {
										Context context = Server.webServerInst.addApplication(appID, env.appType);
										context.getServletContext().setAttribute(AppEnv.APP_ATTR, env);
									} catch (LifecycleException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									System.out.println("request again " + requestURI);
									((HttpServletResponse) response).sendRedirect(requestURI);
								} else {
									HttpServletResponse httpResponse = response;
									httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
									Server.logger.warningLogEntry("\"" + env.appType + "\" has not set");
								}
							}
						}
					} else {
						HttpServletResponse httpResponse = response;
						httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						Server.logger.warningLogEntry("user session was expired");
					}

				} else {
					Cookies appCookies = new Cookies(http);
					String token = appCookies.auth;
					if (token != null) {
						UserSession userSession = SessionPool.getLoggeedUser(token);
						if (userSession != null) {
							jses = http.getSession(true);
							jses.setAttribute(UserSession.SESSION_ATTR, userSession);
							Server.logger.verboseLogEntry("user session \"" + userSession.toString() + "\" got from session pool");
							invoke(request, response);
						} else {
							HttpServletResponse httpResponse = response;
							httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							Server.logger.warningLogEntry("There is no user session ");
						}
					} else {
						HttpServletResponse httpResponse = response;
						httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						Server.logger.warningLogEntry("User session was expired");
					}
				}

			}
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabasePoolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
