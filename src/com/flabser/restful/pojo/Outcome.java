package com.flabser.restful.pojo;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.exception.WebFormValueException;

@JsonRootName("outcome")
public class Outcome {
	OutcomeType type = OutcomeType.OK;
	String message;

	public OutcomeType getType() {
		return type;
	}

	public void setType(OutcomeType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setError(boolean b) {
		type = OutcomeType.ERROR;

	}

	public void setErrorMsg(WebFormValueException e) {
		message = e.getMessage();

	}

}
