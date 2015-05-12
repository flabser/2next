package com.flabser.runtimeobj.caching;

public enum CachingStrategyType {
	UNKNOWN(1000),
	NO_CACHING(1001),
	CACHING_IN_USER_SESSION_SCOPE(1002),
	CACHING_IN_APPLICATION_SCOPE(1003);
	
	private int code;
	
	CachingStrategyType(int code){
		this.code = code;
	}	
	
	public int getCode(){
		return code;
	}
	
	public static CachingStrategyType getType(int code){
		for (CachingStrategyType type : values()){
			if (type.code == code){
				return type;
			}
		}
		return UNKNOWN;
	}
}
