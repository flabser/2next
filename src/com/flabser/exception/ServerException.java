package com.flabser.exception;


public class ServerException extends Exception {
	public int id;
	public String user;
	
	private static final long serialVersionUID = 4762010135613823296L;
	private String errorText;
	
		
	public ServerException(ServerExceptionType error, String addText) {
		super();	
		switch(error){ 
		case APPENV_HAS_NOT_INITIALIZED:		
			errorText = "Applocation environment has not initialized. See server log " + addText;
			break;		
		
		}		
	}
	
	public String getMessage(){
		return errorText;
	}
	
	public String toString(){
		return errorText;
	}
	
}
