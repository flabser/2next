package com.flabser.users;

import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.exception.WebFormValueException;
import com.flabser.util.Util;

import java.sql.SQLException;
import java.util.Date;

public class _UsersHelper {

    public static User regApplicationUser(ApplicationProfile ap, String login, String email, String userName) throws WebFormValueException, ClassNotFoundException, SQLException, InstantiationException, DatabasePoolException, IllegalAccessException {
        User user = new User();
        user.setLogin(login);
        user.setUserName(userName);
        user.setPassword(Util.generateRandomAsText("QWERTYUIOPASDFGHJKLMNBVCXZ1234567890_", 5));
        user.setPasswordHash("");
        user.setEmail(email);
        user.setStatus(UserStatusType.WAITING_FOR_FIRST_ENTERING);
        user.setRegDate(new Date());
        user.setVerifyCode("");
        user.addApplication(ap);

        user.save();
        return user;

    }


}
