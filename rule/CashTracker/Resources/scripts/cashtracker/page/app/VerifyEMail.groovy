package cashtracker.page.app

import com.flabser.script.*
import com.flabser.scriptprocessor.*
import com.flabser.script.events.*
import com.flabser.script.actions.*
import com.flabser.users.*
import com.flabser.solutions.*
import com.flabser.dataengine.*

class VerifyEMail extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {

		String code = formData.getValueSilently("code")
		User user = DatabaseFactory.getSysDatabase().getUserByVerifyCode(code)
		if (user != null) {
			if (user.getStatus() == UserStatusType.WAITING_FOR_VERIFYCODE) {
				user.setStatus(UserStatusType.REGISTERED)
				if (user.save()) {
					publishElement("process", "verify-ok")
				}else {
					publishElement("error", "save-error")
				}
			}else{
				publishElement("process", "already-registered")
			}
		}else {
			publishElement("error", "user-not-found")
			return
		}
	}
}
