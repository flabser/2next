package com.flabser.server;

import java.net.MalformedURLException;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;

public interface IWebServer {
	void init(String defaultHostName) throws MalformedURLException, LifecycleException;

	Host addApplication(String siteName, String URLPath, String docBase) throws LifecycleException, MalformedURLException;

	String initConnectors();

	void startContainer();

	void initDefaultURL(Host host);

	void stopContainer();

	Context addApplication(String appID, String appType) throws LifecycleException, MalformedURLException;

	Context initAppEnv(String appType);

}
