package cashtracker.page.app

import com.flabser.script.*
import com.flabser.users.*
import com.flabser.env.*

class SendVerifyEMail {

	public boolean sendResult = false;

	public SendVerifyEMail(_Session session, User user) {
		def code = user.getVerifyCode()
		def url = session.getFullAppURI()
		def subj = "Confirmation of the E-mail your account in CashTracker site"
		def msg = """<h4>Confirmation of the E-mail</h4>
					<p>Ignore this letter, if you have not registered on the site
						<a href="${url}"><b>${url}</b></a>
					</p>
					<div>
					<b>Click on the link to confirmation your address</b><br/>
					<a href="${url}/Provider?type=page&id=verify-email&code=${code}">
						${url}/Provider?type=page&id=verify-email&code=${code}
					</a></div>"""
		def recipients = []
		recipients << user.getEmail()
		sendResult = session.getMailAgent().sendMail(recipients, subj, msg, false)
	}
}
