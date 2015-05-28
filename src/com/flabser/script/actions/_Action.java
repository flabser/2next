package com.flabser.script.actions;

import com.flabser.rule.constants.RunMode;
import com.flabser.script._Exception;
import com.flabser.script._Session;
import com.flabser.util.XMLUtil;


public class _Action {
	public RunMode isOn = RunMode.ON;
	public String caption;
	public String hint;
	public _ActionType type;
	public String customID;

	private _Session session;
	private String url = "";

	public _Action(_ActionType type){
		this.type = type;
		switch (type){
		case CLOSE:
			caption = "Close";		
			hint = "Close";			
			break;
		case GET_DOCUMENT_ACCESSLIST:
			caption = "Get document access list";		
			hint = "Get document access list";	
			break;
		default:
			caption = "";		
			hint = "";
		}
		customID = type.toString().toLowerCase();
	}
	
	public _Action(String caption, String hint,  _ActionType type){
		this.caption = caption;
		this.hint = hint;
		this.type = type;
		customID = type.toString().toLowerCase();
	}

	_Action(String caption, String hint, String customID){
		this.caption = caption;
		this.hint = hint;
		this.type = _ActionType.CUSTOM_ACTION;
		this.customID = customID;
	}

	public void setURL(String u){
		url = u;
	}

	public String toXML(){
		return "<action  mode=\"" + isOn + "\"" + XMLUtil.getAsAttribute("url", url) + " id=\"" + customID + "\" caption=\"" + caption + "\" hint=\"" +  hint + "\">" + type + getJson(type) + "</action>";
	}

	void setSession(_Session ses){
		session = ses;
	}

	private String getJson(_ActionType type){
		switch (type){
		case CLOSE:
		/*	try {
				return "<js><![CDATA[window.location.href = \"" + session.getLastPageURL() + "\"]]></js>";
			} catch (_Exception e) {
				return "<js><![CDATA[window.location.href = \"" + session.getLastURL() + "\"]]></js>";
			}*/
		case GET_DOCUMENT_ACCESSLIST:
			/*_Document doc = session.getDocumentInConext();
			if (doc != null){
				return "<js><![CDATA[window.location.href = \"Provider?type=service&operation=get_accesslist&id=get_accesslist&docid=" + doc.getID() + "\"]]></js>";
			}else{
				return "<js><![CDATA[window.location.href = \"Provider?type=service&operation=get_accesslist&id=get_accesslist&docid==\"]]></js>";
			}*/
		default:
			return "";
		}
	}
	//Provider?type=page&id=accesslist&docid=3W93088orWwWo63r
}
