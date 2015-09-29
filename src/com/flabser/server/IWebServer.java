package com.flabser.server;

import java.net.MalformedURLException;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;

import com.flabser.env.Site;

public interface IWebServer {
	void init(String defaultHostName) throws MalformedURLException, LifecycleException;

	Host addAppTemplate(Site site) throws LifecycleException, MalformedURLException;

	String initConnectors();

	void startContainer();

	void initDefaultURL();

	void stopContainer();

	Context addApplication(String appID, Site env) throws ServletException;

	Context initAdministartor();

}
