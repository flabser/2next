package com.flabser.scriptprocessor.page;

import com.flabser.localization.Vocabulary;
import com.flabser.script._Element;
import com.flabser.script._Exception;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.scriptprocessor.ScriptEvent;
import com.flabser.scriptprocessor.ScriptProcessorUtil;
import com.flabser.util.ResponseType;
import com.flabser.util.Util;
import com.flabser.util.ScriptResponse;

public abstract class AbstractPageScript extends ScriptEvent implements IPageScript {
	private String lang;
	private _WebFormData formData;
	private ScriptResponse resp = new ScriptResponse(ResponseType.RESULT_OF_PAGE_SCRIPT);

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

	public void println(Exception e) throws _Exception{
		String errText = e.toString();
		System.out.println(errText);
		_Element element = new _Element("error",errText + "stack:" + ScriptProcessorUtil.getScriptError(e.getStackTrace()));	
		publishElement("error",element);		
	}
	

	public void println(String e){		
		System.out.println(e);
	}

	public ScriptResponse process(){

		long start_time = System.currentTimeMillis();
		try{					
			doProcess(ses, formData, lang);
			resp.setPublishResult(toPublishElement);
			resp.setResponseStatus(true);		
	

		}catch(Exception e){
			resp.setResponseStatus(false);		
			try {
				println(e);
			} catch (_Exception e1) {			
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		resp.setElapsedTime(Util.getTimeDiffInSec(start_time));
		return resp;
	}

	public abstract void doProcess(_Session session, _WebFormData formData, String lang);
}
