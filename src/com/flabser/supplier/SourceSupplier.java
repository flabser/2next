package com.flabser.supplier;

import java.util.HashMap;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.localization.SentenceCaption;
import com.flabser.localization.Vocabulary;

public class SourceSupplier {
	public HashMap<String, Vocabulary> staticGlossaries = new HashMap<String, Vocabulary>();

	protected AppTemplate env;
	private Vocabulary vocabulary;
	private String lang;

	public SourceSupplier(AppTemplate env, String lang) {
		this.env = env;
		this.vocabulary = env.vocabulary;
		this.lang = lang;
	}

	public SentenceCaption getValueAsCaption(String keyWord) {
		return vocabulary.getSentenceCaption(keyWord, lang);
	}

}
