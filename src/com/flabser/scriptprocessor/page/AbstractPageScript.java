package com.flabser.scriptprocessor.page;

import java.util.ArrayList;
import java.util.Collection;

import com.flabser.localization.Vocabulary;
import com.flabser.script._IXMLContent;
import com.flabser.script._Session;
import com.flabser.script._Tag;
import com.flabser.script._WebFormData;
import com.flabser.script._XMLDocument;
import com.flabser.scriptprocessor.Msg;
import com.flabser.scriptprocessor.ScriptEvent;
import com.flabser.scriptprocessor.ScriptProcessorUtil;
import com.flabser.util.ResponseType;
import com.flabser.util.Util;
import com.flabser.util.XMLResponse;

public abstract class AbstractPageScript extends ScriptEvent implements IPageScript {	
	public ArrayList<Msg> messages = new ArrayList<Msg>();
	

	private String lang;
	private _WebFormData formData;
	private XMLResponse xmlResp = new XMLResponse(ResponseType.RESULT_OF_PAGE_SCRIPT);
	private ArrayList<_IXMLContent> xml = new ArrayList<_IXMLContent>(); 

	public void setSession(_Session ses){			
		this.ses = ses;
	}

	public void setFormData(_WebFormData formData){
		this.formData = formData;
	}

	public void setCurrentLang(Vocabulary vocabulary, String lang){
		this.lang = lang;
		this.vocabulary = vocabulary;
	}	

	public void setContent(_IXMLContent document) {
		xml.add(document);
	}

	public void setContent(Collection<_IXMLContent> documents) {
		xml.addAll(documents);
	}

	public void println(Exception e){
		String errText = e.toString();
		System.out.println(errText);
		_Tag tag = new _Tag("error",errText);
		tag.addTag(ScriptProcessorUtil.getScriptError(e.getStackTrace()));
		_XMLDocument xml = new _XMLDocument(tag);
		setContent(xml);
	}

	public void println(String e){		
		System.out.println(e);
	}

	public void showFile(String filePath, String fileName) {
        xmlResp.type = ResponseType.SHOW_FILE_AFTER_HANDLER_FINISHED;
        xmlResp.addMessage(filePath, "filepath");
        xmlResp.addMessage(fileName, "originalname");
    }

	/*public void showFile(_ExportManager attachment){
		xmlResp.type = ResponseType.SHOW_FILE_AFTER_HANDLER_FINISHED;
		xmlResp.addMessage(attachment.getFilePath(), "filepath");	
		xmlResp.addMessage(attachment.getOriginalFileName(), "originalname");	
	}*/

	public XMLResponse process(){

		long start_time = System.currentTimeMillis();
		try{					
			doProcess(ses, formData, lang);
			xmlResp.setPublishResult(toPublishElement);
			xmlResp.setResponseStatus(true);
			for(Msg message:messages){
				xmlResp.addMessage(message.text, message.id);	
			}

			if (xml != null)xmlResp.addXMLDocumentElements(xml);
			xmlResp.setRedirect(redirectURL);

		}catch(Exception e){
			xmlResp.setResponseStatus(false);
			xmlResp.addMessage(e.getMessage());
			println(e);
			e.printStackTrace();
		}

		xmlResp.setElapsedTime(Util.getTimeDiffInSec(start_time));
		return xmlResp;
	}

	public abstract void doProcess(_Session session, _WebFormData formData, String lang);
}
