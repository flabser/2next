package com.flabser.localization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.flabser.log.Log4jLogger;
import com.flabser.rule.Lang;

public class Vocabulary {
	private HashMap<String, Sentence> words = new HashMap<String, Sentence>();
	private HashMap<String, HashMap<String, String>> wordsLangs = new HashMap<String, HashMap<String, String>>();
	private static Log4jLogger logger = new Log4jLogger("Vocabulary");
	private String templateType;

	public Vocabulary(Document doc, String templateType, ArrayList<Lang> langsList) {
		org.w3c.dom.Element root = doc.getDocumentElement();
		this.templateType = templateType;
		ArrayList<LanguageType> list = new ArrayList<LanguageType>();
		for (Lang lang : langsList) {
			list.add(lang.type);
			HashMap<String, String> langCaptions = new HashMap<String, String>();
			wordsLangs.put(lang.type.name(), langCaptions);
		}

		NodeList nodename = root.getElementsByTagName("sentence");
		for (int i = 0; i < nodename.getLength(); i++) {
			Sentence sentence = new Sentence(nodename.item(i), list);
			if (sentence.isOn) {
				words.put(sentence.keyWord, sentence);
				for (Entry<String, SentenceCaption> l : sentence.words.entrySet()) {
					wordsLangs.get(l.getKey()).put(l.getValue().keyWord, l.getValue().word);
				}
			}
		}
	}

	@Deprecated
	public String getWord(String keyWord, String lang) {
		Sentence sent = words.get(keyWord);
		if (sent == null) {
			logger.warningLogEntry("translation of word \"" + keyWord + "\" to " + lang
					+ ", has not found in vocabulary (" + templateType + ")");
			return keyWord;
		} else {
			SentenceCaption caption = sent.words.get(lang);
			if (caption != null) {
				return caption.word;
			} else {
				return keyWord;
			}
		}
	}

	public String getWord(String keyWord, LanguageType lang) {
		Sentence sent = words.get(keyWord);
		if (sent == null) {
			logger.warningLogEntry("translation of word \"" + keyWord + "\" to " + lang
					+ ", has not found in vocabulary (" + templateType + ")");
			return keyWord;
		} else {
			SentenceCaption caption = sent.words.get(lang.name());
			if (caption != null) {
				return caption.word;
			} else {
				return keyWord;
			}
		}
	}

	public HashMap<String, String> getAllCaptions(String lang) {
		return wordsLangs.get(lang);
	}

}
