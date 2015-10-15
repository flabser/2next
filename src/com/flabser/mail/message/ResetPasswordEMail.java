package com.flabser.mail.message;

import java.util.ArrayList;

import com.flabser.script._Session;
import com.flabser.script.mail._MailAgent;
import com.flabser.users.User;

public class ResetPasswordEMail implements IEMail {

	private _Session session;
	private ArrayList<String> recipients = new ArrayList<String>();
	private String subj;
	private String msg;

	public ResetPasswordEMail(_Session session, User user) {
		this.session = session;

		String url = session.getBaseAppURL();
		String userName = user.getUserName();
		if (userName == null){
			userName = user.getLogin();
		}
		subj = "Invitation to participate as user in \"" + session.getAppType() + "\" application";
		msg = "<h5>This message has sent by restore password service. Your password has been reseted and the service generwted new temporary password"
				+ " to enter in portal again. The  password will remove in 1 hour and your account will block. After you connect to the service, "
				+ "please change your temporary password for more secure your data</h5>"
				+ "login is your e-mail address<br/>"
				+ "password:" + user.getPwd() + "<br/>"
				+ "<p>Ignore this letter, if you don't want to use the service."
				+ "</p><div><b>Click on the link to jump to the service</b><br/><a href=\"" + url + "/Provider?id=verify_email&code=\">"
				+ url + "/Provider?id=verify_email&code=</a></div>";
		recipients.add(user.getEmail());
	}

	@Override
	public boolean send() {
		_MailAgent ma = session.getMailAgent();
		return ma.sendMail(recipients, subj, msg, false);
	}
}
