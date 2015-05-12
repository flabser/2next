package com.flabser.dataengine.activity;


import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

import com.flabser.dataengine.IDatabase;
import com.flabser.servlets.BrowserType;
import com.flabser.users.User;

public interface IUsersActivity {
	IDatabase getParentDatabase();
	
	/*int postSomeActivity(String event, User user);
	int isRead(int docID, int docType, String userName);
	int isRead(Connection conn, int docID, int docType, String userName);

    int postRemoveFromFavorites(int docID, int docType, String userID, HashSet<String> groups);

    int postMarkRead(int docID, int docType, User user);
	int postMarkUnread(int docID, int docType, User user);*/

    int postAddToFavorites(int docID, int docType, String userID, HashSet<String> groups);

	void postLogout(User currentUser);

	void postLogin(BrowserType browserType, User user);

	int getAllActivityCount();

	Object getAllActivity(Object calcStartEntry, int pagesize);

/*	int postCompose(Document doc, User user);
	int postModify(Document oldDoc, Document modifiedDoc, User user);
	int postDelete(BaseDocument doc, User user);
	int postUndelete(BaseDocument doc, User user);
	int postLogin(BrowserType agent, User user);
    int postLogout(User user);
    int postCompletelyDelete(BaseDocument doc, User user);*/

   /* StringBuffer getUsersWhichRead(int docID, int docType);
    StringBuffer getUsersWhichRead(int docID, int docType, AppEnv env);
    int getAllActivityCount();
    StringBuffer getAllActivity(int offset, int pageSize);

	StringBuffer getActivity(int docID, int docType);
    StringBuffer getActivity(String userID);
    int getActivityCount(int activityType, String userID);
	Document getRecycleBinEntry(int docID, Set<String> complexUserID, String absoluteUserID);
	StringBuffer getActivity(String userID, int offset, int pageSize, int... typeCodes);
    StringBuffer getActivityByService(String serviceName, int offset, int pageSize, int... typeCodes);
	
	int getActivitiesCount(String userID, int...typeCodes);
	ViewEntryCollection getActivities(int offset, int pageSize, String userID, int[] typeCodes);*/
}
