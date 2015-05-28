package com.flabser.script.actions;

import java.util.HashMap;

import com.flabser.rule.constants.RunMode;
import com.flabser.script._Exception;
import com.flabser.script._IPOJOContent;
import com.flabser.script._IXMLContent;
import com.flabser.script._Session;
import com.flabser.util.XMLUtil;


public class _Action  implements _IPOJOContent, _IXMLContent  {
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

	public StringBuffer toXML(){
		StringBuffer output = new StringBuffer(1000);
		output.append("<action  mode=\"" + isOn + "\"" + XMLUtil.getAsAttribute("url", url) + " id=\"" + customID + "\" caption=\"" + caption + "\" hint=\"" +  hint + "\">" + type + "</action>");
		return output;
	}

	void setSession(_Session ses){
		session = ses;
	}


	@Override
	public HashMap<String, Object> getJsonObject() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mode", isOn.name());
		map.put("url", url);
		map.put("id", customID);
		map.put("caption", caption);
		map.put("hint", hint);
		map.put("type", type.name());
		return map;
	}
}
