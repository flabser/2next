package com.flabser.restful.data;

import java.sql.ResultSet;

public interface IAppEntity {
	void init(ResultSet rs);

	String getName();

	long getID();

	boolean isPermissionsStrict();
}
