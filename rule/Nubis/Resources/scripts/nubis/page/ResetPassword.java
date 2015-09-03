package nubis.page;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;
import com.flabser.users.User;
import com.flabser.users.UserStatusType;

public class ResetPassword extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {

		String code = formData.getValueSilently("email");
		User user = DatabaseFactory.getSysDatabase().getUser(code);
		if (user != null) {
			if (user.getStatus() == UserStatusType.REGISTERED) {

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
