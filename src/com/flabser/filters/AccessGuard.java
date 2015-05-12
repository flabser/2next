package com.flabser.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import com.flabser.appenv.AppEnv;


public class AccessGuard implements Filter {

	private ServletContext context;
	private AppEnv env;

	public void init(FilterConfig config) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse resp, FilterChain chain) {
		/*try {
			HttpServletRequest http = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) resp;
			context = http.getServletContext();
			env = (AppEnv) context.getAttribute("portalenv");

			Cookies appCookies = new Cookies(http);

			if (env.authType == AuthTypes.BASIC) {
				ISystemDatabase systemDatabase = DatabaseFactory.getSysDatabase();
				String[] userAndPwd = null;
				String authorization = http.getHeader("Authorization");
				if (authorization != null && authorization.startsWith("Basic")) {
					String encodedUserAndPwd = authorization.substring("Basic".length()).trim();
					String decodedUserAndPwd = new String(Base64.decodeBase64(encodedUserAndPwd),
							Charset.forName("UTF-8"));
					userAndPwd = decodedUserAndPwd.split(":", 2);
				} else {
					request.getRequestDispatcher("/Error?type=access_guard_error").forward(request, resp);
				}

				User user = new User(env);
				user = systemDatabase.checkUserHash(userAndPwd[0], userAndPwd[1], appCookies.authHash, user);
				if (user.authorized) {
					String userID = user.getUserID();

					boolean saveToken = true;

					HttpSession jses = http.getSession(true);
					UserSession userSession = new UserSession(user, http, httpResponse, saveToken, jses);

					AppEnv.logger.normalLogEntry(userID + " has connected");
					IUsersActivity ua = env.getDataBase().getUserActivity();
					ua.postLogin(userSession.browserType, user);

					UserApplicationProfile userAppProfile = user.enabledApps.get(env.appType);
					if (userAppProfile != null) {
						jses.setAttribute("usersession", userSession);
					} else {
						request.getRequestDispatcher("/Error?type=access_guard_error").forward(request, resp);
					}
				} else {
					request.getRequestDispatcher("/Error?type=access_guard_error").forward(request, resp);
				}
				chain.doFilter(request, resp);
			} else {
				String token = appCookies.wAuthHash;
				if (token.length() > 0) {
					LoggedUser logUser = WorkSpaceSession.getLoggeedUser(token);

					if (logUser != null) {
						User user = logUser.getUser();
						String cApp = env.appType.trim();

						if (user.enabledApps.containsKey(cApp) || cApp.equals("Workspace")
								|| cApp.equals("administrator")) {
							chain.doFilter(request, resp);
						} else {
							request.getRequestDispatcher("/Error?type=access_guard_error").forward(request, resp);
							AppEnv.logger.errorLogEntry("For user \"" + user.getUserID() + "\" application '" + cApp
									+ "' access denied");
						}
					} else {
						chain.doFilter(request, resp);
					}
				} else {
					chain.doFilter(request, resp);
				}
			}
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			chain.doFilter(request, resp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {

	}
}
