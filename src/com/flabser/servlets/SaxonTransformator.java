package com.flabser.servlets;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

import com.flabser.exception.TransformatorException;
import com.flabser.exception.TransformatorExceptionType;

public class SaxonTransformator implements IServletTransformator {

	@Override
	public void toTrans(HttpServletResponse response, File xslFileObj, String xmlText) throws IOException, SaxonApiException,
			TransformatorException {
		XsltExecutable exp = null;
		XdmNode source = null;
		Processor proc = new Processor(false);
		XsltCompiler comp = proc.newXsltCompiler();

		try {
			exp = comp.compile(new StreamSource(xslFileObj));
		} catch (SaxonApiException sae) {
			throw new TransformatorException(TransformatorExceptionType.COMPILATION_ERROR_OR_FILE_DOES_NOT_EXIST, xslFileObj);
		}
		try {
			source = proc.newDocumentBuilder().build(new StreamSource(new StringReader(xmlText)));
		} catch (SaxonApiException sae) {
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

	public String toTrans(File xslFileObj, String xmlText) throws IOException, SaxonApiException {
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

}
