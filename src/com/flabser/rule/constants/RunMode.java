package com.flabser.rule.constants;


public enum RunMode {
	UNKNOWN(0), OFF(10), ON(11), DEBUG(12), HIDE(13);

	private int code;

	RunMode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static RunMode getType(int code) {
		for (RunMode type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		return UNKNOWN;
	}
}
