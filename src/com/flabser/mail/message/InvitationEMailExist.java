package com.flabser.mail.message;

import java.util.ArrayList;

import com.flabser.script._Session;
import com.flabser.script.mail._MailAgent;
import com.flabser.users.User;

public class InvitationEMailExist implements IEMail {

	private _Session session;
	private ArrayList<String> recipients = new ArrayList<String>();
	private String subj;
	private String msg;

	public InvitationEMailExist(_Session session, User user, String message, User tempUser) {
		this.session = session;

		String url = session.getBaseAppURL();
		String userName = user.getUserName();
		if (userName == null){
			userName = user.getLogin();
		}

		subj = "Invitation to participate as user in \"" + session.getAppType() + "\" application";
		msg = "<h3>" + message + "<h3><h5>Additional info from the service: The cloud service invite you to participate as regular user in \"" + session.getAppType() + "\" application."
				+ "You was invited by \"" + userName + "\". Welcome."
				+ "<p>Ignore this letter, if you don't want to use the service."
				+ "</p><div><b>Click on the link to jump to the service</b><br/><a href=\"" + url + "\">"
				+ url + "</a></div>";
		recipients.add(tempUser.getEmail());
	}

	@Override
	public boolean send() {
		_MailAgent ma = session.getMailAgent();
		return ma.sendMail(recipients, subj, msg, false);
	}
}
