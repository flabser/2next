package com.flabser.localization;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.flabser.log.Log4jLogger;

public class Vocabulary {
	public HashMap<String, Sentence> words = new HashMap<String, Sentence>();
	private static Log4jLogger logger = new Log4jLogger("Vocabulary");
	private String templateType;

	public Vocabulary(Document doc, String templateType) {
		org.w3c.dom.Element root = doc.getDocumentElement();
		this.templateType = templateType;

		NodeList nodename = root.getElementsByTagName("sentence");
		for (int i = 0; i < nodename.getLength(); i++) {
			Sentence sentence = new Sentence(nodename.item(i));
			if (sentence.isOn) {
				words.put(sentence.keyWord, sentence);
			}
		}
	}

	public String getWord(String keyWord, String lang) {
		Sentence sent = words.get(keyWord);
		if (sent == null) {
			logger.warningLogEntry("translation of word \"" + keyWord + "\" to " + lang
					+ ", has not found in vocabulary (" + templateType + ")");
			return keyWord;
		} else {
			SentenceCaption caption = sent.words.get(lang);
			return caption.word;
		}
	}

	public SentenceCaption getSentenceCaption(String keyWord, String lang) {
		Sentence sent = words.get(keyWord.trim());
		if (sent == null) {
			logger.warningLogEntry("translation of word \"" + keyWord + "\" to " + lang
					+ ", has not found in vocabulary (" + templateType + ")");
			// System.out.println("Translation of word \"" + keyWord + "\" to "
			// + lang + ", has not found in vocabulary");
			SentenceCaption primary = new SentenceCaption(keyWord, keyWord, keyWord);
			return primary;
		} else {
			SentenceCaption caption = sent.words.get(lang);
			if (caption != null) {
				return caption;
			} else {
				SentenceCaption primary = new SentenceCaption(keyWord, keyWord, keyWord);
				return primary;
			}

		}
	}

}
