package com.flabser.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public final class ReadVersion {

	public void readVersionInfoInManifest(String libName, Class<?> clazz) throws MalformedURLException, IOException {
		String classPath = clazz.getResource(clazz.getSimpleName() + ".class").toString();
		String libPath = classPath.substring(0, classPath.lastIndexOf("!"));
		String filePath = libPath + "!/META-INF/MANIFEST.MF";
		Manifest manifest = new Manifest(new URL(filePath).openStream());
		Attributes attr = manifest.getMainAttributes();
		System.out.println(libName + ": " + attr.getValue("Implementation-Version"));
	}
}