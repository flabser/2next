package com.flabser.script;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.flabser.dataengine.Const;

public class _WebFormData {
	private Map<String, String[]> formData;
	
	public _WebFormData(Map<String, String[]> formData){
		this.formData = formData;
	}

	public _WebFormData() {
		
	}

	@Deprecated
	public String[] get(String fn) throws _Exception{
		String value[] = formData.get(fn);
		if (value != null){
			return value;
		}else{
			throw new _Exception(_ExceptionType.FORMDATA_INCORRECT, "field=" + fn);
		}

	}

	public String[] getListOfValuesSilently(String fn){
		String value[] = formData.get(fn);
		if (value != null){
			return value;
		}else{
			String val[] = {""};
			return val;
		}

	}

	public String getValueSilently(String fn){
		try{
			String value[] = formData.get(fn);		
			return value[0].trim();
		}catch(Exception e){			
			return "";
		}
	}

	public String getEncodedValueSilently(String fn){
		try{
			return new String(((String)getValueSilently(fn)).getBytes("ISO-8859-1"),"UTF-8");
		}catch(Exception e){			
			return "";
		}
	}
	
	public int getNumberValueSilently(String fn, int defaultValue){
		try{
			String value[] = formData.get(fn);			
			return Integer.parseInt(value[0].trim());
		}catch(Exception e){			
			return defaultValue;
		}
	}
	
	public double getNumberDoubleValueSilently(String fn, double defaultValue){
		try{
			String value[] = formData.get(fn);			
			return Double.parseDouble(value[0].trim());
		}catch(Exception e){			
			return defaultValue;
		}
	}
	
	public String[] getListOfValues(String fn) throws _Exception{
		String value[] = formData.get(fn);
		if (value != null){
			return value;
		}else{
			throw new _Exception(_ExceptionType.FORMDATA_INCORRECT, "value of field=" + fn + " has not resolved");
		}

	}

	public String getValue(String fn) throws _Exception{
		try{
			String value[] = formData.get(fn);			
			return value[0].trim();
		}catch(Exception e){
			throw new _Exception(_ExceptionType.FORMDATA_INCORRECT, "value of field=" + fn + " has not resolved");
		}
	}

	public Map<String, String[]> getFormData() {
		return formData;
	}

	public boolean containsField(String key) {
		return formData.containsKey(key);
	}

	public int getSizeOfField(String fn){
		try{
			String value[] = formData.get(fn);
			return value.length;
		}catch(Exception e){
			return 0;
		}
	}
	
	@Deprecated
	public int[] getParentDocID() throws _Exception{
		int[] prop = new int[2]; 
		try{
			prop[0] = Integer.parseInt(getValue("parentdocid"));
		}catch(Exception nfe){
			prop[0] = 0;
		}
		try{
			prop[1] = Integer.parseInt(getValue("parentdoctype"));
		}catch(Exception nfe){
			prop[1] = Const.DOCTYPE_UNKNOWN;
		}
		return prop;
	}
	
	
	
	public String toString(){
		String result = "-----------begin of list of web form data-----------\n";
		                 
		Iterator<String> en = formData.keySet().iterator();

		while (en.hasNext()){
			String webFormFieldName = en.next();
			String[] val = (String[])formData.get(webFormFieldName);
			String v = "";
			for (int i = 0; i < val.length; i++){
				v += val[i] + "[" + Integer.toString(i) + "],";
			}
			result += webFormFieldName + "=" + v + "\n";					
		}

		result += "----------------- end of list-----------------------";
		return result;

	}
	


}
