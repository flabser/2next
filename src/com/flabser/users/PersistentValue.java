package com.flabser.users;

import java.util.Date;

public class PersistentValue {
	public String key;
	public Object value;
	public Date postedTime;

	public PersistentValue(String k, String v) {
		key = k;
		value = v;
		postedTime = new Date();
		// TODO here need to write a saving the data to some persist storage
	}

}
