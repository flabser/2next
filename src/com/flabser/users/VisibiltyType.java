package com.flabser.users;

public enum VisibiltyType {
	UNKNOWN(0), PUBLIC(230), ONLY_MEMBERS(231);

	private int code;

	VisibiltyType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static VisibiltyType getType(int code) {
		for (VisibiltyType type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		return UNKNOWN;
	}
}
