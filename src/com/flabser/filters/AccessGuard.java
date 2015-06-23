package com.flabser.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class AccessGuard implements Filter {

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse resp, FilterChain chain) {
		/*	try {
			HttpServletRequest http = (HttpServletRequest) request;
			AppEnv.logger.errorLogEntry(" Filter '" + http.getQueryString() + "' method=" + http.getMethod());
			HttpServletResponse httpResponse = (HttpServletResponse) resp;
			context = http.getServletContext();
			env = (AppEnv) context.getAttribute("portalenv");

			HttpSession jses = http.getSession(true);
			UserSession us = (UserSession) jses.getAttribute("usersession");
			if (us != null) {
				chain.doFilter(request, resp);
			}else {
				httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				AppEnv.logger.errorLogEntry(" Application '" + env.appType + "' access denied");
			}

		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
	}

	@Override
	public void destroy() {

	}


}
