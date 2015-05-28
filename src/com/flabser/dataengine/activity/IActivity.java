package com.flabser.dataengine.activity;

import com.flabser.users.User;

public interface IActivity {

	int postLogin(String ip, User user);
    int postLogout(User user);
   
}
