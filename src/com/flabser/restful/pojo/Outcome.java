package com.flabser.restful.pojo;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.exception.ServerServiceExceptionType;
import com.flabser.exception.ServerServiceWarningType;
import com.flabser.exception.WebFormValueException;

@JsonRootName("outcome")
@JsonPropertyOrder({"type", "warningId", "errorId", "message"})
public class Outcome {
	private OutcomeType type = OutcomeType.OK;
	private String errorId;
	private String warningId;
	private ArrayList<String> message = new ArrayList<String>();

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

	public Outcome setError(WebFormValueException e,  String lang) {
		return setMessage(e.id, lang);
	}

	public String getErrorId() {
		return errorId;
	}

	public Outcome setMessage(String s,  String lang) {
		//TODO system vocabulary
		//message.add(Environment.vocabulary.getWord(e.name(), lang));
		message.clear();
		addMessage(s);
		return this;
	}

	public Outcome setMessage(ServerServiceExceptionType e,  String lang) {
		type = OutcomeType.ERROR;
		errorId = e.name();
		//TODO system vocabulary
		//message.add(Environment.vocabulary.getWord(e.name(), lang));
		message.add(e.name());
		return this;
	}

	public Outcome setMessage(ServerServiceWarningType w, String lang) {
		type = OutcomeType.WARNING;
		warningId = w.name();
		//TODO system vocabulary
		//message.add(Environment.vocabulary.getWord(e.name(), lang));
		message.add(w.name());
		return this;
	}

	public String getWarningId() {
		return warningId;
	}
}
