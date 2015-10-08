package com.flabser.util.recaptcha;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ReCaptchaResponse {

	boolean success;

	@JsonProperty("error-codes")
	String[] errorCodes;

	public ReCaptchaResponse() {

	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String[] getErrorCodes() {
		return errorCodes;
	}

	public void setErrorCodes(String[] errorCodes) {
		this.errorCodes = errorCodes;
	}
}
