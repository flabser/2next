package com.flabser.runtimeobj.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.Const;
import com.flabser.env.Environment;
import com.flabser.exception.RuleException;
import com.flabser.localization.LocalizatorException;
import com.flabser.rule.Caption;
import com.flabser.rule.constants.ValueSourceType;
import com.flabser.rule.page.ElementRule;
import com.flabser.rule.page.PageRule;
import com.flabser.scriptprocessor.page.DoProcessor;
import com.flabser.scriptprocessor.page.IQuerySaveTransaction;
import com.flabser.supplier.SourceSupplier;
import com.flabser.users.User;
import com.flabser.users.UserSession;
import com.flabser.util.ResponseType;
import com.flabser.util.Util;
import com.flabser.util.XMLResponse;

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
        /*SourceSupplier ss = new SourceSupplier(user, env, lang);
        for (GlossaryRule glos : rule.getGlossary()) {
            glossariesAsText.append("<" + glos.name + ">" + ss.getDataAsXML(glos.valueSource, glos.value, glos.macro, lang) + "</" + glos.name + ">");
        }*/
        return glossariesAsText.append("</glossaries>").toString();
    }

    public String getCaptions(SourceSupplier captionTextSupplier, ArrayList<Caption> captions) {
        StringBuffer captionsText = new StringBuffer("<captions>");
        for (Caption cap : captions) {
            captionsText.append("<" + cap.captionID + captionTextSupplier.getValueAsCaption(cap.source, cap.value).toAttrValue() + "></" + cap.captionID + ">");
        }
        return captionsText.append("</captions>").toString();
    }


    public String getAsXML(User user, String lang) throws LocalizatorException, RuleException {
        SourceSupplier captionTextSupplier = new SourceSupplier(env, lang);
        String captions = getCaptions(captionTextSupplier, rule.captions);
        String glossarySet = getSpravFieldSet(user, lang);
        return "<content>" + rule.getAsXML() + glossarySet + captions + "</content>";
    }


    public StringBuffer process(Map<String, String[]> formData) throws ClassNotFoundException, RuleException {
        StringBuffer resultOut = null;
        long start_time = System.currentTimeMillis();
        switch (rule.caching) {
            case NO_CACHING:
                resultOut = getContent(formData);
                break;
            case CACHING_IN_USER_SESSION_SCOPE:
                resultOut = userSession.getPage(this, formData);
                break;
            case CACHING_IN_APPLICATION_SCOPE:
                resultOut = env.getPage(this, formData);
                break;
            case CACHING_IN_SERVER_SCOPE:
                resultOut = new Environment().getPage(this, formData);
                break;
            default:
                resultOut = getContent(formData);
        }
      //  DocID toFlash = userSession.getFlashDoc();
        String flashAttr = "";
      /*  if (toFlash != null) {
            flashAttr = "flashdocid=\"" + toFlash.id + "\" flashdoctype=\"" + toFlash.type + "\"";
        }*/
        StringBuffer output = new StringBuffer(5000);

        output.append("<page id=\"" + rule.id + "\" cache=\"" + rule.caching + "\" elapsed_time = \"" + Util.getTimeDiffInSec(start_time) + "\" " + flashAttr + ">");
        output.append(resultOut);
        return output.append("</page>");
    }

    public String getID() {
        return "PAGE_" + rule.id + "_" + userSession.lang;

    }


    public StringBuffer getContent(Map<String, String[]> formData) throws ClassNotFoundException, RuleException {
        fields = formData;
        StringBuffer output = new StringBuffer(1000);
        User user = userSession.currentUser;
      
        if (rule.elements.size() > 0) {
            loop:
            for (ElementRule elementRule : rule.elements) {
                if (elementRule.hasElementName) output.append("<" + elementRule.name + ">");
                switch (elementRule.type) {
                    case STATIC_TAG:
                        output.append(elementRule.value);
                        break;
                    case SCRIPT:
                     DoProcessor sProcessor = new DoProcessor(env, userSession, userSession.lang, fields);
                        XMLResponse xmlResp = sProcessor.processScript(elementRule.doClassName);

                        for (IQuerySaveTransaction toPostObects : sProcessor.transactionToPost) {
                            toPostObects.post();
                        }

                        if (xmlResp.type == ResponseType.SHOW_FILE_AFTER_HANDLER_FINISHED) {
                            fileGenerated = true;
                            generatedFilePath = xmlResp.getMessage("filepath").text;
                            generatedFileOriginalName = xmlResp.getMessage("originalname").text;
                            break loop;
                        } else {
                            output.append(xmlResp.toXML());
                        }

                        break;
                
                    case INCLUDED_PAGE:
                        PageRule rule = (PageRule) env.ruleProvider.getRule(PAGE_RULE, elementRule.value);
                        //	IncludedPage page = new IncludedPage(env, userSession, rule, request, response);
                        IncludedPage page = new IncludedPage(env, userSession, rule);
                        output.append(page.process(fields));
                        break;
                }
                if (elementRule.hasElementName) output.append("</" + elementRule.name + ">");
            }
        }
        SourceSupplier captionTextSupplier = new SourceSupplier(env, userSession.lang);
        return output.append(getCaptions(captionTextSupplier, rule.captions));

    }

    protected int[] getParentDocProp(Map<String, String[]> formData) {
        int[] prop = new int[2];
        try {
            prop[0] = Integer.parseInt(formData.get("parentdocid")[0]);
        } catch (Exception nfe) {
            prop[0] = 0;
        }
        try {
            prop[1] = Integer.parseInt(formData.get("parentdoctype")[0]);
        } catch (Exception nfe) {
            prop[1] = DOCTYPE_UNKNOWN;
        }
        return prop;
    }

   

}
