package cashtracker.page

import cashtracker.page.app.RegWebForm
import cashtracker.page.app.VerifyEMail

import com.flabser.script._Session
import com.flabser.script._WebFormData
import com.flabser.script.events._DoScript
import com.flabser.users.User


class RetrySendVerifyEMailPage extends _DoScript {

	@Override
	public void doPost(_Session session, _WebFormData formData, String lang) {
		RegWebForm regForm = new RegWebForm(formData)

		if (!regForm.isValid()) {
			if (regForm.errors.contains("email")) {
				publishElement("error", "invalid-email")
				return
			}
		}

		publishElement("email", regForm.email)

		def sdb = com.flabser.dataengine.DatabaseFactory.getSysDatabase()
		User user = sdb.getUser(regForm.email)
		if (user == null || !user.isValid) {
			publishElement("error", "no-user")
			return
		} else if (user.enabledApps.size() > 0) {
			publishElement("error", "user-verified")
			return
		}

		//
		VerifyEMail sve = new VerifyEMail(session, user)
		if (sve.send()) {
			publishElement("process", "verify-email-send")
		} else {
			publishElement("error", "verify-email")
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
