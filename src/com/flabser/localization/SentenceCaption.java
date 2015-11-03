package com.flabser.localization;

public class SentenceCaption {

	public String keyWord;
	public String word;

	public SentenceCaption() {

	}

	public SentenceCaption(String keyword, String caption) {
		this.keyWord = keyword;
		this.word = caption;
	}

	public Object toXML() {
		return "<" + keyWord + " caption=\"" + word + "\" ></" + keyWord + ">";
	}
}
