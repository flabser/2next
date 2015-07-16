package com.flabser.users;

public enum ApplicationStatusType {
	UNKNOWN(0), ON_LINE(895), READY_TO_REMOVE(896), DEPLOING_FAILED(897), DATABASE_NOT_CREATED(898);


	private int code;

	ApplicationStatusType(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public static ApplicationStatusType getType(int code){
		for (ApplicationStatusType type : values()){
			if (type.code == code){
				return type;
			}
		}
		return UNKNOWN;
	}
}
