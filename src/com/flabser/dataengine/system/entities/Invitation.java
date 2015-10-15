package com.flabser.dataengine.system.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.dataengine.system.entities.constants.InvitationStatusType;


@JsonRootName("invitation")
public class Invitation {
	private int id;
	private Date regDate;
	private String email;

	private String message;

	private String appType;
	private String appID;
	private long author;

	private long tempLogin;
	private InvitationStatusType status = InvitationStatusType.UNKNOWN;
	//

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public long getAuthor() {
		return author;
	}

	public void setAuthor(long id2) {
		this.author = id2;
	}

	public long getTempLogin() {
		return tempLogin;
	}

	public void setTempLogin(long tempLogin) {
		this.tempLogin = tempLogin;
	}

	public InvitationStatusType getStatus() {
		return status;
	}

	public void setStatus(InvitationStatusType status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Invitation[" + email + ", " + message + "]";
	}

	public boolean save() {
		ISystemDatabase sysDatabase = DatabaseFactory.getSysDatabase();

		if (id == 0) {
			regDate = new Date();
			id = sysDatabase.insert(this);
		} else {
			id = sysDatabase.update(this);
		}

		if (id < 0) {
			return false;
		} else {
			return true;
		}
	}


}
