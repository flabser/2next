package com.flabser.scriptprocessor;

import com.flabser.script._Document;
import com.flabser.script._Tag;


public class ScriptProcessorUtil {
	public final static String packageList = "import kz.nextbase.script.*;" +	
		"import kz.nextbase.script.task.*;" +	
		"import kz.nextbase.script.project.*;" +
		"import kz.nextbase.script.mail.*;" +
		"import kz.nextbase.script.struct.*;" +
		"import kz.nextbase.script.constants.*;" +
		
		"import kz.flabs.runtimeobj.document.structure.*;" +
		"import kz.flabs.runtimeobj.document.glossary.*;" +
        "import kz.flabs.runtimeobj.document.project.*;" +
		
		"import kz.flabs.users.User;" +
		
		"import net.sf.jasperreports.engine.*;" +
		"import net.sf.jasperreports.engine.export.*;" +
		"import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;" +
        "import net.sf.jasperreports.engine.data.JsonDataSource;" +
        "import net.sf.jasperreports.engine.fill.JRFileVirtualizer;" +
        "import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;" +

        "import net.sf.jasperreports.engine.design.JRDesignStyle;" +
        "import net.sf.jasperreports.engine.export.JExcelApiExporter;" +
        "import net.sf.jasperreports.engine.export.JRHtmlExporter;" +
        "import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;" +

		"import java.text.*;" +
		"import java.util.*;" +
		"import java.sql.*;" +
		
		"import kz.flabs.dataengine.Const;" +
		"import kz.flabs.dataengine.IDatabase;" +
		"import kz.flabs.runtimeobj.document.task.Task;" +
		"import kz.pchelka.env.Environment;" +
		"import kz.flabs.dataengine.h2.SystemDatabase;";
		
	
/*	public static _Document getScriptingDocument(BaseDocument d, String userID){
		if (d instanceof Project){			
			_Project doc = new _Project((Project)d, userID);
			return doc;
		}else if (d instanceof Task){
			_Task doc = new _Task((Task)d, userID);
			return doc;
		}else{
			return new _Document(d);
		}		
	}
	
	public static _Document getScriptingDocument(BaseDocument d, _Session ses){
		if (d instanceof Project){			
			_Project doc = new _Project((Project)d, ses);
			return doc;
		}else if (d instanceof Task){
			_Task doc = new _Task((Task)d, ses);
			return doc;
		} else if (d instanceof Execution) {
			_Execution doc = new _Execution((Execution)d, ses);
			return doc;
		} else if (d instanceof Glossary) {
			_Glossary doc = new _Glossary((Glossary)d, ses);
			return doc;
		}else{
			return new _Document(d, ses);
		}		
	}*/
	
	public static _Tag getScriptError(StackTraceElement stack[]){	
		_Tag tag = new _Tag("stack","");
		for (int i=0; i<stack.length; i++){
			tag.addTag("entry", stack[i].getClassName()+"(" + stack[i].getMethodName() + ":" + Integer.toString(stack[i].getLineNumber()) + ")");			
		}
		return tag;
	}
	
	public static String getGroovyError(StackTraceElement stack[]){		
		for (int i=0; i<stack.length; i++){
			if (stack[i].getClassName().contains("Foo")){
				return stack[i].getMethodName()+", > "+Integer.toString(stack[i].getLineNumber() - 3) + "\n";	
			}
		}
		return "";
	}
	
}
