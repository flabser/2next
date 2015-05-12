package com.flabser.rule;

import org.w3c.dom.Node;

import com.flabser.rule.constants.RunMode;
import com.flabser.rule.constants.ValueSourceType;
import com.flabser.util.XMLUtil;


public class Caption {
	public RunMode isOn = RunMode.OFF;
	public String captionID = "";
	public String value = "";
	public ValueSourceType source;
	
	
	public Caption(Node node){
		captionID = XMLUtil.getTextContent(node,"@name", false);
		if (!captionID.equals("")){
			value = XMLUtil.getTextContent(node,".", false);
			source = ValueSourceType.valueOf(XMLUtil.getTextContent(node,"@source",true,"KEYWORD", false));
			isOn =  RunMode.ON;
		}
	}

	public Caption(String value){
		this.value = value;
		source = ValueSourceType.STATIC;
        isOn =  RunMode.ON;
	}
}
