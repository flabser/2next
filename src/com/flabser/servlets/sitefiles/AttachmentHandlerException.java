package com.flabser.servlets.sitefiles;

public class AttachmentHandlerException extends Exception {
	private static final long serialVersionUID = -7955384945856281213L;
	private Exception realException;
	private AttachmentHandlerExceptionType type;
	private String errorText;
	
	public AttachmentHandlerException(AttachmentHandlerExceptionType type, String error) {
		super(error);	
		this.type = type;
		switch(type){ 
		case FILE_NOT_FOUND:		
			errorText = "File not found";
			break;
		case FORMSESID_IS_NOT_CORRECT:		
			errorText = "there is no formsesid";
			break;
		
		}		
		realException = this;
	}
	
	public AttachmentHandlerException(String error) {
		super(error);			
		realException = this;
	}
	
	public Exception getRealException() {
		return realException;
	}
	
	public String getMessage(){
		return errorText;
		
	}
	
}
