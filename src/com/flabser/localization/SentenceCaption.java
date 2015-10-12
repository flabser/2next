package com.flabser.localization;

public class SentenceCaption {

	public String keyWord;
	public String word;


	public SentenceCaption() {

	}

	public SentenceCaption(String keyword, String caption, String hint) {
		this.keyWord = keyword;
		this.word = caption;
	}

	public String toAttrValue() {
		return "<" + keyWord + " caption=\"" + word + "\" ></" + keyWord + ">";
	}

	public Object toXML() {
		return "<" + keyWord + " caption=\"" + word + "\" ></" + keyWord + ">";
	}
}
