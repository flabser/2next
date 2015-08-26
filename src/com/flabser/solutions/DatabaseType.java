package com.flabser.solutions;


public enum DatabaseType {

	UNKNOWN(0), SYSTEM(420), POSTGRESQL(421);

	private int code;

	DatabaseType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getJDBCPrefix(DatabaseType t) {
		switch (t) {
		case POSTGRESQL:
			return "jdbc:postgresql://";
		case SYSTEM:
			return "jdbc:postgresql://";
		default:
			return "";

		}
	}

	public static DatabaseType getType(int code) {
		for (DatabaseType type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		return UNKNOWN;
	}
}
