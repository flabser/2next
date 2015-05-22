package com.flabser.runtimeobj.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.Const;
import com.flabser.env.Environment;
import com.flabser.exception.RuleException;
import com.flabser.localization.LocalizatorException;
import com.flabser.localization.SentenceCaption;
import com.flabser.rule.Caption;
import com.flabser.rule.page.ElementRule;
import com.flabser.rule.page.PageRule;
import com.flabser.script._Page;
import com.flabser.scriptprocessor.page.DoProcessor;
import com.flabser.scriptprocessor.page.IQuerySaveTransaction;
import com.flabser.supplier.SourceSupplier;
import com.flabser.users.User;
import com.flabser.users.UserSession;
import com.flabser.util.Util;
import com.flabser.util.Response;

public class Page implements Const {
	public boolean fileGenerated;
	public String generatedFilePath;
	public String generatedFileOriginalName;

	protected AppEnv env;
	protected PageRule rule;
	protected Map<String, String[]> fields = new HashMap<String, String[]>();
	protected UserSession userSession;

	public Page(AppEnv env, UserSession userSession, PageRule rule) {
		this.userSession = userSession;
		this.env = env;
		this.rule = rule;
	}

	public String getSpravFieldSet(User user, String lang) throws RuleException, LocalizatorException {
		StringBuffer glossariesAsText = new StringBuffer("<glossaries>");
		/*
		 * SourceSupplier ss = new SourceSupplier(user, env, lang); for
		 * (GlossaryRule glos : rule.getGlossary()) {
		 * glossariesAsText.append("<" + glos.name + ">" +
		 * ss.getDataAsXML(glos.valueSource, glos.value, glos.macro, lang) +
		 * "</" + glos.name + ">"); }
		 */
		return glossariesAsText.append("</glossaries>").toString();
	}

	public ArrayList<SentenceCaption> getCaptions(SourceSupplier captionTextSupplier, ArrayList<Caption> captions) {
		ArrayList<SentenceCaption> captionsList = new ArrayList<SentenceCaption>();
		
		for (Caption cap : captions) {
			SentenceCaption sc = captionTextSupplier.getValueAsCaption(cap.source, cap.captionID);
		/*	Element v = new Element("caption", sc.word);
			Element h = new Element("hint", sc.hint);
			Element captElement = new Element(cap.captionID);
			captElement.addTag(v);
			captElement.addTag(h);*/
			captionsList.add(sc);		
		}
		return captionsList;// captionsText.append("</captions>").toString();
	}

	public _Page process(Map<String, String[]> formData) throws ClassNotFoundException, RuleException {
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
		return "PAGE_" + rule.id + "_" + userSession.lang;

	}

	public _Page getContent(Map<String, String[]> formData) throws ClassNotFoundException, RuleException {
		fields = formData;
		_Page pp = new _Page();

		if (rule.elements.size() > 0) {
			loop: for (ElementRule elementRule : rule.elements) {
				switch (elementRule.type) {
				case STATIC_TAG:
					pp.addElement(elementRule.value, new Element(elementRule.name, elementRule.value));
					break;
				case SCRIPT:
					DoProcessor sProcessor = new DoProcessor(env, userSession, userSession.lang, fields);
					Response xmlResp = sProcessor.processScript(elementRule.doClassName);

					for (IQuerySaveTransaction toPostObects : sProcessor.transactionToPost) {
						toPostObects.post();
					}
					pp.addElements(xmlResp.getElements());
					

					break;

				case INCLUDED_PAGE:
					PageRule rule = (PageRule) env.ruleProvider.getRule(PAGE_RULE, elementRule.value);
					IncludedPage page = new IncludedPage(env, userSession, rule);
					pp.addPage(page.process(fields));
					break;
				default:
					pp.addElement(elementRule.value, new Element(elementRule.name, elementRule.value));
				}
			}
		}
		userSession.lang = "RUS";
		SourceSupplier captionTextSupplier = new SourceSupplier(env, userSession.lang);
		pp.setCaptions(getCaptions(captionTextSupplier, rule.captions));
		return pp;

	}

}
