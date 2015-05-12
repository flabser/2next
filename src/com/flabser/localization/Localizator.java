package com.flabser.localization;

import java.io.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.Const;
import com.flabser.rule.GlobalSetting;


public class Localizator implements Const {
	private GlobalSetting globalSetting;
	
	public Localizator(GlobalSetting globalSetting) {
		try {
			this.globalSetting = globalSetting;		
		} catch (Exception ne) {
			AppEnv.logger.errorLogEntry(ne);
		}
	}	


	public Vocabulary populate(String vocabular) throws LocalizatorException{
		String vocabuarFilePath = globalSetting.rulePath + File.separator + "Resources" + File.separator + vocabular + ".xml";
		try {
			File docFile = new File(vocabuarFilePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			Document queryDoc = db.parse(docFile.toString());
			if (queryDoc == null) {
				throw new LocalizatorException(LocalizatorExceptionType.VOCABULAR_NOT_FOUND);				
			}
			return new Vocabulary(queryDoc, vocabular, globalSetting);				
		} catch (FileNotFoundException e){
			AppEnv.logger.errorLogEntry("File not found, filepath=" + vocabuarFilePath + ", the vocabulary file has not loaded");
		} catch (ParserConfigurationException e) {		
			AppEnv.logger.errorLogEntry(e);
		} catch (IOException e) {		
			AppEnv.logger.errorLogEntry(e);
		} catch (SAXException e) {		
			AppEnv.logger.errorLogEntry(e);
		}
		return null;
	}
}
