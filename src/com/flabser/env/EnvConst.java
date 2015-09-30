package com.flabser.env;

import java.io.File;

//TODO need to secure this class
public class EnvConst {
	public static final int DEFAULT_HTTP_PORT = 38779;
	public static final String DEFAULT_XML_ENC = "utf-8";
	public static final String CFG_FILE = "cfg.xml";
	public static final String VOCABULARY_FILE = "vocabulary.xml";
	public static final String SUPPOSED_CODE_PAGE = "utf-8";
	public static final String ERROR_XSLT = "xsl" + File.separator + "errors" + File.separator + "error.xsl";
	public static final String DATABASE_HOST = "localhost";
	public static final String DATABASE_NAME = "2Next";
	public static final String CONN_PORT = "5433";
	public static final String CONN_URI = "jdbc:postgresql://" + DATABASE_HOST + ":" + CONN_PORT + "/" + DATABASE_NAME;
	public static final String DB_USER = "postgres";
	public static final String DB_PWD = "smartdoc";
	public static final String TEMPLATE_ATTR = "template";
	public final static String SESSION_ATTR = "usersession";
	public final static String SHARED_RESOURCES_NAME = "SharedResources";
	public final static String ADMIN_APP_NAME = "Administrator";
	public static final String AUTH_COOKIE_NAME = "2nses";
	public static final String LANG_COOKIE_NAME = "lang";
	public static final String DEFAULT_PAGE = "about";



}
