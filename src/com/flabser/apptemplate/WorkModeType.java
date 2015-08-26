package com.flabser.apptemplate;


public enum WorkModeType {
	UNKNOWN(0), COMMON(205), CLOUD(206), MIXED(207);

	private int code;

	WorkModeType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static WorkModeType getType(int code) {
		for (WorkModeType type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		return UNKNOWN;
	}
}

