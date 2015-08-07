package com.flabser.exception;

public class ApplicationException extends Exception {
	private static final long serialVersionUID = 1L;
	private Exception realException;

	public ApplicationException(String error) {
		super(error);

		realException = this;
	}

	public Exception getException() {
		return realException;
	}

}
