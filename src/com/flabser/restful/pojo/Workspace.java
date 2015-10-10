package com.flabser.restful.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName("workspace")
public class Workspace{


	private List <Icon> icons;


	@Override
	public String toString() {
		return "Workspace[]";
	}
}
