package com.flabser.script;

import java.util.regex.Pattern;

public class _URL {
	
	private String urlAsString;
	
	_URL(String urlAsString){
		this.urlAsString = urlAsString;
	}
	
	public void changeParameter(String parName, String value){
        int ind = urlAsString.indexOf(parName + "=");
        if(ind > -1){
            Pattern pattern = Pattern.compile("[a-z0-9]");
            int endInd = ind;
            for(int i = ind + parName.length()+1; i < urlAsString.length(); i++){
                if(!pattern.matcher(urlAsString.substring(i, i + 1)).matches()){
                    break;
                }
                endInd = i;
            }
            if(endInd != ind){
                urlAsString = urlAsString.substring(0, ind) + parName + "=" + value + urlAsString.substring(endInd+1, urlAsString.length());
            } else {
                urlAsString = urlAsString.substring(0, ind) + parName + "=" + value + urlAsString.substring(endInd + parName.length() + value.length()-1, urlAsString.length());
            }
        }else{
        	urlAsString = urlAsString + "&" + parName + "=" + value;
        }
    }
	
	public String toString(){
		return urlAsString;
	}

}
