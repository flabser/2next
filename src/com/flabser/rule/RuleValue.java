package com.flabser.rule;

import com.flabser.rule.constants.ValueSourceType;


public class RuleValue implements IRuleValue {
	public String value;
	ValueSourceType sourceType;
	
	public RuleValue(String value, String st, String ft){
		try {
			this.value = value;
			sourceType = ValueSourceType.valueOf(st);
		} catch (IllegalArgumentException e) {
			this.value = "";
			sourceType = ValueSourceType.UNKNOWN;
		}
	}
	
	@Override
	public ValueSourceType getSourceType() {
		return sourceType;
	}

	@Override
	public String getValue() {		
		return value;
	}

	public String toString(){
		return "value=" + value + ", source=" + sourceType ;
	}
	
	public String toXML(){
		return "<value>" + value + "</value><source>" + sourceType + "</source>";
	}
}
