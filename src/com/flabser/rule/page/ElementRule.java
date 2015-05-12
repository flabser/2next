package com.flabser.rule.page;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.w3c.dom.Node;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.Const;
import com.flabser.env.Environment;
import com.flabser.rule.RuleValue;
import com.flabser.rule.constants.QueryType;
import com.flabser.rule.constants.RunMode;
import com.flabser.rule.constants.ValueSourceType;
import com.flabser.util.XMLUtil;

import java.io.File;
import java.io.IOException;

public class ElementRule implements  Const {
	public ElementType type;
	public boolean isValid = true;
	public String name;
	public boolean hasElementName;
	public RunMode isOn;
	public String value;
	public String doClassName;
	public QueryType queryType = QueryType.UNKNOWN;
	public RuleValue query;


	private IElement parentRule;

	public ElementRule(Node node, IElement parent) {
		parentRule = parent;
		try {
			name = XMLUtil.getTextContent(node, "name", false);
			if (!name.equals(""))
				hasElementName = true;
			String mode = XMLUtil.getTextContent(node, "@mode", false);
			if (mode.equalsIgnoreCase("off")) {
				isOn = RunMode.OFF;
				return;
			}

			type = ElementType.valueOf(XMLUtil.getTextContent(node, "@type",
					true, "UNKNOWN", false));
			switch (type) {
			case STATIC_TAG:
				value = XMLUtil.getTextContent(node, "value", false);
				break;
			case SCRIPT:
				Node qoNode = XMLUtil.getNode(node, "events/doscript", false);
				doClassName = getClassName(qoNode, "doscript");
				if (doClassName == null) {
					isValid = false;
				}

				break;
			case INCLUDED_PAGE:
				value = XMLUtil.getTextContent(node, "value", false);
				break;
		
			}
		} catch (Exception e) {
			AppEnv.logger.errorLogEntry(e);
			isValid = false;
		}
	}

	public String toString() {
		return "name=\"" + name + "\", value=" + value;
	}

	private String getClassName(Node node, String normailzator){		
		ClassLoader parent = getClass().getClassLoader();

		String value = XMLUtil.getTextContent(node,".", true);
		ValueSourceType qsSourceType = ValueSourceType.valueOf(XMLUtil.getTextContent(node,"@source",true,"STATIC", true));	
		try{		
			Class<GroovyObject> process = null;
			if (qsSourceType == ValueSourceType.FILE){
				CompilerConfiguration compiler = new CompilerConfiguration();
				compiler.setTargetDirectory(Environment.libsDir);
				GroovyClassLoader loader = new GroovyClassLoader(parent, compiler); 
				File groovyFile = new File(getScriptDirPath() + File.separator + value.replace(".",File.separator) + ".groovy");
				if (groovyFile.exists()){
					try{		  
						process = loader.parseClass(groovyFile);
						return process.getName();					
					} catch (CompilationFailedException e) {
						AppEnv.logger.errorLogEntry(e);
					} catch (IOException e) {
						AppEnv.logger.errorLogEntry(e);
					}	
				}else{					 
					groovyFile = new File(parentRule.getPrimaryScriptDirPath() + File.separator + value.replace(".",File.separator) + ".groovy");
					if (groovyFile.exists()){
						try{
							process = loader.parseClass(groovyFile);
							loader.addClasspath(groovyFile.getParentFile().getAbsolutePath() + File.pathSeparator);
							return process.getName();					
						} catch (CompilationFailedException e) {
							AppEnv.logger.errorLogEntry(e);
						} catch (IOException e) {
							AppEnv.logger.errorLogEntry(e);
						}	
					}else {
					AppEnv.logger.errorLogEntry("File \"" + groovyFile.getAbsolutePath() + "\" not found");
					}
				}
			}else {
				AppEnv.logger.errorLogEntry("Included script did not implemented, form rule=" + parentRule.getID() + ", node=" + node.getBaseURI());	
											
			}	

		}catch(MultipleCompilationErrorsException e){
			AppEnv.logger.errorLogEntry("Script compilation error at form rule compiling=" + parentRule.getID() + ", node=" + node.getBaseURI());
			AppEnv.logger.errorLogEntry(e.getMessage());		
		}
		return null;
	}



	public String getScriptDirPath() {
		return parentRule.getScriptDirPath();
	}

	
	
}