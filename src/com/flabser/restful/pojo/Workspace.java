package com.flabser.restful.pojo;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("workspace")
public class Workspace {

	// private String url;
	// private List <Application> icons;

	@Override
	public String toString() {
		return "Workspace[]";
	}
}
