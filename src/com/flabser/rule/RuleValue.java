package com.flabser.rule;

import com.flabser.rule.constants.FieldType;
import com.flabser.rule.constants.ValueSourceType;


public class RuleValue implements IRuleValue {
	public String value;
	ValueSourceType sourceType;
	FieldType fieldType;
	
	public RuleValue(String value, String st, String ft){
		try {
			this.value = value;
			sourceType = ValueSourceType.valueOf(st);
			fieldType = FieldType.valueOf(ft);
		} catch (IllegalArgumentException e) {
			this.value = "";
			sourceType = ValueSourceType.UNKNOWN;
			fieldType = FieldType.UNKNOWN;
		}
	}
	
	@Override
	public ValueSourceType getSourceType() {
		return sourceType;
	}

	@Override
	public Enum getValueType() {		
		return fieldType;
	}

	@Override
	public String getValue() {		
		return value;
	}

	public String toString(){
		return "value=" + value + ", source=" + sourceType + ", type=" + fieldType;
	}
	
	public String toXML(){
		return "<value>" + value + "</value><source>" + sourceType + "</source><type>" + fieldType + "</type>";
	}
}
