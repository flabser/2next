package com.flabser.users;

import java.util.Date;

import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.server.Server;
import com.flabser.util.Util;

public class _UsersHelper {

	public static User regApplicationUser(ApplicationProfile ap, String login, String email, String userName, String pwd){
		try {
			User user = new User();
			user.setLogin(login);
			user.setUserName(userName);
			user.setPwd(pwd);
			user.setEmail(email);
			user.setRegDate(new Date());
			user.setVerifyCode("");
			user.addApplication(ap);

			user.save();
			return user;
		} catch (Exception e) {
			Server.logger.errorLogEntry(e);
		}
		return null;

	}

	public static User regTempApplicationUser(ApplicationProfile ap, String email) {
		User user = regApplicationUser(ap, email, email, "",
				Util.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890", 10));
		user.setStatus(UserStatusType.TEMPORARY);
		return user;

	}

}
