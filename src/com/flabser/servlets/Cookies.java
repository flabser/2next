package com.flabser.servlets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class Cookies {
	public String currentLang = "ENG";

	public Cookies(HttpServletRequest request){
		Cookie[] cooks = request.getCookies();
		if (cooks != null){
			for (int i = 0; i < cooks.length; i++){
				if (cooks[i].getName().equals("lang")){
					currentLang = cooks[i].getValue();
				}
			}
		}
	}



	public String toString(){
		return "lang=" + currentLang ;
	}

}
