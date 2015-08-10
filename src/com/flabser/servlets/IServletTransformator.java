package com.flabser.servlets;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import net.sf.saxon.s9api.SaxonApiException;

import com.flabser.exception.TransformatorException;

public interface IServletTransformator {
	public void toTrans(HttpServletResponse response, File xslFileObj, String xmlText) throws IOException, SaxonApiException,
			TransformatorException;

}
