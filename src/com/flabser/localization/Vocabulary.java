package com.flabser.localization;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.flabser.env.Environment;
import com.flabser.rule.GlobalSetting;
import com.flabser.util.XMLUtil;

import java.util.HashMap;

public class Vocabulary {
	public String id;
	public HashMap<String, Sentence> words = new HashMap<String, Sentence>();
	public GlobalSetting globalSetting;
	 
	public Vocabulary(Document doc, String id,  GlobalSetting globalSetting) {
		this.id = id;
		this.globalSetting = globalSetting;
		org.w3c.dom.Element root = doc.getDocumentElement();
		Language primaryLang = Language.valueOf(XMLUtil.getTextContent(doc,"/vocabulary/@primary",true,"UNKNOWN", false).toUpperCase());
		
		
		NodeList nodename = root.getElementsByTagName("sentence");		
		for(int i = 0; i<nodename.getLength();i++){			
			Sentence sentence = new Sentence(nodename.item(i), primaryLang.toString());			
			if (sentence.isOn){
				words.put(sentence.keyWord, sentence);				
			}
		}
	}

	public String[] getWord(String keyWord, String lang){
		String returnVal[] = new String[2];
		Sentence sent = words.get(keyWord);
		if (sent == null && keyWord !="№"){
			if (globalSetting.multiLangEnable){
				Environment.logger.warningLogEntry("Translation of word \"" + keyWord + "\" to " + lang + ", has not found in vocabulary");	
			}
			returnVal[0] = keyWord;
			returnVal[1] = "";
			return returnVal;
		}else{
			SentenceCaption caption = sent.words.get(lang);
			returnVal[0] = caption.word;
			returnVal[1] = caption.hint;
			return returnVal;
		}
	}

	public SentenceCaption getSentenceCaption(String keyWord, String lang){		
		Sentence sent = words.get(keyWord.trim());
		if (sent == null){
			Environment.logger.warningLogEntry("Translation of word \"" + keyWord + "\" to " + lang + ", has not found in vocabulary");			
			SentenceCaption primary = new SentenceCaption(keyWord,keyWord);
			return primary;
		}else{
			SentenceCaption caption = sent.words.get(lang);	
			if (caption != null){
				return caption;	
			}else{
				SentenceCaption primary = new SentenceCaption(keyWord,keyWord);
				return primary;
			}
			
		}
	}
		
	public StringBuffer toXML(String lang) {
		StringBuffer output = new StringBuffer(1000);
		for(Sentence s: words.values()){
			//output.append(new ViewEntry(s.words.get(lang).word,s.code).toXML());
		}
		return output;
	}
	
}
