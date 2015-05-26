package com.flabser.servlets;

import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

public class ServletUtil {

	public static int getPage(HttpServletRequest request){
		int page = 0;
		try{
			page = Integer.parseInt(request.getParameter("page"));
		}catch(NumberFormatException nfe){
			page = 1;
		}
		return page;

	}


	public static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static HashMap<String, String[]> showParametersMap(HttpServletRequest request){
		HashMap<String, String[]> fields = (HashMap<String, String[]>) request.getParameterMap();

		Iterator<String> en = fields.keySet().iterator();

		while (en.hasNext()){
			String webFormFieldName = en.next();
			String[] val = (String[])fields.get(webFormFieldName);
			String v = "";
			for (int i = 0; i < val.length; i++){
				v += val[i] + "[" + Integer.toString(i) + "],";
			}
			System.out.println(webFormFieldName + "=" + v) ;
		}
		
     /*  for (String en : fields.keySet()) {
			String webFormFieldName = en;
			String[] val = fields.get(webFormFieldName);
			StringBuffer v = new StringBuffer(1000);
			for (int i = 0; i < val.length; i++) {
				v.append(val[i] + "[" + Integer.toString(i) + "],");
			}
			System.out.print(webFormFieldName + "=" + v.toString() + "\n") ;
		}*/
		System.out.println("PROVIDER : ---------------------------------------------");
		return fields;
	}
	
	 public static BrowserType getBrowserType(HttpServletRequest request) {
	        String userAgent = request.getHeader("user-agent");
	        if (userAgent == null || userAgent.length() == 0) {
	            return BrowserType.APPLICATION;
	        }
	        //Server.logger.verboseLogEntry("userAgent=" + userAgent);
	        if (userAgent.indexOf("MSIE") != -1) {
	            return BrowserType.IE;
	        } else if (userAgent.indexOf("Firefox") != -1 && userAgent.indexOf("Android") == -1) {
	            return BrowserType.FIREFOX;
	        } else if (userAgent.indexOf("Chrome") != -1 && userAgent.indexOf("Android") == -1) {
	            return BrowserType.CHROME;
	        } else if (userAgent.indexOf("Opera") != -1 && userAgent.indexOf("Android") == -1) {
	            return BrowserType.OPERA;
	        } else if (userAgent.indexOf("iPad") != -1) {
	            return BrowserType.IPAD_SAFARI;
	        } else if (userAgent.indexOf("iPhone") != -1) {
	            return BrowserType.IPAD_SAFARI;
	        } else if (userAgent.indexOf("Android") != -1) {
	            return BrowserType.ANDROID;
	        } else if (userAgent.indexOf("P1000") != -1) {
	            return BrowserType.GALAXY_TAB_SAFARI;
	        } else if (userAgent.indexOf("Safari") != -1) {
	            return BrowserType.SAFARI;
	        } else if (userAgent.indexOf("CFNetwork") != -1) {
	            return BrowserType.APPLICATION;
	        } else {
	            return BrowserType.UNKNOWN;
	        }
	    }
}
