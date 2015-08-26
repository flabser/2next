package com.flabser.apptemplate;


public enum ModeType {
	UNKNOWN(0), COMMON(205), CLOUD(206), MIXED(207);

	private int code;

	ModeType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static ModeType getType(int code) {
		for (ModeType type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		return UNKNOWN;
	}
}

