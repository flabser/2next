package flabserpromo.page;

import java.util.ArrayList;

import com.flabser.script._Exception;
import com.flabser.script._Session;
import com.flabser.script._Validator;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;
import com.flabser.script.mail._MailAgent;
import com.flabser.util.recaptcha.ReCaptcha;


public class SendMail extends _DoScript {

	@Override
	public void doPost(_Session session, _WebFormData formData, String lang) {
		String email = formData.getValueSilently("email");
		String subj = formData.getValueSilently("subject");
		String msg = formData.getValueSilently("message");
		String grecaptcha = formData.getValueSilently("g-recaptcha-response");

		if (!validateForm(email, subj, msg, grecaptcha)) {
			return;
		}

		//
		ArrayList <String> recipients = new ArrayList <String>();
		recipients.add("k-zone@ya.ru");
		_MailAgent ma = session.getMailAgent();
		ma.sendMail(recipients, subj, msg + " (lang: " + lang + " from: " + email + ")", false);

		publishElement("result", "ok");
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

	//
	public boolean validateForm(String email, String subj, String msg, String captcha) {
		boolean isValid = true;
		String errors = "";

		if (!_Validator.checkEmail(email)) {
			isValid = false;
			errors += "email,";
		}
		if (subj.isEmpty() || subj.length() > 256) {
			isValid = false;
			errors += "subject,";
		}
		if (msg.isEmpty() || msg.length() > 3000 || msg.length() < 20) {
			isValid = false;
			errors += "message,";
		}
		if (isValid && !validateReCaptcha(captcha)) {
			isValid = false;
			errors += "recaptcha";
		}

		if (!isValid) {
			publishElement("errors", errors);
		}

		return isValid;
	}

	private boolean validateReCaptcha(String captcha) {
		return ReCaptcha.validate(captcha).isSuccess();
	}
}
