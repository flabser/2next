package com.flabser.scriptprocessor;

import org.codehaus.groovy.control.MultipleCompilationErrorsException;

import com.flabser.log.ILogger;
import com.flabser.log.Log4jLogger;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

public class ScriptProcessor implements IScriptProcessor{
	public static ILogger logger = new Log4jLogger("ScriptProcessor");
	
	public String[] processString(String script) {
		logger.errorLogEntry("method 4563 has not reloaded");
		return null;
	}	

	@Override
	public String process(String script) {	
		logger.errorLogEntry("method 4564 has not reloaded");
		return "";
	}
	
	@Override
	public String[] processString(Class<GroovyObject> compiledClass) {
		logger.errorLogEntry("method 4565 has not reloaded");
		return null;
	}

	
		
	public IScriptSource setScriptLauncher(String userScript, boolean debug){
		GroovyObject groovyObject = null;
		String script = "";
		if (!debug){
			script = normalizeScript(userScript);
		}else{
			script = normalizeDebugScript(userScript);
		}
		ClassLoader parent = getClass().getClassLoader();
		GroovyClassLoader loader = new GroovyClassLoader(parent);
		Class<GroovyObject> groovyClass = null;
		try{
			groovyClass = loader.parseClass(script);
			
			try {				
				groovyObject = groovyClass.newInstance();
			} catch (InstantiationException e) {
				if (!debug)	logger.errorLogEntry(e);
			} catch (IllegalAccessException e) {
				if (!debug)	logger.errorLogEntry(e);
			}
			
			IScriptSource sciptObject = (IScriptSource) groovyObject;		
			return sciptObject;
			
		}catch(MultipleCompilationErrorsException mcee){
//			logger.errorLogEntry(script);
			if (!debug)	logger.errorLogEntry(mcee.getMessage());
			return new ScriptSource();
		}
	}

	
	public static String normalizeScript(String script) {
		String beforeScript = 
							
			"import java.io.File;" +
					
			"import java.io.BufferedReader;" +
			"import java.io.FileNotFoundException;" +
			"import java.io.FileReader;" +
			"import java.io.IOException;" +
			"import java.util.ArrayList;" +
			"import java.util.Random;" +
			"import java.util.HashSet;" +
			"import java.util.Calendar;" +
			"import java.util.Date;" +
			"import java.util.Random;" +	
			"import java.sql.Connection;" +
			"import java.sql.Statement;" +
			"import java.sql.PreparedStatement;" +
			"import java.sql.ResultSet;" +
			"import java.sql.Timestamp;" +
			"import java.sql.*;" +
			"import kz.flabs.dataengine.Const;" +
			"import kz.flabs.util.*;" +	
			ScriptProcessorUtil.packageList +
			"import kz.flabs.scriptprocessor.*;" +	
			"import kz.flabs.runtimeobj.document.task.TaskType;" + 
			"import jxl.*;" +
			"import jxl.format.*;" +
			"import jxl.write.*;" +
			"import org.apache.xml.serialize.*;"+
			"import org.w3c.dom.*;" +
			"import javax.xml.parsers.DocumentBuilderFactory;" +
			"import javax.xml.parsers.DocumentBuilder;" +
			"import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;" +
			"import org.apache.xerces.jaxp.DocumentBuilderImpl;" +
            "import com.itextpdf.text.DocumentException;" +
            "import com.itextpdf.text.pdf.PdfReader;" +
            "import com.itextpdf.text.pdf.PdfStamper;" +
            "import java.io.FileOutputStream;" +
            "import java.io.IOException;" +
			"class Foo extends ScriptSource{";
		String afterScript = "}";
		return  beforeScript + script + afterScript;		
	}

	public static String normalizeDebugScript(String script) {
        String beforeScript = "import java.io.File;" +
                "import java.io.BufferedReader;" +
                "import java.io.FileNotFoundException;" +
                "import java.io.FileReader;" +
                "import java.io.IOException;" +
                "import java.util.ArrayList;" +
                "import java.util.Random;" +
                "import java.util.HashSet;" +
                "import java.util.Calendar;" +
                "import java.util.Date;" +
                "import java.util.Random;" +
                "import java.sql.Connection;" +
                "import java.sql.Statement;" +
                "import java.sql.PreparedStatement;" +
                "import java.sql.ResultSet;" +
                "import java.sql.Timestamp;" +
                "import kz.flabs.dataengine.Const;" +
                "import kz.flabs.util.*;" +
                ScriptProcessorUtil.packageList +
                "import kz.flabs.scriptprocessor.*;" +
                "import kz.flabs.runtimeobj.document.task.TaskType;" +
                "import jxl.*;" +
                "import jxl.format.*;" +
                "import jxl.write.*;" +
                "import org.apache.xml.serialize.*;" +
                "import org.w3c.dom.*;" +
                "import javax.xml.parsers.DocumentBuilderFactory;" +
                "import javax.xml.parsers.DocumentBuilder;" +
                "import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;" +
                "import org.apache.xerces.jaxp.DocumentBuilderImpl;" +
                "import com.itextpdf.text.DocumentException;" +
                "import com.itextpdf.text.pdf.PdfReader;" +
                "import com.itextpdf.text.pdf.PdfStamper;" +
                "import java.io.FileOutputStream;" +
                "import java.io.IOException;" +
                "class Foo extends DebugScriptSource{";
        String afterScript = "}";
        return  beforeScript + script + afterScript;
	}
	
	public String toString(){
		return "ScriptProcessorType=" + ScriptProcessorType.UNDEFINED;
	}

}
