package com.flabser.restful.pojo;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.exception.WebFormValueException;

@JsonRootName("outcome")
public class Outcome {
	OutcomeType type = OutcomeType.OK;
	ArrayList<String> message = new ArrayList<String>();

	public OutcomeType getType() {
		return type;
	}

	public Outcome setType(OutcomeType type) {
		this.type = type;
		return this;
	}

	public ArrayList<String> getMessage() {
		return message;
	}

	public Outcome addMessage(String message) {
		this.message.add(message);
		return this;
	}

	public Outcome setError(boolean b) {
		type = OutcomeType.ERROR;
		return this;

	}

	public Outcome setError(WebFormValueException e) {
		type = OutcomeType.ERROR;
		message.add(e.getMessage());
		return this;
	}


}
