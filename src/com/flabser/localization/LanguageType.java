package com.flabser.localization;

/**
 * https://ru.wikipedia.org/wiki/%D0%9A%D0%BE%D0%B4%D1%8B_%D1%8F%D0%B7%D1%8B%D0%
 * BA%D0%BE%D0%B2 numeric code by ГОСТ 7.75-97
 */
public enum LanguageType {
	UNKNOWN(0), ENG(45), RUS(570), KAZ(255), BG(115), POR(545), SPA(230), CHO(315);

	private int code;

	LanguageType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static LanguageType getType(int code) {
		for (LanguageType type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		return UNKNOWN;
	}
}
