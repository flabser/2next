package cashtracker.page

import com.flabser.dataengine.*
import com.flabser.script.*
import com.flabser.script.actions.*
import com.flabser.script.events.*
import com.flabser.scriptprocessor.*
import com.flabser.solutions.*
import com.flabser.users.*


class VerifyEMailPage extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {

		String code = formData.getValueSilently("code")
		User user = DatabaseFactory.getSysDatabase().getUserByVerifyCode(code)
		if (user != null) {
			if (user.getStatus() == UserStatusType.WAITING_FOR_VERIFYCODE) {
				user.setStatus(UserStatusType.REGISTERED)
				if (user.save()) {
					publishElement("process", "verify-ok")
					publishElement("email", user.email)
				} else {
					publishElement("error", "save-error")
				}
			} else {
				publishElement("process", "already-registered")
				publishElement("email", user.email)
			}
		} else {
			publishElement("error", "user-not-found")
			return
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
