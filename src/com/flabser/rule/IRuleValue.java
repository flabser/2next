package com.flabser.rule;

import com.flabser.rule.constants.ValueSourceType;

public interface IRuleValue {
	ValueSourceType getSourceType();
	String getValue();
}
