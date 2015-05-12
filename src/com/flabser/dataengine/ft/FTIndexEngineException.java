package com.flabser.dataengine.ft;



public class FTIndexEngineException extends Exception {
	public FTIndexEngineExceptionType id;
	public String user;
	
	private static final long serialVersionUID = 4762010135613823296L;
	private String errorTextRus;
	
	
	public FTIndexEngineException(FTIndexEngineExceptionType error, String query) {
		super();
		id = error;
		switch(id){ 
		case QUERY_UNRECOGNIZED:
			errorTextRus = "FT request has not recognized (query=" + query + ")";
		case FT_ENGINE_ERROR:
			errorTextRus = "(query=" + query + ")";
		
		}		
	}
	
	public String getMessage(){
		return errorTextRus;
	}
	
	public String toString(){
		return errorTextRus;
	}
}
