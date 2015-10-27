package com.flabser.restful;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.flabser.env.EnvConst;
import com.flabser.server.Server;

public class ClassFinder {
	private static final String DOT = ".";
	private static final String CLASS_SUFFIX = ".class";
	private static final String INNER_CLASS_SPLITTER = "$";
	private List<Class<?>> classes = new ArrayList<Class<?>>();

	public List<Class<?>> find(String scannedPackage) {
		ZipInputStream zip = null;
		String scannedPath = scannedPackage.replace(DOT, File.separator);
		try {

			File jarFile = new File(EnvConst.REST_HANDLERS_JAR_FILE);
			if (jarFile.exists()) {
				zip = new ZipInputStream(new FileInputStream(EnvConst.REST_HANDLERS_JAR_FILE));
				for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
					String resource = entry.getName().replace("/", ".");
					if (!entry.isDirectory() && resource.startsWith(scannedPackage)) {
						load(resource);
					}
				}
			} else {
				URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
				if (scannedUrl != null) {
					File scannedDir = new File(scannedUrl.getFile().replace("%5c", File.separator));
					for (File file : scannedDir.listFiles()) {
						String resource = scannedPackage + DOT + file.getName();
						load(resource);
					}

				}
			}
		} catch (IOException e) {
			Server.logger.errorLogEntry(e);
		} finally {
			try {
				if (zip != null) {
					zip.close();
				}
			} catch (IOException e) {
				Server.logger.errorLogEntry(e);
			}
		}

		return classes;
	}

	private void load(String resource) {
		if (resource.endsWith(CLASS_SUFFIX) && resource.indexOf(INNER_CLASS_SPLITTER) == -1) {
			int endIndex = resource.length() - CLASS_SUFFIX.length();
			String className = resource.substring(0, endIndex);
			try {
				Server.logger.debugLogEntry("register REST handler \"" + className + "\"");
				classes.add(Class.forName(className));
			} catch (ClassNotFoundException ignore) {
			}
		}
	}

}