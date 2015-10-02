package com.flabser.dataengine.system.entities.constants;


public enum InvitationType {
	UNKNOWN(0), WAITING(456), EXPIRED(457), SENDING_FAILED(458), SAVING_FAILED(459);

	private int code;

	InvitationType(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public static InvitationType getType(int code){
		for (InvitationType type : values()){
			if (type.code == code){
				return type;
			}
		}
		return UNKNOWN;
	}
}
