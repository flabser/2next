package cashtracker.page.app

import com.flabser.script.*
import com.flabser.script.actions.*
import com.flabser.script.events.*
import com.flabser.scriptprocessor.*
import com.flabser.solutions.*
import com.flabser.users.*


class RegUser extends _DoScript {

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

		User userEmail = sdb.getUserByEmail(regForm.email)
		if (userEmail) {
			publishElement("error", "user-exists")
			return
		}

		// reg user
		com.flabser.users.User user = session.getUser()
		user.setLogin(regForm.email)
		user.setUserName(regForm.userName)
		user.setPassword(regForm.pwd)
		user.setPasswordHash(regForm.pwd)
		user.setEmail(regForm.email)
		user.setStatus(UserStatusType.NOT_VERIFIED)
		user.setRegDate(new Date())
		user.setVerifyCode(_Helper.randomValue)

		/*def appName = session.getGlobalSettings().appName;
		 ApplicationProfile ap = new ApplicationProfile()
		 ap.appName = appName
		 ap.owner = (user.getUserID().replace("@","_").replace(".","_")).replace("-","_").toLowerCase()
		 ap.dbName = appName.toLowerCase() + "_" + ap.owner
		 ap.dbLogin = ap.owner
		 ap.dbPwd = regForm.pwd
		 user.addApplication(ap)*/

		if (!user.save()) {
			publishElement("error", "save-error")
			return
		}

		publishElement("process", "user-reg")

		SendVerifyEMail sve = new SendVerifyEMail(session, user)
		if (sve.sendResult) {
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
