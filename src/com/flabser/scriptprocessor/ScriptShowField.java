package com.flabser.scriptprocessor;

import com.flabser.script._Exception;
import com.flabser.script._IXMLContent;
import com.flabser.util.XMLUtil;

public class ScriptShowField implements _IXMLContent {
	private String name;
	private String value = "";
	private String idValue;
	private boolean hasAttr;
	private String XMLPiece;
	
	public ScriptShowField(String name, String value, boolean noConvert){
		this.name = name;
		this.value = value;
		XMLPiece = "<" + name + ">" + (noConvert ? value : XMLUtil.getAsTagValue(value)) + "</" + name + ">";
	}
	
	public ScriptShowField(String name, _IXMLContent value) throws _Exception{
		this.name = name;
		this.value = value.toString();
		XMLPiece = "<" + name + ">" + value.toXML() + "</" + name + ">";
	}
	
	public ScriptShowField(String name, String value){
		this.name = name;
		this.value = value;
		XMLPiece = "<" + name + ">" + XMLUtil.getAsTagValue(value) + "</" + name + ">";
	}

	public ScriptShowField(String name, int idv, String value){
		this.name = name;
		this.value = value;
		this.idValue = Integer.toString(idv);
		hasAttr = true;
		XMLPiece = "<" + name + " attrval=\"" + idValue + "\">" + XMLUtil.getAsTagValue(value) + "</" + name + ">";
	}
	
	public ScriptShowField(String name, String idValue, String value){
		this.name = name;
		this.value = value;
		this.idValue = idValue;
		hasAttr = true;
		XMLPiece = "<" + name + " attrval=\"" + idValue + "\">" + XMLUtil.getAsTagValue(value) + "</" + name + ">";
	}
	

	
	
	public String toString(){
		return XMLPiece;
	}

	@Override
	public String toXML() throws _Exception {		
		return XMLPiece;
	}

}
