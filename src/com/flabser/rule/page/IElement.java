package com.flabser.rule.page;

import java.util.ArrayList;

import com.flabser.rule.Caption;

public interface IElement {
	String getID();

	String getScriptDirPath();

	String getPrimaryScriptDirPath();

	ArrayList<Caption> getCaptions();

}
