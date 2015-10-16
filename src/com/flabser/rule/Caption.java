package com.flabser.rule;

import org.w3c.dom.Node;

import com.flabser.rule.constants.RunMode;
import com.flabser.util.XMLUtil;

public class Caption {

	public RunMode isOn = RunMode.OFF;
	public String captionID = "";

	public Caption(Node node) {
		captionID = XMLUtil.getTextContent(node, "@name", false);
		if (!captionID.isEmpty()) {
			isOn = RunMode.ON;
		}
	}

}
