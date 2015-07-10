package com.flabser.script;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

public class _WebFormDataRest extends _WebFormData {
	private MultivaluedMap<String, String> formData;

	public _WebFormDataRest(MultivaluedMap<String, String> formData){
		this.formData = formData;
	}

	/*public String[] getListOfValuesSilently(String fn){
		List<String> value = formData.get(fn);
		if (value != null){
			return value.toArray(String[]);
		}else{
			String val[] = {""};
			return val;
		}

	}*/

	public String getValueSilently(String fn){
		try{
			List<String> value = formData.get(fn);
			return value.get(0).trim();
		}catch(Exception e){
			return "";
		}
	}

	/*	public String getEncodedValueSilently(String fn){
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

	}*/



}
