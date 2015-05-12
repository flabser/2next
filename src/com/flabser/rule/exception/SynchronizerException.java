package com.flabser.rule.exception;

public class SynchronizerException extends Exception {
	private static final long serialVersionUID = -3882182689002701624L;
	public int id;
	public String user;
	private String errorText;
	
	
	public SynchronizerException(RuleExceptionType error) {
		super();		
		switch(error){ 
		case CUTOFF_TIME_ERROR:

		}		
	}
	
	public String getMessage(){
		return errorText;
	}
	
	public String toString(){
		return errorText;
	}
	
}
