package com.flabser.log;

public interface ILogger {	   
	public void normalLogEntry (String logtext);
	public void errorLogEntry (String logtext);		
	public void warningLogEntry (String logtext);
	public void verboseLogEntry (String logtext);
	public void errorLogEntry (Exception exception);
	public void fatalLogEntry (String logtext);		
	
	public void normalLogEntry (String agent, String logtext);
	public void errorLogEntry (String agent, String logtext);		
	public void errorLogEntry (String agent, String logtext, String searchKey);		
	public void warningLogEntry (String agent, String logtext);
	public void verboseLogEntry (String agent, String logtext);
	public void errorLogEntry (String agent, Exception exception);
	public void errorLogEntry (String agent, Exception exception,  String searchKey);
	public void errorLogEntry(Throwable exception);
	
}
