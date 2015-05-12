package com.flabser.dataengine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.flabser.users.User;

public interface Const {
	String[] supervisorGroup = {"[supervisor]"};
	List<String> supervisorGroupAsList = Arrays.asList(supervisorGroup);
	HashSet<String> supervisorGroupAsSet = new HashSet<String>(supervisorGroupAsList);
	@Deprecated
	String[] observerGroup = {"[observer]"};
	@Deprecated
	List<String> observerGroupAsList = Arrays.asList(observerGroup);
	@Deprecated
	HashSet<String> sysGroupAsSet = new HashSet<String>(observerGroupAsList);
	String sysUser = "supervisor";
	String DEFAULT_SORT_ORDER = "ASC";
	String DEFAULT_SORT_COLUMN = "VIEWDATE";
  //  public static final User supervisorUser = new User(sysUser);
	
	
	int UNKNOWN = 0;
	int TEXT = 1;
	int DATETIMES = 2;
	int NUMBERS = 3;
	int AUTHORS = 4;
	@Deprecated
	int TEXTLIST = 5;
	int READERS = 6;
	int FILES = 7;
	@Deprecated
	int GLOSSARY = 8;
	int DATE = 9;
	int COMPLEX_OBJECT = 10;
    int RICHTEXT = 11;
    int COORDINATION = 12;


	public int OUTLINE_RULE = 80;
	public int STATICCONTENT_RULE = 81;
	public int VIEW_RULE = 82;
	public int FILTER_RULE = 88;
	public int FORM_RULE = 83;
	public int QUERY_RULE = 84;
	public int PAGE_RULE = 85;
	public int HANDLER_RULE = 86;
	public int SYNC_RULE = 87;

	public int EDITMODE_NOACCESS = 140;
	public int EDITMODE_READONLY = 141;
	public int EDITMODE_EDIT = 142;	
	
	public int CATEGORY = 887;
	public int DOCTYPE_DEPARTMENT = 888;
	public int DOCTYPE_EMPLOYER = 889;
	public int DOCTYPE_UNKNOWN = 890;
	public int DOCTYPE_ORGANIZATION = 891;
//	public int DOCTYPE_ROLE = 892;
	public int DOCTYPE_PERSON = 893;
	public int DOCTYPE_GLOSSARY = 894;
	public int DOCTYPE_USERPROFILE = 895;
	public int DOCTYPE_MAIN = 896;
	public int DOCTYPE_TASK = 897;
	public int DOCTYPE_EXECUTION = 898;
	public int DOCTYPE_PROJECT = 899;
	public int DOCTYPE_GROUP = 900;
	public int DOCTYPE_REPORT = 901;
	public int DOCTYPE_ACTIVITY_ENTRY = 902;
	public int DOCTYPE_RECYCLE_BIN_ENTRY = 903;
	public int DOCTYPE_TOPIC = 904;
	public int DOCTYPE_POST = 905;
	
	int DATABASE_EMBEDED = 1300;
	
	int SCHEDULER_OFFLINE = 3000;
	int SCHEDULER_PERIODICAL = 3001;
	int SCHEDULER_INTIME = 3002;


	
	
}
