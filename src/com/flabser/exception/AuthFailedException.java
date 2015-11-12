package com.flabser.exception;

import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class AuthFailedException extends ApplicationException {

	public AuthFailedException(AuthFailedExceptionType type, String dir) {
		super(dir, type.name());
		setCode(HttpServletResponse.SC_UNAUTHORIZED);
		setType("AUTHFAIL");
	}

}
