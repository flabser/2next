package com.flabser.users;


public enum UserStatusType {
	UNKNOWN(0), NOT_VERIFIED(455), REGISTERED(456), DELETED(457), WAITING_FOR_VERIFYCODE(458), WAITING_FIRST_ENTERING_AFTER_INVITATION(459), VERIFYCODE_NOT_SENT(460), INVITATTION_NOT_SENT(462);


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
