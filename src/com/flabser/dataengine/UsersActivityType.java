package com.flabser.dataengine;

public enum UsersActivityType {
	UNKNOWN(1009), MARKED_AS_READ(1001), MARKED_AS_UNREAD(1002), COMPOSED(1003), 
	MODIFIED(1004), DELETED(1005), UNDELETED(1006), LOGGED_IN(1007), LOGGED_OUT(1008), 
	COMPLETELY_DELETED(1009), MARKED_AS_FAVORITE(1010), UNMARKED_AS_FAVORITE(1011), CUSTOM_EVENT(1012);
	
	UsersActivityType(int code){
		this.code = code;
	}
	
	private int code;
	
	public int getCode(){
		return code;
	}
	
	public static UsersActivityType getType(int code){
		for (UsersActivityType type : values()){
			if (type.code == code){
				return type;
			}
		}
		return UNKNOWN;
	}
	
}
