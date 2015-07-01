package com.flabser.users;


public enum UserStatusType {
	UNKNOWN(0), NOT_VERIFIED(455), REGISTERED(456), DELETED(457), WAITING_FOR_VERIFYCODE(458), WAITING_FOR_FIRST_ENTERING(459);
	
	
	private int code;
	
	UserStatusType(int code){
		this.code = code;
	}	
	
	public int getCode(){
		return code;
	}
	
	public static UserStatusType getType(int code){
		for (UserStatusType type : values()){
			if (type.code == code){
				return type;
			}
		}
		return UNKNOWN;
	}
}
