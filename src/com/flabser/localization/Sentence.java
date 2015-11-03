package com.flabser.localization;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.flabser.server.Server;
import com.flabser.util.XMLUtil;

public class Sentence {
	public boolean isOn = true;
	public String keyWord;
	public HashMap<String, SentenceCaption> words = new HashMap<String, SentenceCaption>();

	Sentence(Node node, ArrayList<LanguageType> list) {
		try {
			if (!XMLUtil.getTextContent(node, "@mode", false).equals("on")) {
				isOn = false;
				return;
			}
			keyWord = XMLUtil.getTextContent(node, "@keyword", false);
			NodeList entries = XMLUtil.getNodeList(node, "entry");
			for (int i = 0; i < entries.getLength(); i++) {
				Node wordNode = entries.item(i);
				LanguageType l = LanguageType
						.valueOf(XMLUtil.getTextContent(wordNode, "@lang", true, "UNKNOWN", false).toUpperCase());
				SentenceCaption c = new SentenceCaption(keyWord, wordNode.getTextContent());
				if (list.contains(l)) {
					words.put(l.name(), c);
				}
			}

		} catch (Exception e) {
			Server.logger.errorLogEntry(this.getClass().getSimpleName(), e);
		}
	}

	@Override
	public String toString() {
		return "keyword=" + keyWord + ", words=" + words;
	}

}
