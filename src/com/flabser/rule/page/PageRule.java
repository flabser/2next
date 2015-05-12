package com.flabser.rule.page;


import java.io.File;
import java.util.*;

import org.w3c.dom.*;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.Const;
import com.flabser.exception.RuleException;
import com.flabser.exception.WebFormValueException;
import com.flabser.rule.Rule;
import com.flabser.rule.constants.RuleType;
import com.flabser.rule.constants.RunMode;
import com.flabser.util.XMLUtil;

public class PageRule extends Rule implements IElement, Const{
	public boolean isValid;
	public ArrayList<ElementRule> elements = new ArrayList<ElementRule>();		

	public boolean qoEnable;
	public String qoClassName;
	public CachingStrategyType caching = CachingStrategyType.NO_CACHING;
	public final RuleType type = RuleType.PAGE;
	
	public PageRule(AppEnv env, File ruleFile) throws RuleException{
		super(env, ruleFile);
		try{

			String cachingValue = XMLUtil.getTextContent(doc,"/rule/caching", false);
			if (!cachingValue.equalsIgnoreCase("")){
				caching = CachingStrategyType.valueOf(cachingValue);
			}
			
			
			NodeList fields =  XMLUtil.getNodeList(doc,"/rule/element");   
			for(int i = 0; i < fields.getLength(); i++){
				ElementRule element = new ElementRule(fields.item(i), this);						
				if (element.isOn != RunMode.OFF && element.isValid){					
					elements.add(element);
				}
			}


			isValid = true;	
		} catch(Exception e) {                
			AppEnv.logger.errorLogEntry(e);
		}
	}



	public String toString(){
		return "PAGE id=" + id + ", ison=" + isOn;
	}


	@Override
	public void update(Map<String, String[]> fields)throws WebFormValueException {
		
		
	}



	@Override
	public boolean save() {		
		return false;
	}

	
	
}
