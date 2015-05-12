package com.flabser.localization;

public class SentenceCaption{
	public String word;
	public String hint;

	public SentenceCaption(){
		this.hint = word;
	}
	
	public SentenceCaption(String word, String hint){
		this.word = word;
		if (hint.equals("")){
			this.hint = word;
		}else{
			this.hint = hint;
		}
	}
	
	public String toAttrValue(){
		return " caption=\"" + word + "\" hint=\"" + hint + "\" ";		
	}
	
}