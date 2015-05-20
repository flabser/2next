package com.flabser.scriptprocessor.page;

import java.util.ArrayList;
import java.util.Collection;

import com.flabser.localization.Vocabulary;
import com.flabser.runtimeobj.page.Element;
import com.flabser.script._IXMLContent;
import com.flabser.script._Session;
import com.flabser.script._Element;
import com.flabser.script._WebFormData;
import com.flabser.script._WebDocument;
import com.flabser.scriptprocessor.Msg;
import com.flabser.scriptprocessor.ScriptEvent;
import com.flabser.scriptprocessor.ScriptProcessorUtil;
import com.flabser.util.ResponseType;
import com.flabser.util.Util;
import com.flabser.util.Response;

public abstract class AbstractPageScript extends ScriptEvent implements IPageScript {	
	public ArrayList<Msg> messages = new ArrayList<Msg>();
	

	private String lang;
	private _WebFormData formData;
	private Response resp = new Response(ResponseType.RESULT_OF_PAGE_SCRIPT);
	private ArrayList<Element> xml = new ArrayList<Element>(); 

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
//		xml.add(document);
	}

	public void setContent(Collection<_IXMLContent> documents) {
//		xml.addAll(documents);
	}

	public void println(Exception e){
		String errText = e.toString();
		System.out.println(errText);
		_Element tag = new _Element("error",errText);
		tag.addTag(ScriptProcessorUtil.getScriptError(e.getStackTrace()));
		_WebDocument xml = new _WebDocument(tag);
		setContent(xml);
	}

	public void println(String e){		
		System.out.println(e);
	}

	public Response process(){

		long start_time = System.currentTimeMillis();
		try{					
			doProcess(ses, formData, lang);
			resp.setPublishResult(toPublishElement);
			resp.setResponseStatus(true);		

			if (xml != null)resp.addXMLDocumentElements(xml);
			

		}catch(Exception e){
			resp.setResponseStatus(false);		
			println(e);
			e.printStackTrace();
		}

		resp.setElapsedTime(Util.getTimeDiffInSec(start_time));
		return resp;
	}

	public abstract void doProcess(_Session session, _WebFormData formData, String lang);
}
