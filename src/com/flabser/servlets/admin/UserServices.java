package com.flabser.servlets.admin;

import java.util.*;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.exception.RuleException;
import com.flabser.localization.LocalizatorException;
import com.flabser.users.User;
import com.flabser.users.UserApplicationProfile;

public class UserServices {
	private ISystemDatabase sysDatabase;
	private int count;

	public UserServices(){
		sysDatabase = DatabaseFactory.getSysDatabase();
	}

	public String getUserAsXML(String userID) throws RuleException,  LocalizatorException{
		String xmlContent = "", ea = "";
		User user = sysDatabase.getUser(userID);


		for(UserApplicationProfile app: user.enabledApps.values()){
			ea += app.toXML();		
		}

		if (user.getUserID() != null){
			xmlContent  += "<userid>" + user.getUserID() + "</userid>" + 
					"<docid>" + user.docID + "</docid>" +
					"<fullname></fullname>" +		
					"<email>" + user.getEmail() + "</email>" +
								
					"<password>" + user.getPassword() +"</password>" +
					"<isadmin>" + user.isSupervisor() + "</isadmin>" +
					"<pk>" + user.getPublicKey() + "</pk>" + 		
					"<isadmin>" + user.isSupervisor() + "</isadmin>" +
					"<hash>" + user.getHash() + "</hash>" + 
					"<enabledapps>" + ea + "</enabledapps>";

		//	SourceSupplier ss = new SourceSupplier(user.getUserID());
		//	xmlContent += "<glossaries><apps>" + ss.getDataAsXML(ValueSourceType.MACRO, "", Macro.ALL_APPLICATIONS,"RUS") + "</apps></glossaries>";
		}


		return xmlContent;
	}

	public String getBlankUserAsXML() throws RuleException, LocalizatorException{
		String xmlContent = "";
	//	SourceSupplier ss = new SourceSupplier(Const.sysUser);
	//	xmlContent += "<glossaries><apps>" + ss.getDataAsXML(ValueSourceType.MACRO, "", Macro.ALL_APPLICATIONS,"RUS") + "</apps></glossaries>";
		return xmlContent;
	}




	boolean deleteUser(String id){	
		int docID = Integer.parseInt(id);
		return sysDatabase.deleteUser(docID);
	}

	public String getUserListWrapper(String keyWord, int pageNum, int pageSize) {
		String condition = "", xmlFragment = "";	
		if (keyWord != null){
			condition = "USERID LIKE '" + keyWord + "%'";
		}
		count = sysDatabase.getAllUsersCount(condition);
		ArrayList<User> fl = sysDatabase.getAllUsers(condition, sysDatabase.calcStartEntry(pageNum, pageSize), pageSize);		

		Iterator<User> it = fl.iterator();
		while (it.hasNext()) {
			User user = it.next();
			xmlFragment += "<entry docid=\"" + user.docID + "\" ><userid>" + user.getUserID() + "</userid>" +		
					"<isadministrator>" + user.isSupervisor() + "</isadministrator><email>" + user.getEmail() + "</email>" +
					"<jid>" + user.getInstMsgAddress() + "</jid><redirecturl></redirecturl>" +
					"</entry>";
		}

		return  xmlFragment;
	}

	public int getCount() {
		return count;
	}

	
}
