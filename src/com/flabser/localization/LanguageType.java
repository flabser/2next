package com.flabser.localization;


public enum LanguageType {
	UNKNOWN(0), ENG(325), RUS(326), KAZ(327), BG(328);
	
	
	private int code;
	
	LanguageType(int code){
		this.code = code;
	}	
	
	public int getCode(){
		return code;
	}
	
	public static LanguageType getType(int code){
		for (LanguageType type : values()){
			if (type.code == code){
				return type;
			}
		}
		return UNKNOWN;
	}
}
