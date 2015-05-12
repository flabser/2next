package com.flabser.servlets.sitefiles;

import org.apache.commons.io.FilenameUtils;

import com.flabser.appenv.AppEnv;
import com.flabser.env.Environment;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLEncoder;

public class AttachmentHandler{
	private HttpServletRequest request;
	private HttpServletResponse response;
	private boolean deleteOnPublish;
	
	public AttachmentHandler(HttpServletRequest request, HttpServletResponse response,boolean deleteOnPublish){
		this.request = request;
		this.response = response;
		this.deleteOnPublish = deleteOnPublish;
	}
	
	public  void publish(String filePath, String fileName, String disposition) throws AttachmentHandlerException{
		ServletOutputStream outStream = null;
		BufferedInputStream buf = null;
		File file = null;

		try{
			file = new File(filePath);			
			String fileType = FilenameUtils.getExtension(fileName);			

			String userAgent = request.getHeader("USER-AGENT").toLowerCase();
			fileName = URLEncoder.encode(fileName, "UTF8");
			if (userAgent != null && (userAgent.indexOf("opera") == -1 && userAgent.indexOf("msie") != -1) || userAgent.indexOf("chrome") != -1 ) {
				response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
			} else {
				response.setHeader("Content-Disposition", "attachment; filename*=\"utf-8'" + fileName + "\"" );
			}

			response.setContentType(Environment.mimeHash.get(fileType));
			response.setContentLength((int)file.length());
			FileInputStream inStream = new FileInputStream(file);
			buf = new BufferedInputStream(inStream);
			//Reader reader = new InputStreamReader(buf, "Cp1251");
			outStream = response.getOutputStream();
			int readBytes = 0;


			while((readBytes = buf.read()) != -1){
				outStream.write(readBytes);
			}
		}catch(FileNotFoundException e){
			throw new AttachmentHandlerException("File not found");
		}catch(IOException ioe){
			throw new AttachmentHandlerException("");
		}finally{
			try{
				if (outStream != null) {
					outStream.flush();
					outStream.close();					
				}
				if (buf != null) {
					buf.close();
				}
				//if (file != null)file.delete();
			}catch(Exception e){
				AppEnv.logger.errorLogEntry(e);
			}
			if (deleteOnPublish)Environment.fileToDelete.add(filePath);
		}
	}


}
