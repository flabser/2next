package cashtracker.page.app

import com.flabser.script.*
import com.flabser.users.*
import com.flabser.env.*

class SendVerifyEMail {

	public boolean sendResult = false;

	public SendVerifyEMail(_Session session, User user) {
		def code = user.getVerifyCode()
		def url = session.getFullAppURI()
		def subj = "Подтверждение E-mail Вашей учетной записи CashTracker"
		def msg = """<h4>Подтверждение E-mail</h4>
					<p>Проигнорируйте это письмо, если вы не регистрировались на сайте
						<a href="${url}"><b>${url}</b></a>
					</p>
					<div>
					<b>Пройдите по ссылке для подтверждения адреса</b><br/>
					<a href="${url}/Provider?type=page&id=verify-email&code=${code}">
						${url}/Provider?type=page&id=verify-email&code=${code}
					</a></div>"""
		def recipients = []
		recipients << user.getEmail()
		sendResult = session.getMailAgent().sendMail(recipients, subj, msg, false)
	}
}
