package com.flabser.rule.page;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.io.IOException;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.w3c.dom.Node;

import com.flabser.env.Environment;
import com.flabser.rule.RuleValue;
import com.flabser.rule.constants.RunMode;
import com.flabser.rule.constants.ValueSourceType;
import com.flabser.server.Server;
import com.flabser.util.XMLUtil;

public class ElementRule {
	public ElementType type;
	public boolean isValid = true;
	public RunMode isOn;
	public String value;
	public ElementScript doClassName;
	public RuleValue query;

	private IElement parentRule;

	public ElementRule(Node node, IElement parent) {
		parentRule = parent;
		try {
			String mode = XMLUtil.getTextContent(node, "@mode", false);
			if (mode.equalsIgnoreCase("off")) {
				isOn = RunMode.OFF;
				return;
			}

			type = ElementType.valueOf(XMLUtil.getTextContent(node, "@type", true, "UNKNOWN", false));
			switch (type) {
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
			default:
				value = "Type of the Page element is not defined";
				break;

			}
		} catch (Exception e) {
			Server.logger.errorLogEntry(e);
			isValid = false;
		}
	}

	@Override
	public String toString() {
		return "value=" + value;
	}

	@SuppressWarnings({ "unchecked", "resource" })
	private ElementScript getClassName(Node node, String normailzator) {
		ClassLoader parent = getClass().getClassLoader();

		String value = XMLUtil.getTextContent(node, ".", true);
		ValueSourceType qsSourceType = ValueSourceType.valueOf(XMLUtil.getTextContent(node, "@source", true, "UNKNOWN", true));
		try {
			Class<GroovyObject> process = null;
			if (qsSourceType == ValueSourceType.GROOVY_FILE || qsSourceType == ValueSourceType.FILE) {
				CompilerConfiguration compiler = new CompilerConfiguration();
				compiler.setTargetDirectory(Environment.libsDir);
				GroovyClassLoader loader = new GroovyClassLoader(parent, compiler);
				File groovyFile = new File(getScriptDirPath() + File.separator + value.replace(".", File.separator) + ".groovy");
				if (groovyFile.exists()) {
					try {
						process = loader.parseClass(groovyFile);
						return new ElementScript(qsSourceType, process.getName());
					} catch (CompilationFailedException e) {
						Server.logger.errorLogEntry(e);
					} catch (IOException e) {
						Server.logger.errorLogEntry(e);
					}
				} else {
					groovyFile = new File(parentRule.getPrimaryScriptDirPath() + File.separator + value.replace(".", File.separator) + ".groovy");
					if (groovyFile.exists()) {
						try {
							process = loader.parseClass(groovyFile);
							loader.addClasspath(groovyFile.getParentFile().getAbsolutePath() + File.pathSeparator);
							return new ElementScript(qsSourceType, process.getName());
						} catch (CompilationFailedException e) {
							Server.logger.errorLogEntry(e);
						} catch (IOException e) {
							Server.logger.errorLogEntry(e);
						}
					} else {
						Server.logger.errorLogEntry("File \"" + groovyFile.getAbsolutePath() + "\" not found");
					}
				}
			} else if (qsSourceType == ValueSourceType.JAVA_CLASS) {
				return new ElementScript(qsSourceType, XMLUtil.getTextContent(node, ".", true));
			} else {
				Server.logger.errorLogEntry("Included script did not implemented, form rule=" + parentRule.getID() + ", node=" + node.getBaseURI());

			}

		} catch (MultipleCompilationErrorsException e) {
			Server.logger.errorLogEntry("Script compilation error at form rule compiling=" + parentRule.getID() + ", node=" + node.getBaseURI());
			Server.logger.errorLogEntry(e.getMessage());
		}
		return null;
	}

	public String getScriptDirPath() {
		return parentRule.getScriptDirPath();
	}

}