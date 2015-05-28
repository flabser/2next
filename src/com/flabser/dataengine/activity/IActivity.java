package com.flabser.dataengine.activity;

import com.flabser.servlets.BrowserType;
import com.flabser.users.User;

public interface IActivity {

	int postLogin(BrowserType agent, User user);
    int postLogout(User user);
   
}
