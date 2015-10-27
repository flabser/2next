package com.flabser.log;

import java.io.File;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;


public class Log4jLogger implements ILogger {
	public org.apache.log4j.Logger log4jLogger;

	public Log4jLogger(String module) {
		log4jLogger = org.apache.log4j.Logger.getLogger(module);

	}

	@Override
	public void errorLogEntry(String logtext) {
		log4jLogger.error(logtext);
	}

	@Override
	public void fatalLogEntry(String logtext) {
		log4jLogger.fatal(logtext);
	}

	@Override
	public void errorLogEntry(Exception exception) {
		log4jLogger.error(exception);
		exception.printStackTrace();
	}

	@Override
	public void errorLogEntry(String agent, String logtext) {
		log4jLogger.error(agent + "-" + logtext);
	}

	@Override
	public void errorLogEntry(String agent, String logtext, String searchKey) {
		log4jLogger.error(agent + "-" + logtext + " searchKey=" + searchKey);
	}

	@Override
	public void errorLogEntry(String agent, Exception exception) {
		String msg = agent + "-" + exception.toString();
		msg += getErrorStackString(exception.getStackTrace());
		log4jLogger.error(msg, exception);
	}

	@Override
	public void errorLogEntry(String agent, Exception exception, String searchKey) {
		String msg = exception.toString();
		msg += getErrorStackString(exception.getStackTrace());
		log4jLogger.error(agent + " searchKey=" + searchKey + " " + msg, exception);

	}

	@Override
	public void infoLogEntry(String logtext) {
		log4jLogger.info(logtext);

	}

	@Override
	public void infoLogEntry(String agent, String logtext) {
		log4jLogger.info(agent + "-" + logtext);


	}

	@Override
	public void debugLogEntry(String logtext) {

		log4jLogger.debug(logtext);

	}

	@Override
	public void debugLogEntry(String agent, String logtext) {

		log4jLogger.debug(agent + "-" + logtext);

	}

	@Override
	public void warningLogEntry(String agent, String logtext) {
		log4jLogger.info(agent + "-" + logtext);
	}

	@Override
	public void warningLogEntry(String logtext) {
		log4jLogger.warn(logtext);

	}

	public boolean getLoggingMode() {
		return false;
	}

	public static String getErrorStackString(StackTraceElement stack[]) {
		StringBuffer addErrorMessage = new StringBuffer(1000);
		for (int i = 0; i < stack.length; i++) {
			addErrorMessage.append("\n" + stack[i].getClassName()
					+ " > " + stack[i].getMethodName() + " "
					+ Integer.toString(stack[i].getLineNumber()) + "\n");
		}

		return addErrorMessage.toString();
	}

	public String getBuildDateTime(){
		String value = "";
		JarFile jarFile = null;
		try{
			String pathToJar = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			if (pathToJar.contains("jar")) {
				jarFile = new JarFile(new File(pathToJar));
				Manifest entry = jarFile.getManifest();
				Attributes attrs = entry.getMainAttributes();
				value = attrs.getValue("Built-Date");
			}
		}catch(Exception e){
			errorLogEntry(getClass().getName(),e);
		}finally{
			if (jarFile != null ) {
				try {
					jarFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	@Override
	public void errorLogEntry(Throwable exception) {
		log4jLogger.error(exception);
		exception.printStackTrace();

	}
}
