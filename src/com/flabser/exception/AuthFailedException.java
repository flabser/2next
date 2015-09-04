package com.flabser.exception;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.flabser.restful.AppUser;

@SuppressWarnings("serial")
public class AuthFailedException extends ApplicationException {

	public AuthFailedException(AppUser user) {
		super(Response.status(Response.Status.UNAUTHORIZED).header(HttpHeaders.WWW_AUTHENTICATE, user.getError().toString()).entity(user)
				.build());
	}

	public AuthFailedException(AuthFailedExceptionType type, String dir) {
		super(dir, type.name());
		setCode(HttpServletResponse.SC_UNAUTHORIZED);
		setType("AUTHFAIL");
	}

}
