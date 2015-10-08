package nubis.page;

import nubis.page.app.ResetPasswordEMail;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.exception.WebFormValueException;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;
import com.flabser.users.User;
import com.flabser.users.UserStatusType;
import com.flabser.util.Util;

public class ResetPassword extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {

		String code = formData.getValueSilently("email");
		User user = DatabaseFactory.getSysDatabase().getUser(code);
		if (user != null) {
			if (user.getStatus() == UserStatusType.REGISTERED) {
				try {
					user.setPwd(Util.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890", 10));
					if (user.save()){
						ResetPasswordEMail sve = new ResetPasswordEMail(session, user);
						if (sve.send()) {
							user.setStatus(UserStatusType.WAITING_FIRST_ENTERING_AFTER_RESET_PASSWORD);
							if (!user.save()) {
								publishElement("error", "unknown-status");
							}
						} else {
							user.setStatus(UserStatusType.RESET_PASSWORD_NOT_SENT);
							user.save();
							publishElement("error", "unknown-status");
						}
					}
				} catch (WebFormValueException e) {
					publishElement("error", "unknown-status");
				}
			} else if (user.getStatus() == UserStatusType.REGISTERED) {
				publishElement("process", "already-registered");
				publishElement("email", user.getEmail());
			} else {
				publishElement("error", "unknown-status");
			}
		} else {
			publishElement("error", "user-not-found");
			return;
		}
	}

	@Override
	public void doPost(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doPut(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doDelete(_Session session, _WebFormData formData, String lang) {
	}
}
