package com.flabser.exception;

public class RuleException extends Exception {

	private static final long serialVersionUID = 4762010135613823296L;
	private Exception realException;
	
	public RuleException(String error) {
		super(error);
		
			
		realException = this;
	}
	
	public Exception getRealException() {
		return realException;
	}
	
}
