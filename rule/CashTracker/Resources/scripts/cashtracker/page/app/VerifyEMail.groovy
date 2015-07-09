package cashtracker.page.app

import com.flabser.env.*
import com.flabser.script.*
import com.flabser.users.*


class VerifyEMail {

	private def session
	private def recipients = []
	private def subj
	private def msg

	public VerifyEMail(_Session session, User user) {
        this.session = session
		def code = user.getVerifyCode()
		def url = session.getFullAppURI()
		subj = "Confirmation of the E-mail your account in CashTracker site"
		msg = """<h4>Confirmation of the E-mail</h4>
					<p>Ignore this letter, if you have not registered on the site
						<a href="${url}"><b>${url}</b></a>
					</p>
					<div>
					<b>Click on the link to confirmation your address</b><br/>
					<a href="${url}/Provider?id=verify-email&code=${code}">
						${url}/Provider?id=verify-email&code=${code}
					</a></div>"""
		recipients << user.getEmail()
	}

	public boolean send() {
        def ma = session.getMailAgent()
		return ma.sendMail(recipients, subj, msg, false)
	}
}
