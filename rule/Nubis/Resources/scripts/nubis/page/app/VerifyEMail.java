package nubis.page.app;

import java.util.ArrayList;

import com.flabser.script._Session;
import com.flabser.script.mail._MailAgent;
import com.flabser.users.User;

public class VerifyEMail {

	private _Session session;
	private ArrayList<String> recipients = new ArrayList<String>();
	private String subj;
	private String msg;

	public VerifyEMail(_Session session, User user) {
		this.session = session;
		String code = user.getVerifyCode();
		String url = session.getFullAppURI();
		subj = "Confirmation of the E-mail your account in " + session.getAppType();
		msg = "<h4>Confirmation of the E-mail</h4><p>Ignore this letter, if you have not registered on the site<a href=\"" + url + "\"><b>" + url + "</b></a>"
				+ "</p><div><b>Click on the link to confirmation your address</b><br/<a href=\"" + url + "/Provider?id=verify-email&code=" + code + "\">" + url
				+ "/Provider?id=verify-email&code=" + code + "</a></div>";
		recipients.add(user.getEmail());
	}

	public boolean send() {
		_MailAgent ma = session.getMailAgent();
		return ma.sendMail(recipients, subj, msg, false);
	}
}
