package nubis.page;

import java.util.Date;

import nubis.page.app.RegWebForm;
import nubis.page.app.VerifyEMail;

import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.exception.WebFormValueException;
import com.flabser.script._Helper;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;
import com.flabser.users.User;
import com.flabser.users.UserStatusType;


public class SignUp extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doPost(_Session session, _WebFormData formData, String lang) throws WebFormValueException {

		publishElement("process-action", "reg");

		System.out.println(formData);

		RegWebForm regForm = new RegWebForm(formData);

		if (!regForm.isValid()) {
			if (regForm.getErrors().contains("required")) {
				publishElement("error", "required");
				return;
			}
			if (regForm.getErrors().contains("email")) {
				publishElement("error", "email");
			}
			if (regForm.getErrors().contains("pwd-weak")) {
				publishElement("error", "pwd-weak");
			}
			return;
		}

		ISystemDatabase sdb = com.flabser.dataengine.DatabaseFactory.getSysDatabase();
		User userExists = sdb.getUser(regForm.email);
		if (userExists != null) {
			publishElement("error", "user-exists");
			return;
		}

		com.flabser.users.User user = session.getUser();
		user.setLogin(regForm.email);
		user.setUserName(regForm.userName);
		user.setPwd(regForm.pwd);
		user.setPasswordHash(regForm.pwd);
		user.setEmail(regForm.email);
		user.setStatus(UserStatusType.NOT_VERIFIED);
		user.setRegDate(new Date());
		user.setVerifyCode(_Helper.getRandomValue());

		if (!user.save()) {
			publishElement("error", "save-error");
			return;
		}

		publishElement("process", "user-reg");

		VerifyEMail sve = new VerifyEMail(session, user);
		if (sve.send()) {
			user.setStatus(UserStatusType.WAITING_FOR_VERIFYCODE);
			if (user.save()) {
				publishElement("process", "verify-email-send");
			} else {
				publishElement("error", "save-error");
			}
		} else {
			publishElement("error", "verify-email-sending-error");
		}
	}

	@Override
	public void doPut(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doDelete(_Session session, _WebFormData formData, String lang) {
	}
}
