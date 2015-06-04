package com.flabser.script;

import java.util.Locale;
import java.util.ResourceBundle;

import com.flabser.appenv.AppEnv;

public class _Exception extends Exception {
	private static final long serialVersionUID = -6047450542781990205L;
	private String errorText;
	private _Session session;
	private String serviceAddInfo = "";
	
	_Exception(String text){
		super(text);
		errorText = text;
	}
	
	public _Exception(_ExceptionType type,  String addInfo){
		super(type.toString() + " " + addInfo);
		errorText = type.toString() + ". " + addInfo;
	}

	public _Exception(_ExceptionType type,  String addInfo, String serviceAddInfo, _Session session){
		super(type.toString() + " " + addInfo);
		this.serviceAddInfo = "," + serviceAddInfo;
		errorText = type.toString() + ". " + addInfo;
		  this.session = session;
	}
	
    public _Exception(_ExceptionType type, String addInfo, _Session session) {
        this(type, addInfo);
        this.session = session;
    }

    public _Exception(String text, _Session session) {
        this(text);
        this.session = session;
    }

	public String getMessage(){
		return errorText;
	}

    public String getLocalizedMessage() {
        String error_message = this.errorText; 
        return error_message + serviceAddInfo;
    }

	public String toString(){
		return errorText;
	}
	
}
