package com.flabser.servlets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class Cookies {
	public boolean wAuthCookiesIsValid;
	public boolean authCookiesIsValid;
	public String currentLang = "ENG";
	public String authHash = "0";
	public int pageSize = 20;
	public String redirectURL;
	public boolean redirectURLIsValid;
	
	public Cookies(HttpServletRequest request){
		Cookie[] cooks = request.getCookies();
		if (cooks != null){			
			for (int i = 0; i < cooks.length; i++){
				if (cooks[i].getName().equals("lang")){
					currentLang = cooks[i].getValue();	
				}else if (cooks[i].getName().equals("auth")){
					authHash = cooks[i].getValue();
					authCookiesIsValid = true;
				}else if (cooks[i].getName().equals("ru")){
					redirectURL = cooks[i].getValue();
					redirectURLIsValid = true;
				}else if (cooks[i].getName().equals("pagesize")){	
					try{
						pageSize = Integer.parseInt(cooks[i].getValue());
					}catch(NumberFormatException nfe){
						pageSize = 20;
					}
				}
			}
		}
	}
	

	
	public String toString(){
		return "auth=" + authHash + ", " + currentLang + "," + pageSize;
	}
	
}
