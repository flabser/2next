package com.flabser.solutions.cashtracker.constants;

public enum BudgetState {
	UNKNOWN(0), ACTIVE(653), DELETED(457);

	private int code;

	private BudgetState(int code) {
		this.code = code;
	}

	public static BudgetState stateOf(int code) {
		for (BudgetState type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		return UNKNOWN;
	}

	public int getCode() {
		return code;
	}
}
