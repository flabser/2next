package com.flabser.localization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.flabser.server.Server;

public class Localizator {

	public Vocabulary populate(String vocabuarFilePath) throws LocalizatorException {
		try {
			File docFile = new File(vocabuarFilePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			Document queryDoc = db.parse(docFile.toString());
			if (queryDoc == null) {
				throw new LocalizatorException(LocalizatorExceptionType.VOCABULAR_NOT_FOUND);
			}
			return new Vocabulary(queryDoc);
		} catch (FileNotFoundException e) {
			Server.logger.errorLogEntry("file not found, filepath=" + vocabuarFilePath + ", the vocabulary file has not loaded");
		} catch (ParserConfigurationException e) {
			Server.logger.errorLogEntry(e);
		} catch (IOException e) {
			Server.logger.errorLogEntry(e);
		} catch (SAXException e) {
			Server.logger.errorLogEntry(e);
		}
		return null;
	}
}
