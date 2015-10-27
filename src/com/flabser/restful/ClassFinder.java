package com.flabser.restful;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.flabser.server.Server;

public class ClassFinder {
	private static final String DOT = ".";
	private static final String CLASS_SUFFIX = ".class";
	private static final String INNER_CLASS_SPLITTER = "$";

	public static List<Class<?>> find(String scannedPackage) {
		String scannedPath = scannedPackage.replace(DOT, File.separator);
		Server.logger.debugLogEntry("scan REST handlers " + scannedPath + "...");
		List<Class<?>> classes = new ArrayList<Class<?>>();
		URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
		if (scannedUrl != null) {
			if (scannedUrl != null) {
				File scannedDir = new File(scannedUrl.getFile().replace("%5c", File.separator));
				for (File file : scannedDir.listFiles()) {
					classes.addAll(find(file, scannedPackage));
				}
			}
		}
		return classes;
	}

	private static List<Class<?>> find(File file, String scannedPackage) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		String resource = scannedPackage + DOT + file.getName();
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				classes.addAll(find(child, resource));
			}
		} else if (resource.endsWith(CLASS_SUFFIX) && resource.indexOf(INNER_CLASS_SPLITTER) == -1) {
			int endIndex = resource.length() - CLASS_SUFFIX.length();
			String className = resource.substring(0, endIndex);
			try {
				Server.logger.debugLogEntry("register REST handler \"" + className + "\"");
				classes.add(Class.forName(className));
			} catch (ClassNotFoundException ignore) {
			}
		}
		return classes;
	}

}