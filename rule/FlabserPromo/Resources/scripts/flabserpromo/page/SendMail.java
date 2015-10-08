package flabserpromo.page;

import java.util.ArrayList;

import com.flabser.script._Exception;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;
import com.flabser.script.mail._MailAgent;


public class SendMail extends _DoScript {

	@Override
	public void doPost(_Session session, _WebFormData formData, String lang) {
		String email = formData.getValueSilently("email");
		String subj = formData.getValueSilently("subject");
		String msg = formData.getValueSilently("message");

		ArrayList<String> recipients = new ArrayList<String>();
		recipients.add("k-zone@ya.ru");

		_MailAgent ma = session.getMailAgent();
		ma.sendMail(recipients, subj, msg + " (lang: " + lang + " from: " + email + ")", false);

	}

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) throws _Exception {
	}

	@Override
	public void doPut(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doDelete(_Session session, _WebFormData formData, String lang) {
	}
}
