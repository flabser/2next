package com.flabser.exception;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.Const;
import com.flabser.server.Server;
import com.flabser.servlets.ProviderExceptionType;
import com.flabser.servlets.PublishAsType;
import com.flabser.util.XMLUtil;

import java.io.*;



public class PortalException extends Exception implements Const{	
	private Enum type = ProviderExceptionType.INTERNAL;
	private AppEnv env;
	private String currentSkin = "";

	private static final long serialVersionUID = 3214292820186296427L;
	private Source xsltSource;
	
	
	public PortalException(Exception e, HttpServletResponse response, ProviderExceptionType type, PublishAsType publishAs){
		super(e);
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setContentType("text/xml;charset=utf-8");
		this.type = type;
		xsltSource =	new StreamSource(new File("xsl" + File.separator + "error.xsl"));
		message(errorMessage(e), response, publishAs);
	}
	
	public PortalException(Exception e, AppEnv env, HttpServletResponse response, PublishAsType publishAs, String defaultSkin){
		super(e);
		this.env = env;
		if (defaultSkin != null) currentSkin = defaultSkin;
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setContentType("text/xml;charset=utf-8");
		xsltSource = getXSLT();
		message(errorMessage(e), response, publishAs);
	}

	public PortalException(String text,Exception e, AppEnv env, HttpServletResponse response,  ProviderExceptionType type, PublishAsType publishAs, String defaultSkin){
		super(e);
		this.env = env;
		if (defaultSkin != null) currentSkin = defaultSkin;
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setContentType("text/xml;charset=utf-8");
		this.type = type;
		xsltSource = getXSLT();
		message("<errorcontex>" + text + "</errorcontext>" + errorMessage(e), response, publishAs);
	}

	public PortalException(Exception e, AppEnv env, HttpServletResponse response,  Enum type){
		super(e);
		this.env = env;		
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setContentType("text/xml;charset=utf-8");
		this.type = type;
		message(errorMessage(e), response, PublishAsType.XML);
	}

	public PortalException(Exception e, AppEnv env, HttpServletResponse response,  Enum type, PublishAsType publishAs, String defaultSkin){
		super(e);
		this.env = env;	
		if (defaultSkin != null) currentSkin = defaultSkin;
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setContentType("text/xml;charset=utf-8");
		this.type = type;
		xsltSource = getXSLT();
		message(errorMessage(e), response, publishAs);
	}


	public PortalException(String text,AppEnv env, HttpServletResponse response, ProviderExceptionType type, PublishAsType publishAs, String defaultSkin){
		super(text);
		this.env = env;
		if (defaultSkin != null) currentSkin = defaultSkin;
		this.type = type;
		xsltSource = getXSLT();
		message(text, response, publishAs);	
	}


	private void message(String text, HttpServletResponse response, PublishAsType publishAs){
		ServletOutputStream out;
		String xmlText;
		Server.logger.errorLogEntry(text);
		try{

			xmlText = "<?xml version = \"1.0\" encoding=\"utf-8\"?><request><error type=\"" + type +"\">" +
					"<message><version>" +  Server.serverVersion + "</version><errortext>" + XMLUtil.getAsTagValue(text) + "</errortext></message></error></request>";
			//		System.out.println("xml text = "+xmlText);
			response.setHeader("Cache-Control", "no-cache, must-revalidate, private, no-store, s-maxage=0, max-age=0");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);

			if (publishAs == PublishAsType.HTML){
				response.setContentType("text/html;charset=utf-8");				
				out = response.getOutputStream();					
				Source xmlSource = new StreamSource(new StringReader(xmlText));				
				Result result =	new StreamResult(out);
				TransformerFactory transFact = TransformerFactory.newInstance( );
				Transformer trans = transFact.newTransformer(xsltSource);
				//System.out.println(PortalEnv.appID+": xsl transformation="+PortalEnv.errorXSL);
				trans.transform(xmlSource, result);
			}else{
				response.setContentType("text/xml;charset=utf-8");
				//response.sendError(550);
				out = response.getOutputStream();
				out.println(xmlText);
			}
		}catch(IOException ioe){
			System.out.println(ioe);
			ioe.printStackTrace();
		}catch(TransformerConfigurationException tce){
			System.out.println(tce);
			tce.printStackTrace();
		}catch(TransformerException te){
			System.out.println(te);
			te.printStackTrace();
		}
	}

	public static String errorMessage (Exception exception){
		String message = "";
		try{
			String addErrorMessage = getErrorStackString(exception.getStackTrace());

			message = exception.toString();				

			return "<errortext>" + message + "</errortext>".replaceAll("\"","'")+"<stack>" + addErrorMessage.replaceAll(">","-").replaceAll("<","-")+"</stack>\n\r";
		}catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return "";	
		}
	} 

	public static String getErrorStackString(StackTraceElement stack[]){
		String addErrorMessage = "";
		for (int i=0; i<stack.length; i++){
			addErrorMessage = addErrorMessage + "\n" +stack[i].getClassName()+" > "+stack[i].getMethodName()+" "+Integer.toString(stack[i].getLineNumber())+"\n";
		}
		return addErrorMessage;
	}
	
	private Source getXSLT(){
		Source xsltSource = null;
		if (env.appType.equalsIgnoreCase("administrator")){
			xsltSource =	new StreamSource(new File("xsl" + File.separator + "error.xsl"));
		}else{
			xsltSource =	new StreamSource(new File("xsl" + File.separator + "error.xsl"));
			/*Skin skin = env.globalSetting.skinsMap.get(currentSkin);
			if (skin == null){
				skin = env.globalSetting.defaultSkin;
			}
			xsltSource = new StreamSource(new File(skin.errorPagePath));*/
		}
		return xsltSource;
	}
}	

