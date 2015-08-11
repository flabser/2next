package com.flabser.script;

import java.sql.ResultSet;

public interface _IObject {
	 void init(ResultSet rs);
    long getId();
    String getTableName();
}
