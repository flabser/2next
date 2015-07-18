package com.flabser.runtimeobj.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.flabser.appenv.AppEnv;
import com.flabser.env.Environment;
import com.flabser.exception.RuleException;
import com.flabser.localization.SentenceCaption;
import com.flabser.rule.Caption;
import com.flabser.rule.page.ElementRule;
import com.flabser.rule.page.PageRule;
import com.flabser.script._Page;
import com.flabser.script._URL;
import com.flabser.scriptprocessor.page.DoProcessor;
import com.flabser.scriptprocessor.page.IQuerySaveTransaction;
import com.flabser.supplier.SourceSupplier;
import com.flabser.users.UserSession;
import com.flabser.util.ScriptResponse;
import com.flabser.util.Util;

public class Page {

	public boolean fileGenerated;
	public String generatedFilePath;
	public String generatedFileOriginalName;
	private String httpMethod;
	public ArrayList<_URL> redirects = new ArrayList<_URL>();

	protected AppEnv env;
	protected PageRule rule;
	protected Map<String, String[]> fields = new HashMap<String, String[]>();
	protected UserSession userSession;

	public Page(AppEnv env, UserSession userSession, PageRule rule,
			String httpMethod) {
		this.userSession = userSession;
		this.env = env;
		this.rule = rule;
		this.httpMethod = httpMethod;
	}

	public HashMap<String, String[]> getCaptions(
			SourceSupplier captionTextSupplier, ArrayList<Caption> captions) {
		HashMap<String, String[]> captionsList = new HashMap<String, String[]>();
		for (Caption cap : captions) {
			SentenceCaption sc = captionTextSupplier
					.getValueAsCaption(cap.captionID);
			String c[] = new String[2];
			c[0] = sc.word;
			c[1] = sc.hint;
			captionsList.put(cap.captionID, c);
		}
		return captionsList;
	}

	public _Page process(Map<String, String[]> formData)
			throws ClassNotFoundException, RuleException {
		_Page pp = null;
		long start_time = System.currentTimeMillis();
		switch (rule.caching) {
		case NO_CACHING:
			pp = getContent(formData);
			break;
		case CACHING_IN_USER_SESSION_SCOPE:
			pp = userSession.getPage(this, formData);
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
		return "PAGE_" + rule.id + "_" + userSession.getLang();

	}

	public _Page getContent(Map<String, String[]> formData)
			throws ClassNotFoundException, RuleException {
		fields = formData;
		_Page pp = new _Page();

		if (rule.elements.size() > 0) {
			loop: for (ElementRule elementRule : rule.elements) {
				switch (elementRule.type) {
				case SCRIPT:
					DoProcessor sProcessor = new DoProcessor(env, userSession,
							userSession.getLang(), fields);
					ScriptResponse scriptResp = sProcessor.processScript(
							elementRule.doClassName, httpMethod);

					for (IQuerySaveTransaction toPostObects : sProcessor.transactionToPost) {
						toPostObects.post();
					}
					pp.addElements(scriptResp.getElements());
					redirects.addAll(scriptResp.getRedirects());

					break;

				case INCLUDED_PAGE:
					PageRule rule = (PageRule) env.ruleProvider
							.getRule(elementRule.value);
					IncludedPage page = new IncludedPage(env, userSession,
							rule, httpMethod);
					pp.addPage(page.process(fields));
					break;
				default:
					break;
				}
			}
		}

		SourceSupplier captionTextSupplier = new SourceSupplier(env,
				userSession.getLang());
		pp.setCaptions(getCaptions(captionTextSupplier, rule.captions));
		return pp;
	}
}
