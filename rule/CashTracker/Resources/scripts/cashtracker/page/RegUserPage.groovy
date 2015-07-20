package cashtracker.page

import cashtracker.page.app.RegWebForm
import cashtracker.page.app.VerifyEMail

import com.flabser.script.*
import com.flabser.script.actions.*
import com.flabser.script.events.*
import com.flabser.scriptprocessor.*
import com.flabser.solutions.*
import com.flabser.users.*


class RegUserPage extends _DoScript {

	@Override
	public void doPost(_Session session, _WebFormData formData, String lang) {

		publishElement("process-action", "reg")

		RegWebForm regForm = new RegWebForm(formData)

		if (!regForm.isValid()) {
			if (regForm.errors.contains("required")) {
				publishElement("error", "required")
				return
			}
			if (regForm.errors.contains("email")) {
				publishElement("error", "email")
			}
			if (regForm.errors.contains("pwd-weak")) {
				publishElement("error", "pwd-weak")
			}
			return
		}

		def sdb = com.flabser.dataengine.DatabaseFactory.getSysDatabase()
		def userExists = sdb.getUser(regForm.email)
		if (userExists) {

			publishElement("error", "user-exists")
			return
		}

		// reg user
		com.flabser.users.User user = session.getUser()
		user.setLogin(regForm.email)
		user.setUserName(regForm.userName)
		user.setPwd(regForm.pwd)
		user.setPasswordHash(regForm.pwd)
		user.setEmail(regForm.email)
		user.setStatus(UserStatusType.NOT_VERIFIED)
		user.setRegDate(new Date())
		user.setVerifyCode(_Helper.randomValue)

		if (!user.save()) {
			publishElement("error", "save-error")
			return
		}

		publishElement("process", "user-reg")

		VerifyEMail sve = new VerifyEMail(session, user)
		if (sve.send()) {
			user.setStatus(UserStatusType.WAITING_FOR_VERIFYCODE)
			if (user.save()) {
				publishElement("process", "verify-email-send")
			}else {
				publishElement("error", "save-error")
			}
		} else {
			publishElement("error", "verify-email-sending-error")
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
