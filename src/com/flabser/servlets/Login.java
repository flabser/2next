package com.flabser.servlets;

import org.apache.catalina.realm.RealmBase;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.Const;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.activity.IActivity;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.exception.PortalException;
import com.flabser.users.AuthFailedException;
import com.flabser.users.AuthFailedExceptionType;
import com.flabser.users.User;
import com.flabser.users.UserSession;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


public class Login extends HttpServlet implements Const {
	private static final long serialVersionUID = 1L;
	private AppEnv env;

	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		env = (AppEnv) context.getAttribute("portalenv");
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		UserSession userSession = null;
		try {
			String login = request.getParameter("login");
			String pwd = request.getParameter("pwd");
		//	String noAuth = request.getParameter("noauth");
			String noHash = request.getParameter("nohash");
			HttpSession jses;
			
			ISystemDatabase systemDatabase = DatabaseFactory.getSysDatabase();

			if (env == null || env.appType.equalsIgnoreCase("administrator")) {
				HashMap<String, User> admins = systemDatabase.getAllAdministrators();
				if (admins.size() > 0) {
					User admin = admins.get(login);
					if (admin != null) {
						if (admin.getPasswordHash() != null) {
							if (!admin.getPasswordHash().equals("")) {
								RealmBase rb = null;
								if (admin != null && admin.getPasswordHash().equals(rb.Digest(pwd, "MD5", "UTF-8"))) {
									jses = request.getSession(true);
									jses.setAttribute("adminLoggedIn", true);
									response.sendRedirect("Provider?type=view&element=users");
								} else {
									AppEnv.logger.warningLogEntry("Authorization failed, login or password is incorrect *");
									throw new AuthFailedException(AuthFailedExceptionType.PASSWORD_INCORRECT,login);
								}
							} else {
								if (admin.getPassword().equals(pwd)) {
									jses = request.getSession(true);
									jses.setAttribute("adminLoggedIn", true);
									response.sendRedirect("Provider?type=view&element=users");
								} else {
									AppEnv.logger.warningLogEntry("Authorization failed, login or password is incorrect *");
									throw new AuthFailedException(AuthFailedExceptionType.PASSWORD_INCORRECT,login);
								}
							}

						} else {
							if (admin.getPassword().equals(pwd)) {
								jses = request.getSession(true);
								jses.setAttribute("adminLoggedIn", true);
								response.sendRedirect("Provider?type=view&element=users");
							} else {
								AppEnv.logger
										.warningLogEntry("Authorization failed, login or password is incorrect *");
								throw new AuthFailedException(
										AuthFailedExceptionType.PASSWORD_INCORRECT,
										login);
							}
						}
					} else {
						AppEnv.logger.warningLogEntry("Authorization failed, login or password is incorrect *");
						throw new AuthFailedException(AuthFailedExceptionType.PASSWORD_INCORRECT,login);
					}
				} else {
					if (login.equals("admin") && pwd.equals("G2xQoZp4eLK@")) {
						jses = request.getSession(true);
						jses.setAttribute("adminLoggedIn", true);
						userSession = new UserSession(new User());
						jses.setAttribute("usersession", userSession);
						response.sendRedirect("Provider?type=view&element=users");
					} else {
						AppEnv.logger.warningLogEntry("Authorization failed, special login or password is incorrect");
						throw new AuthFailedException(AuthFailedExceptionType.PASSWORD_INCORRECT,login);
					}
				}
			
			} else {
				User user = new User();
				Cookies appCookies = new Cookies(request);
				if (noHash != null) {
					user = systemDatabase.checkUser(login, pwd, user);
				} else {
					user = systemDatabase.checkUserHash(login, pwd,
							appCookies.authHash, user);
				}
				if (!user.isAuthorized)
					throw new AuthFailedException(
							AuthFailedExceptionType.PASSWORD_INCORRECT, login);
				
				String userID = user.getUserID();
				jses = request.getSession(true);
				userSession = new UserSession(user, env.globalSetting.implementation, env.appType);

				AppEnv.logger.normalLogEntry(userID + " has connected");
				IActivity ua = DatabaseFactory.getSysDatabase().getActivity();
				ua.postLogin(ServletUtil.getClientIpAddr(request), user);

				String redirect = "";

				jses.setAttribute("usersession", userSession);
			
				redirect = getRedirect(jses, appCookies);
				response.sendRedirect(redirect);

			}
		} catch (AuthFailedException e) {
			try {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);			
				request.getRequestDispatcher("/Error?type=ws_auth_error")
						.forward(request, response);
			} catch (IOException e1) {
				new PortalException(e, env, response,ProviderExceptionType.INTERNAL, PublishAsType.HTML);
			} catch (ServletException e2) {
				new PortalException(e, env, response,ProviderExceptionType.INTERNAL, PublishAsType.HTML);
			}
		} catch (IOException ioe) {
			new PortalException(ioe, env, response, PublishAsType.HTML);
		} catch (IllegalStateException ise) {
			new PortalException(ise, env, response, PublishAsType.HTML);
		} catch (Exception e) {
			new PortalException(e, response, ProviderExceptionType.INTERNAL,
					PublishAsType.HTML);
		}
	}

	public String getMD5Hash(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			byte[] passBytes = password.getBytes(Charset.forName("UTF-8"));
			md.reset();
			byte[] digested = md.digest(passBytes);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < digested.length; i++) {
				sb.append(Integer.toHexString(0xff & digested[i]));
			}

			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}

	private String getRedirect(HttpSession jses, Cookies appCookies){
		String callingPage = (String)jses.getAttribute("callingPage");
		if( callingPage != null && (!callingPage.equalsIgnoreCase(""))){
			jses.removeAttribute("callingPage");
			return callingPage;
		}else{
			return env.globalSetting.defaultRedirectURL;		
		}
	}

	public class CallingPageCookie {
		public String page = "";

		public CallingPageCookie(HttpServletRequest request) {
			Cookie[] cooks = request.getCookies();
			if (cooks != null) {
				for (int i = 0; i < cooks.length; i++) {
					if (cooks[i].getName().equals("calling_page")) {
						page = cooks[i].getValue();
					}
				}
			}
		}
	}

}
