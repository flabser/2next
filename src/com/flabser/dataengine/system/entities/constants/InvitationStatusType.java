package com.flabser.dataengine.system.entities.constants;


public enum InvitationStatusType {
	UNKNOWN(0), WAITING(456), EXPIRED(457), SENDING_FAILED(458), SAVING_FAILED(459);

	private int code;

	InvitationStatusType(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public static InvitationStatusType getType(int code){
		for (InvitationStatusType type : values()){
			if (type.code == code){
				return type;
			}
		}
		return UNKNOWN;
	}
}
