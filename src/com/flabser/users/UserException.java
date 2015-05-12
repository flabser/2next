package com.flabser.users;


public class UserException extends Exception {
	public int id;
	public String user;
	public UserExceptionType exceptionType;
	
	private static final long serialVersionUID = 4762010135613823296L;
	private String errorText;
	
		
	public UserException(UserExceptionType error, String user) {
		super();
		this.user = user;
		switch(error){ 
		case REDIRECT_URL_NOT_DEFINED:		
		//	errorText = "Redirect URL has not defined , user=" + user + "(" + new User(user).getAllUserGroups() + ")";
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
