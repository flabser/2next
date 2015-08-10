package com.flabser.exception;

import com.flabser.env.EnvConst;
import com.flabser.server.Server;

public class ExceptionXML {
	private String errorMessage;
	private int code;
	private String location;
	private String type;
	private String servletName;
	private String exception;

	public ExceptionXML(String errorMessage, int code, String location, String type, String servletName, String exception) {
		this.errorMessage = errorMessage;
		this.code = code;
		this.location = location;
		this.type = type;
		this.servletName = servletName;
		this.exception = exception;
	}

	public String toXML() {
		return "<?xml version = \"1.0\" encoding=\"" + EnvConst.xmlEnc + "\"?><error><message>" + errorMessage + "</message><code>" + code
				+ "</code><location>" + location + "</location><type>" + type + "</type><name>" + servletName + "</name><exception>"
				+ exception + "</exception><server>" + Server.serverTitle + "</server></error>";
	}
}
