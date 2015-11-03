package com.flabser.restful.pojo;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.env.Environment;
import com.flabser.exception.ServerServiceExceptionType;
import com.flabser.exception.ServerServiceWarningType;
import com.flabser.exception.WebFormValueException;

@JsonRootName("outcome")
@JsonPropertyOrder({ "type", "warningId", "errorId", "message" })
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

	public Outcome addMessage(String message, String lang) {
		this.message.add(Environment.vocabulary.getWord(message, lang));
		return this;
	}

	public Outcome setError(WebFormValueException e, String lang) {
		return setMessage(e.id, lang);
	}

	public String getErrorId() {
		return errorId;
	}

	public Outcome setMessage(String s, String lang) {
		message.clear();
		addMessage(s, lang);
		return this;
	}

	public Outcome setMessage(ServerServiceExceptionType e, String lang) {
		type = OutcomeType.ERROR;
		errorId = e.name();
		message.add(Environment.vocabulary.getWord(e.name(), lang));
		return this;
	}

	public Outcome setMessage(ServerServiceWarningType w, String lang) {
		type = OutcomeType.WARNING;
		warningId = w.name();
		message.add(Environment.vocabulary.getWord(w.name(), lang));
		return this;
	}

	public String getWarningId() {
		return warningId;
	}

	public Outcome addMessage(String msg) {
		this.message.add(msg);
		return this;

	}

	public Object setMessages(ArrayList<String> e, String lang) {
		for (String msg : e) {
			addMessage(msg, lang);
		}
		return this;
	}

	public Outcome setMessages(ArrayList<String> all) {
		message.addAll(all);
		return this;
	}

}
