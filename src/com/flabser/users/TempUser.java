package com.flabser.users;

import java.util.Random;

import com.flabser.dataengine.Const;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;

public class TempUser implements Const {

	private static final long serialVersionUID = 1L;
	private transient ISystemDatabase sysDatabase;
	private String userID;

	public TempUser(){
		this.sysDatabase = DatabaseFactory.getSysDatabase();
	}

	public TempUser(String userID){
		this.sysDatabase = DatabaseFactory.getSysDatabase();
		setUserID(userID);	
	}

	public String add(){
		String _userID = this.generateUserID();

		/*if(sysDatabase.insert(this) != 1){
			_userID = "";
		}*/

		return _userID;
	}

	public String getUserID() {
		return userID;
	}

	private void setUserID(String userID) {
		this.userID = userID;
	}

	public String getCurrentUserID(){
		return userID;
	}

	public String generateUserID(){

		String id = "user";
		Random rnd = new Random();

		for(int i=0;i<6;i++){
			id += rnd.nextInt(9);
		}

		return id;
	}

	public int save(){
		return sysDatabase.update(this);
	}

	public String toString(){
		return "userID=" + userID;
	}

	public String toXML(){
		return "<userid>" + userID + "</userid>";
	}
}
