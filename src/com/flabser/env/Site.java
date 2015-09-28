package com.flabser.env;

import org.apache.catalina.Host;
import org.apache.catalina.core.StandardHost;

public class Site {
	private String virtualHostName;
	private String appBase;
	private String global = "global.xml";
	private String parent;

	public String getVirtualHostName() {
		return virtualHostName;
	}

	public void setVirtualHostName(String virtualHostName) {
		this.virtualHostName = virtualHostName;
	}

	public String getAppBase() {
		return appBase;
	}

	public void setAppBase(String appBase) {
		this.appBase = appBase;
	}

	public String getGlobal() {
		return global;
	}

	public void setGlobal(String global) {
		this.global = global;
	}

	public void setParent(String parent) {
		this.parent = parent;

	}

	public String getParent() {
		return parent;

	}

	public Host getHost() {
		Host appHost = new StandardHost();
		appHost.setName(virtualHostName);
		return appHost;
	}

}
