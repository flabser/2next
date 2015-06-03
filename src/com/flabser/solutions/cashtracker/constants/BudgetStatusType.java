package com.flabser.solutions.cashtracker.constants;


public enum BudgetStatusType {
	UNKNOWN(0), ACTIVE(653), DELETED(457);
	
	
	private int code;
	
	BudgetStatusType(int code){
		this.code = code;
	}	
	
	public int getCode(){
		return code;
	}
	
	public static BudgetStatusType getType(int code){
		for (BudgetStatusType type : values()){
			if (type.code == code){
				return type;
			}
		}
		return UNKNOWN;
	}
}
