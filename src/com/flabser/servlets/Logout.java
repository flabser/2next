package com.flabser.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.activity.IActivity;
import com.flabser.exception.PortalException;
import com.flabser.server.Server;
import com.flabser.users.User;
import com.flabser.users.UserSession;

public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AppTemplate env;

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		env = (AppTemplate) context.getAttribute("portalenv");
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		UserSession userSession = null;

		String mode = request.getParameter("mode");
		if (mode == null) {
			mode = "leave_ses";
		}

		try {
			HttpSession jses = request.getSession(false);
			if (jses != null) {
				userSession = (UserSession) jses
						.getAttribute(UserSession.SESSION_ATTR);
				if (userSession != null) {
					User user = userSession.currentUser;
					String userID = user.getLogin();
					IActivity ua = DatabaseFactory.getSysDatabase()
							.getActivity();
					ua.postLogout(ServletUtil.getClientIpAddr(request), user);
					jses.removeAttribute(UserSession.SESSION_ATTR);
					Server.logger.normalLogEntry(userID + " logout");
				}
			}
			response.sendRedirect(getRedirect());
		} catch (Exception e) {
			new PortalException(e, env, response,
					ProviderExceptionType.LOGOUTERROR);
		}

	}

	private String getRedirect() {
		if (env != null) {
			return env.globalSetting.entryPoint;
		} else {
			return env.globalSetting.entryPoint
					+ "&reason=session_lost&autologin=0";
		}
	}

}
