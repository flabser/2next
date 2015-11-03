package com.flabser.scriptprocessor.page;

import javax.ws.rs.HttpMethod;

import com.flabser.exception.WebFormValueException;
import com.flabser.localization.Vocabulary;
import com.flabser.script._Element;
import com.flabser.script._Exception;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.scriptprocessor.ScriptEvent;
import com.flabser.scriptprocessor.ScriptProcessorUtil;
import com.flabser.util.ResponseType;
import com.flabser.util.ScriptResponse;

public abstract class AbstractPageScript extends ScriptEvent implements IPageScript {

	private String method;
	private String lang;
	private _WebFormData formData;
	private ScriptResponse resp = new ScriptResponse(ResponseType.RESULT_OF_PAGE_SCRIPT);

	@Override
	public void setSession(_Session ses) {
		this.ses = ses;
	}

	@Override
	public void setFormData(_WebFormData formData) {
		this.formData = formData;
	}

	@Override
	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public void setCurrentLang(Vocabulary vocabulary, String lang) {
		this.lang = lang;
		this.vocabulary = vocabulary;
	}

	public void println(Exception e) throws _Exception {
		String errText = e.toString();
		_Element element = new _Element("error",
				errText + "stack:" + ScriptProcessorUtil.getScriptError(e.getStackTrace()));
		publishElement("error", element);
	}

	@Override
	public ScriptResponse process() {
		try {
			switch (method) {
			case HttpMethod.GET:
				doGet(ses, formData, lang);
				break;

			case HttpMethod.POST:
				doPost(ses, formData, lang);
				break;

			case HttpMethod.PUT:
				doPut(ses, formData, lang);
				break;

			case HttpMethod.DELETE:
				doDelete(ses, formData, lang);
				break;
			}

			resp.setPublishResult(toPublishElement);
			resp.setResponseStatus(true);

		} catch (Exception e) {
			resp.setResponseStatus(false);
			try {
				println(e);
			} catch (_Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		return resp;
	}

	public abstract void doGet(_Session session, _WebFormData formData, String lang)
			throws WebFormValueException, _Exception;

	public abstract void doPost(_Session session, _WebFormData formData, String lang) throws WebFormValueException;

	public abstract void doPut(_Session session, _WebFormData formData, String lang) throws WebFormValueException;

	public abstract void doDelete(_Session session, _WebFormData formData, String lang) throws WebFormValueException;
}
