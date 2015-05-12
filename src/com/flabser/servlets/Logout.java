package com.flabser.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.Const;
import com.flabser.env.Environment;
import com.flabser.exception.PortalException;
import com.flabser.users.UserSession;
import com.flabser.users.UserSession.HistoryEntry;

public class Logout extends HttpServlet implements Const {
	private static final long serialVersionUID = 1L;
	private AppEnv env;

	public void init (ServletConfig config)throws ServletException{
		ServletContext context = config.getServletContext();
		env = (AppEnv) context.getAttribute("portalenv");
	}

	protected void  doGet(HttpServletRequest request, HttpServletResponse response){
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		UserSession userSession = null;

		String mode = request.getParameter("mode");	
		if(mode == null) mode = "leave_ses";

		try{
			HttpSession jses = request.getSession(false);	
			if (jses != null){
				userSession = (UserSession)jses.getAttribute("usersession");			
				if (userSession != null){
		//			IUsersActivity ua = env.getDataBase().getUserActivity();
					String userID = userSession.currentUser.getUserID();
		//			ua.postLogout(userSession.currentUser);
					AppEnv.logger.normalLogEntry(userID + " logout");					
				}

				if (env != null){					
					String addParameters = "&autologin=0";
					if (mode != null && mode.equalsIgnoreCase("session_lost")){
						addParameters = "&reason=session_lost&autologin=0";
					}else{
						addParameters = "&reason=user_logout";				
						Cookie loginCook = new Cookie("auth","0");
						loginCook.setMaxAge(0);
						response.addCookie(loginCook);
			
						if (userSession != null){

							HistoryEntry entry = userSession.history.getLastEntry();
							Cookie ruCookie = new Cookie("ru",entry.URL);
							ruCookie.setMaxAge(99999);						
							response.addCookie(ruCookie);
						}

					}				
				}
				jses.invalidate();	
				jses = null;
			}
			response.sendRedirect(getRedirect());	
		}catch (Exception e) {			
			new PortalException(e,env, response, ProviderExceptionType.LOGOUTERROR);				
		}		

	}	

	private String getRedirect(){
		if (env != null){
			return env.globalSetting.entryPoint;			
		}else{
			return env.globalSetting.entryPoint + "&reason=session_lost&autologin=0";
		}		
	}

}
