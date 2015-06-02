package cashtracker.page.app

import com.flabser.script.*
import com.flabser.scriptprocessor.*
import com.flabser.script.events.*
import com.flabser.script.actions.*
import com.flabser.users.*
import com.flabser.solutions.*


class RegUser extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {

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

		boolean userExists = sdb.getUser(regForm.email).isValid
		if (userExists) {
			publishElement("error", "user-exists")
			return
		}

		// reg user
		com.flabser.users.User user = session.getUser()
		user.setUserID(regForm.email)
		user.setUserName(regForm.userName)
		user.setPassword(regForm.pwd)
		user.setPasswordHash(regForm.pwd)
		user.setEmail(regForm.email)
		user.setStatus(UserStatusType.NOT_VERIFIED)
		
		def appName = session.getGlobalSettings().appName;
		ApplicationProfile ap = new ApplicationProfile()
		ap.appName = appName
		ap.owner = (user.getUserID().replace("@","_").replace(".","_")).toLowerCase()
		ap.dbName = appName.toLowerCase() + "_" + ap.owner
		ap.dbLogin = ap.owner
		ap.dbPwd = regForm.pwd
		
		user.addApplication(ap)

		if (!user.save()) {
			publishElement("error", "save-error")
			return
		}

		publishElement("process", "user-reg")

		// create verify doc
	/*	VerifyDoc vd = VerifyDoc.createVerifyDoc(session, regForm)
		if (vd != null) {
			// SendVerifyEMail
			SendVerifyEMail sve = new SendVerifyEMail(session, vd)
			if (sve.sendResult) {
				publishElement("process", "verify-email-send")
			} else {
				publishElement("error", "verify-email")
			}
		} else {
			publishElement("error", "verify-email")
		}*/
	}

	
}
