package com.flabser.servlets;

import net.sf.saxon.s9api.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.flabser.dataengine.Const;
import com.flabser.exception.TransformatorException;
import com.flabser.exception.TransformatorExceptionType;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;


public class SaxonTransformator implements IServletTransformator, Const{

	public void toTrans(HttpServletResponse response, File xslFileObj,String xmlText) throws IOException, SaxonApiException, TransformatorException{
		XsltExecutable exp = null;
		XdmNode source = null;
		Processor proc = new Processor(false);		  
		XsltCompiler comp = proc.newXsltCompiler();

		try{
			exp = comp.compile(new StreamSource(xslFileObj));
		}catch (SaxonApiException sae) {
			throw new TransformatorException(TransformatorExceptionType.COMPILATION_ERROR_OR_FILE_DOES_NOT_EXIST, xslFileObj);
		}
		try{
			source = proc.newDocumentBuilder().build(new StreamSource(new StringReader(xmlText)));
		}catch (SaxonApiException sae) {
			throw new TransformatorException(TransformatorExceptionType.XML_CONTENT_ERROR, xmlText);
		}
		
		XsltTransformer trans = exp.load();          
		Serializer out = new Serializer();  
		ServletOutputStream sos = response.getOutputStream();
		out.setOutputStream(sos);          
		trans.setInitialContextNode(source);          
		trans.setDestination(out);   
		trans.transform();		  
		sos.close();
	}

	public String toTrans(File xslFileObj, String xmlText) throws IOException, SaxonApiException{

		Processor proc = new Processor(false);
		XsltCompiler comp = proc.newXsltCompiler();       
		XsltExecutable exp = comp.compile(new StreamSource(xslFileObj));          
		XdmNode source = proc.newDocumentBuilder().build(new StreamSource(new StringReader(xmlText)));         
		XsltTransformer trans = exp.load();          
		Serializer out = new Serializer();		
		ByteArrayOutputStream outPlace = new ByteArrayOutputStream();
		out.setOutputStream(outPlace);
		trans.setInitialContextNode(source);
		trans.setDestination(out); 
		trans.transform();		
		outPlace.close();
		return outPlace.toString();

	}


	public ArrayList<String> getFormWords(String stylesheet) {
		ArrayList<String> words = new ArrayList<String>();		
		try {			
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true); 
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(stylesheet);

			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile("//@select[contains(.,'/dictionary/')]");



			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				String w = node.getNodeValue();
				words.add(w.split("/dictionary/")[1]);
			}


		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}	
		return words;
	}

	


}
