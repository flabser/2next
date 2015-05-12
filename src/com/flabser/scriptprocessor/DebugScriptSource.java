package com.flabser.scriptprocessor;


public class DebugScriptSource extends ScriptSource implements IScriptSource{	
	private String consoleOutput = "";
	
	public String print(String val){
		consoleOutput += val;
		return val;
	}
	
	public String println(String val){
		consoleOutput += "<entry>" + val + "</entry>";
		return val;
	}

	@Override
	public String getConsoleOutput() {
		return consoleOutput;
	}

}
