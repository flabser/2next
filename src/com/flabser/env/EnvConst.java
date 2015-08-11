package com.flabser.env;

import java.io.File;

//TODO need to secure this class 
public class EnvConst {
	public static final String DEFAULT_XML_ENC = "utf-8";
	public static final String CFG_FILE = "cfg.xml";
	public static final String SUPPOSED_CODE_PAGE = "utf-8";
	public static final String ERROR_XSLT = "xsl" + File.separator + "errors" + File.separator + "error.xsl";
	public static final String CONN_URI = "jdbc:postgresql://localhost/2Next";
	public static final String DB_USER = "postgres";
	public static final String DB_PWD = "smartdoc";
	public static final String TEMPLATE_ATTR = "template";
	public final static String SESSION_ATTR = "usersession";
	public final static String ADMIN_APP_NAME = "Administrator";
	public final static String WORKSPACE_APP_NAME = "Nubis";
	public static final String AUTH_COOKIE_NAME = "2nses";
}
