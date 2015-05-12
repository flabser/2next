package com.flabser.rule;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import com.flabser.appenv.AppEnv;
import com.flabser.env.Environment;
import com.flabser.exception.RuleException;
import com.flabser.exception.WebFormValueException;
import com.flabser.exception.WebFormValueExceptionType;
import com.flabser.rule.constants.RuleType;
import com.flabser.rule.constants.RunMode;
import com.flabser.rule.page.ElementRule;
import com.flabser.rule.page.IElement;
import com.flabser.servlets.PublishAsType;
import com.flabser.util.XMLUtil;

public abstract class Rule implements IElement, IRule{
	public RunMode isOn = RunMode.ON;
	public boolean isValid = true;
	public String description;  
	public String id = "unknown";
	public String xsltFile;
	public PublishAsType publishAs = PublishAsType.XML;
	public String app;
	public Date lastUpdate = new Date();
	public Date regDate = new Date();
	public String filePath;
	public String scriptDirPath = "";
	public String primaryScriptDirPath;
	public int hits;
	public ArrayList<Caption> captions = new ArrayList<Caption>();	
	public boolean addToHistory;	
	public AppEnv env;	
	public ArrayList<ElementRule> elements = new ArrayList<ElementRule>();	
	protected org.w3c.dom.Document doc;
	protected RuleType type = RuleType.UNKNOWN;
	protected String targetDirectory = "libs";	
	
	private boolean allowAnonymousAccess;
	
	protected Rule(AppEnv env, File docFile) throws RuleException{		
		try {
			this.env = env;
			DocumentBuilderFactory pageFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder pageBuilder = pageFactory.newDocumentBuilder();
			Document xmlFileDoc = pageBuilder.parse(docFile.toString());
			doc = xmlFileDoc;
			filePath = docFile.getAbsolutePath();
		//	parentScriptDirPath = docFile.getParentFile().getAbsolutePath();
			scriptDirPath = env.globalSetting.rulePath + File.separator + "Resources" + File.separator + "scripts";
			primaryScriptDirPath = Environment.primaryAppDir + scriptDirPath;
	
			id = XMLUtil.getTextContent(doc,"/rule/@id");			 
			AppEnv.logger.verboseLogEntry("Load rule: " + this.getClass().getSimpleName() + ", id=" + id);
			if (XMLUtil.getTextContent(doc,"/rule/@mode").equalsIgnoreCase("off")){                    
				isOn = RunMode.OFF;	
				isValid = false;
			}
			
			if (XMLUtil.getTextContent(doc,"/rule/@anonymous").equalsIgnoreCase("on")){                    
				allowAnonymousAccess = true;	
			}
			
			if (XMLUtil.getTextContent(doc,"/rule/@history").equalsIgnoreCase("on")){                    
				addToHistory = true;	
			}
						
			xsltFile = XMLUtil.getTextContent(doc,"/rule/xsltfile");
			if (!xsltFile.equals("")) publishAs = PublishAsType.HTML;
			description = XMLUtil.getTextContent(doc,"/rule/description");		
		
			

			NodeList captionList =  XMLUtil.getNodeList(doc,"/rule/caption");   
			for(int i = 0; i < captionList.getLength(); i++){
				Caption c = new Caption(captionList.item(i));	
				if (c.isOn == RunMode.ON){
					captions.add(c);
				}
			}
			

		}catch(SAXParseException spe){
			AppEnv.logger.errorLogEntry("XML-file structure error (" + docFile.getAbsolutePath() + ")");
			AppEnv.logger.errorLogEntry(spe);
		}catch(FileNotFoundException e){
			throw new RuleException("Rule \"" + docFile.getAbsolutePath() + "\" has not found");
		}catch (ParserConfigurationException e) {
			AppEnv.logger.errorLogEntry(e);
		}catch (IOException e) {
			AppEnv.logger.errorLogEntry(e);
		}catch (SAXException se) {
			AppEnv.logger.errorLogEntry(se);
		}

	}

	protected String[] getWebFormValue(String fieldName, Map<String, String[]> fields) throws WebFormValueException{
		try{
			return fields.get(fieldName);
		}catch(Exception e){
			throw new WebFormValueException(WebFormValueExceptionType.FORMDATA_INCORRECT, fieldName);
		}
	}

	protected void setIsOn(String isOnAsText){		
		if(isOnAsText.equalsIgnoreCase("on")){
			isOn = RunMode.ON;
		}else{
			isOn = RunMode.OFF;
		}
	}
	
	protected void setDescription(String d){		
			description = d;	
	}
	
	protected void setID(String id){		
		this.id = id;	
	}

	protected void setCaptions(String[]id){		
		
	}

	public boolean isAnonymousAccessAllowed(){
		return allowAnonymousAccess;
	}
		
	public void plusHit(){
		hits ++ ;
	}

	public String toString(){
		return getClass().getSimpleName() + " id=" + id + ", file=" + filePath ;
	}	

	public String getXSLT() {	
		return xsltFile.replace("\\", File.separator);
	}

	public String getAsXML(){		
		return "";
	}

	public String getRuleAsXML(String app){
		String xmlText = "<rule id=\"" + id + "\" isvalid=\"" + isValid + "\" app=\"" + app + "\" ison=\"" + isOn + "\">" +			
		"<description>" + description + "</description>";		
		return xmlText + "</fields></rule>";
	}

	
	
	@Override
	public boolean addToHistory(){
		return addToHistory;
	}
	
	abstract public void update(Map<String, String[]> fields) throws WebFormValueException;		
	
	abstract public boolean save() ;

	

	public 	String getRuleID(){
		return type + "_" + id;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public AppEnv getAppEnv() {
		return env;
	}

	@Override
	public String getScriptDirPath() {
		return scriptDirPath;
	}

	@Override
	public String getPrimaryScriptDirPath() {
		return primaryScriptDirPath;
	}


	@Override
	public ArrayList<Caption> getCaptions() {
		return captions;
	}
	
}
