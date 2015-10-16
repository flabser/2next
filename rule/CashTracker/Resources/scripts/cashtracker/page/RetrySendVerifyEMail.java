package cashtracker.page;

import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.mail.message.VerifyEMail;
import com.flabser.script._Session;
import com.flabser.script._Validator;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;
import com.flabser.users.User;


public class RetrySendVerifyEMail extends _DoScript {

	@Override
	public void doPost(_Session session, _WebFormData formData, String lang) {

		String email = formData.getValueSilently("email");
		if (!_Validator.checkEmail(email)) {
			publishElement("error", "invalid-email");
			return;
		}

		publishElement("email", email);

		ISystemDatabase sdb = com.flabser.dataengine.DatabaseFactory.getSysDatabase();
		User user = sdb.getUser(email);
		if (user == null || !user.isValid) {
			publishElement("error", "no-user");
			return;
		} else if (user.getEnabledApps().size() > 0) {
			publishElement("process", "user-verified");
			return;
		}

		VerifyEMail ve = new VerifyEMail(session, user);
		if (ve.send()) {
			publishElement("process", "verify-email-send");
		} else {
			publishElement("error", "verify-email");
		}
	}

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doPut(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doDelete(_Session session, _WebFormData formData, String lang) {
	}
}
