package com.flabser.rule.page;

import com.flabser.rule.constants.ValueSourceType;

public class ElementScript {
	private String className;
	private ValueSourceType type;

	ElementScript(ValueSourceType type, String className) {
		this.type = type;
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public ValueSourceType getType() {
		return type;
	}

}
