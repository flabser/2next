package com.flabser.server;

import java.net.MalformedURLException;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;

import com.flabser.appenv.AppEnv;

public interface IWebServer {
	void init(String defaultHostName) throws MalformedURLException, LifecycleException;

	Host addAppTemplate(String siteName, String URLPath, String docBase) throws LifecycleException, MalformedURLException;

	String initConnectors();

	void startContainer();

	void initDefaultURL();

	void stopContainer();

	Context addApplication(String appID, AppEnv env);

	Context initAdministartor();

}
