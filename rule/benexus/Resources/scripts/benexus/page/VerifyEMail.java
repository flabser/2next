package benexus.page;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;
import com.flabser.servlets.SessionCooks;
import com.flabser.users.User;
import com.flabser.users.UserStatusType;


public class VerifyEMail extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang, SessionCooks cooks) {

		String code = formData.getValueSilently("code");
		User user = DatabaseFactory.getSysDatabase().getUserByVerifyCode(code);
		if (user != null) {
			if (user.getStatus() == UserStatusType.WAITING_FOR_VERIFYCODE
					|| user.getStatus() == UserStatusType.NOT_VERIFIED) {
				user.setStatus(UserStatusType.REGISTERED);
				if (user.save()) {
					publishElement("process", "verify-ok");
					publishElement("email", user.getEmail());
				} else {
					publishElement("error", "save-error");
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
	public void doPost(_Session session, _WebFormData formData, String lang, SessionCooks cooks) {
	}

	@Override
	public void doPut(_Session session, _WebFormData formData, String lang, SessionCooks cooks) {
	}

	@Override
	public void doDelete(_Session session, _WebFormData formData, String lang, SessionCooks cooks) {
	}
}
