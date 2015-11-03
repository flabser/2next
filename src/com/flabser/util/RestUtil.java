package com.flabser.util;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import com.flabser.exception.ServerServiceExceptionType;
import com.flabser.restful.pojo.Outcome;
import com.flabser.script._Session;
import com.flabser.script._Validator;
import com.flabser.script.mail._MailAgent;
import com.flabser.util.recaptcha.ReCaptcha;

public class RestUtil {
	public static Response processSimpleMessage(_Session session, String recipient, String email, String subj,
			String msg, String captcha) {
		Outcome res = new Outcome();
		String lang = session.getLang();
		ArrayList<String> e = validateSimpleMailForm(email, subj, msg, captcha);

		if (e.size() > 0) {
			res.setMessage(ServerServiceExceptionType.FORMDATA_INCORRECT, lang);
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(res.setMessages(e, lang)).build();
		}

		//
		ArrayList<String> recipients = new ArrayList<String>();
		recipients.add(recipient);
		_MailAgent ma = session.getMailAgent();
		if (ma.sendMail(recipients, subj, msg + " (lang: " + lang + " from: " + email + ")", false)) {
			res.addMessage("message_has_sent_succesfully", lang);
			return Response.status(HttpServletResponse.SC_OK).entity(res).build();
		} else {
			res.setMessage(ServerServiceExceptionType.MESSAGE_HAS_NOT_SENT, lang);
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(res.setMessages(e, lang))
					.build();
		}

	}

	private static ArrayList<String> validateSimpleMailForm(String email, String subj, String msg, String captcha) {
		ArrayList<String> errors = new ArrayList<String>();
		boolean isValid = true;

		if (!_Validator.checkEmail(email) && !_Validator.checkPhoneNumber(email)) {
			isValid = false;
			errors.add(ServerServiceExceptionType.EMAIL_IS_INCORRECT.name());
		}
		if (subj.isEmpty() || subj.length() > 256) {
			isValid = false;
			errors.add("subject_incorrect");
		}
		if (msg.isEmpty() || msg.length() > 3000 || msg.length() < 20) {
			errors.add("message_incorrect");
		}
		if (isValid && !validateReCaptcha(captcha)) {
			errors.add("captcha_incorrect");
		}
		return errors;
	}

	private static boolean validateReCaptcha(String captcha) {
		// TODO need secure secret
		String secret = "6Lf34Q0TAAAAAG5Yca5N4rbibH5YFrE0A5iXZd35";
		return ReCaptcha.validate(secret, captcha).isSuccess();
	}
}
