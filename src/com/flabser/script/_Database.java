package com.flabser.script;


import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.flabser.dataengine.Const;
import com.flabser.dataengine.IDatabase;
import com.flabser.users.User;


public class _Database implements Const {

	IDatabase dataBase;

	private _Session session;
	
	
	private User user;

	public _Database(IDatabase db, String userID, _Session session) {
		this.session = session;
		dataBase = db;
		user = session.getUser();
	
	}

	public _Session getParent() {
		return session;
	}

	

	public IDatabase getBaseObject() {
		return dataBase;
	}
}
