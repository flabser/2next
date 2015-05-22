package com.flabser.localization;

public class SentenceCaption{
	public String keyWord;
	public String word;
	public String hint;

	public SentenceCaption(){
		this.hint = word;
	}
	
	public SentenceCaption(String keyword, String caption, String hint){
		this.keyWord = keyword;
		this.word = caption;
		if (hint.equals("")){
			this.hint = caption;
		}else{
			this.hint = hint;
		}
	}
	
	public String toAttrValue(){
		return "<" + keyWord + " caption=\"" + word + "\" hint=\"" + hint + "\" ></" + keyWord + ">";		
	}

	public Object toXML() {
		return "<" + keyWord + " caption=\"" + word + "\" hint=\"" + hint + "\" ></" + keyWord + ">";	
	}
	
}