package com.flabser.rule.page;

import java.util.ArrayList;

import com.flabser.appenv.AppEnv;
import com.flabser.rule.Caption;


public interface IElement {
	String getID();
	AppEnv getAppEnv();
	String getScriptDirPath();
	String getPrimaryScriptDirPath();
	ArrayList<Caption> getCaptions();

}
