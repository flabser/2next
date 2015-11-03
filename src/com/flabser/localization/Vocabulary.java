package com.flabser.localization;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.flabser.log.Log4jLogger;
import com.flabser.rule.Lang;

public class Vocabulary {
	private HashMap<String, Sentence> words = new HashMap<String, Sentence>();
	private HashMap<String, ArrayList<String>> wordsLangs = new HashMap<String, ArrayList<String>>();
	private static Log4jLogger logger = new Log4jLogger("Vocabulary");
	private String templateType;

	public Vocabulary(Document doc, String templateType, ArrayList<Lang> langsList) {
		org.w3c.dom.Element root = doc.getDocumentElement();
		this.templateType = templateType;

		NodeList nodename = root.getElementsByTagName("sentence");
		for (int i = 0; i < nodename.getLength(); i++) {
			Sentence sentence = new Sentence(nodename.item(i), langsList);
			if (sentence.isOn) {
				words.put(sentence.keyWord, sentence);
				for (SentenceCaption l : sentence.words.values()) {
					ArrayList<String> wordsList = wordsLangs.get(l);
					if (wordsList == null) {
						wordsList = new ArrayList<String>();
						// wordsLangs.put(l, wordsList);
					}
					// wordsList.add(sentence.words.get(l));

				}

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
			if (caption != null) {
				return caption.word;
			} else {
				return keyWord;
			}
		}
	}

	public ArrayList<String> getAllCaptions(String lang) {
		return null;
	}

}
