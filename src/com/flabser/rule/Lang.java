package com.flabser.rule;

import org.w3c.dom.Node;

import com.flabser.localization.LanguageType;
import com.flabser.rule.constants.RunMode;
import com.flabser.server.Server;
import com.flabser.util.XMLUtil;

public class Lang {
	public RunMode isOn = RunMode.ON;
	public boolean isValid = true;
	public String description;
	public String id = "unknown";
	public LanguageType type;
	private String name;

	public Lang(LanguageType t, String name) {
		type = t;
		this.name = name;
	}

	Lang(Node node) {
		id = XMLUtil.getTextContent(node, "@id", false);
		name = XMLUtil.getTextContent(node, ".", false);
		LanguageType l = LanguageType.valueOf(id);
		if (l == null) {
			isOn = RunMode.OFF;
			isValid = true;
			Server.logger.warningLogEntry("unknown language \"" + name + "\"(id=" + id + ")");
		} else {
			if (XMLUtil.getTextContent(node, "@mode", false).equalsIgnoreCase("off")) {
				isOn = RunMode.OFF;
				isValid = false;
			} else {
				type = l;
			}
		}
	}

	@Override
	public String toString() {
		return "id:" + id + ", name:" + name;
	}

	public String toXML() {
		return "<ison>" + isOn + "</ison><id>" + id + "</id><name>" + name + "</name>";
	}

}
