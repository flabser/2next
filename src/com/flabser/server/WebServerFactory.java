package com.flabser.server;

public class WebServerFactory {

	public static IWebServer getServer(int ver){

		return new WebServer();


	}
}
