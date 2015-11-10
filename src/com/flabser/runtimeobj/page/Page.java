package com.flabser.runtimeobj.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.env.Environment;
import com.flabser.exception.AuthFailedException;
import com.flabser.exception.RuleException;
import com.flabser.exception.WebFormValueException;
import com.flabser.rule.Caption;
import com.flabser.rule.page.ElementRule;
import com.flabser.rule.page.PageRule;
import com.flabser.script._Page;
import com.flabser.script._Session;
import com.flabser.scriptprocessor.page.DoProcessor;
import com.flabser.scriptprocessor.page.IQuerySaveTransaction;
import com.flabser.supplier.SourceSupplier;
import com.flabser.util.ScriptResponse;
import com.flabser.util.Util;

public class Page {

	public boolean fileGenerated;
	public String generatedFilePath;
	public String generatedFileOriginalName;
	private String httpMethod;

	protected AppTemplate env;
	protected PageRule rule;
	protected Map<String, String[]> fields = new HashMap<String, String[]>();
	protected _Session ses;

	private String context;

	public Page(AppTemplate env, _Session ses, PageRule rule, String httpMethod, String context)
			throws AuthFailedException {
		this.ses = ses;
		this.env = env;
		this.rule = rule;
		this.httpMethod = httpMethod;
		this.context = context;

	}

	public HashMap<String, String> getCaptions(SourceSupplier captionTextSupplier, ArrayList<Caption> captions) {
		HashMap<String, String> captionsList = new HashMap<String, String>();
		for (Caption cap : captions) {
			String sc = captionTextSupplier.getValueAsCaption(cap.captionID);
			captionsList.put(cap.captionID, sc);
		}
		return captionsList;
	}

	public _Page process(Map<String, String[]> formData)
			throws ClassNotFoundException, RuleException, WebFormValueException {
		_Page pp = null;
		long start_time = System.currentTimeMillis();
		switch (rule.caching) {
		case NO_CACHING:
			pp = getContent(formData);
			break;
		case CACHING_IN_USER_SESSION_SCOPE:
			pp = ses.getPage(this, formData);
			break;
		case CACHING_IN_APPLICATION_SCOPE:
			pp = env.getPage(this, formData);
			break;
		case CACHING_IN_SERVER_SCOPE:
			pp = new Environment().getPage(this, formData);
			break;
		default:
			pp = getContent(formData);
		}

		pp.setId(rule.id);
		pp.setCaching(rule.caching);
		pp.setElapsed_time(Util.getTimeDiffInSec(start_time));
		return pp;
	}

	public String getID() {
		return "PAGE_" + rule.id + "_" + ses.getLang();

	}

	@SuppressWarnings("unused")
	public _Page getContent(Map<String, String[]> formData)
			throws ClassNotFoundException, RuleException, WebFormValueException {
		fields = formData;
		_Page pp = new _Page();

		if (rule.elements.size() > 0) {
			loop: for (ElementRule elementRule : rule.elements) {
				switch (elementRule.type) {
				case SCRIPT:
					ScriptResponse scriptResp = null;
					DoProcessor sProcessor = new DoProcessor(env, ses, fields, context);
					switch (elementRule.doClassName.getType()) {
					case GROOVY_FILE:
						scriptResp = sProcessor.processGroovyScript(elementRule.doClassName.getClassName(), httpMethod);
						break;
					case FILE:
						scriptResp = sProcessor.processGroovyScript(elementRule.doClassName.getClassName(), httpMethod);
						break;
					case JAVA_CLASS:
						scriptResp = sProcessor.processJava(elementRule.doClassName.getClassName(), httpMethod);
						break;
					case UNKNOWN:
						break;
					default:
						break;

					}

					for (IQuerySaveTransaction toPostObects : sProcessor.transactionToPost) {
						toPostObects.post();
					}
					pp.addElements(scriptResp.getElements());

					break;

				case INCLUDED_PAGE:
					PageRule rule = (PageRule) env.ruleProvider.getRule(elementRule.value);
					IncludedPage page = new IncludedPage(env, ses, rule, httpMethod, context);
					pp.addPage(page.process(fields));
					break;
				default:
					break;
				}
			}
		}

		SourceSupplier captionTextSupplier = new SourceSupplier(env, ses.getLang());
		pp.setCaptions(getCaptions(captionTextSupplier, rule.captions));
		return pp;
	}
}
