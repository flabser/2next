package com.flabser.servlets;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.flabser.dataengine.Const;
import com.flabser.servlets.sitefiles.FileUploadListener;
import com.flabser.servlets.sitefiles.RequestWrapper;
import com.flabser.servlets.sitefiles.UploadedFile;
import com.flabser.util.ResponseType;
import com.flabser.util.XMLResponse;
import com.flabser.util.XMLUtil;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;


public class Uploader extends HttpServlet implements Const, Servlet {
    private static final long serialVersionUID = -5486823536445378485L;

    public Uploader() {
        super();
    }

    /**
     * Add doGet for implements progress bar
     */

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.addHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
        // Установить стандартные HTTP/1.1 no-cache заголовки.
        response.addHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Установить IE расширенный HTTP/1.1 no-cache заголовок (используя addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Установить стандартный HTTP/1.0 no-cache заголовок.
        response.addHeader("Pragma", "no-cache");
        HttpSession session = request.getSession();
        FileUploadListener listener = null;
        StringBuffer buffy = new StringBuffer();
        long bytesRead = 0, contentLength = 0;

        // Make sure the session has started
        if (session == null) {
            return;
        } else if (session != null) {
            // Check to see if we've created the listener object yet
            listener = (FileUploadListener) session.getAttribute("LISTENER");

            if (listener == null) {
                return;
            } else {
                // Get the meta information
                bytesRead = listener.getBytesRead();
                contentLength = listener.getContentLength();
            }
        }

	    	/*
	    	 * XML Response Code
	    	 */
        response.setContentType("text/xml");

        buffy.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        buffy.append("<response>\n");
        buffy.append("\t<bytes_read>" + bytesRead + "</bytes_read>\n");
        buffy.append("\t<content_length>" + contentLength + "</content_length>\n");

        // Check to see if we're done
		    if (bytesRead == contentLength)
		    {
			    buffy.append("\t<finished />\n");

            // No reason to keep listener in session since we're done
            session.setAttribute("LISTENER", null);
        } else {
            // Calculate the percent complete
            long percentComplete = ((100 * bytesRead) / contentLength);

            buffy.append("\t<percent_complete>" + percentComplete + "</percent_complete>\n");
        }

        buffy.append("</response>\n");

        out.println(buffy.toString());
        out.flush();
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestWrapper wrappedReq = null;
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        XMLResponse resp = new XMLResponse(ResponseType.UPLOAD_FILE);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (isMultipart) {
            try {
                request.setCharacterEncoding("utf-8");
                wrappedReq = new RequestWrapper(request);
                UploadedFile[] newFilesObj = wrappedReq.getFilesArray();

                resp.setResponseStatus(true);

                if (!wrappedReq.isRichTextUpload()) {
                    for (int i = 0; i < newFilesObj.length; i++) {
                        resp.setFormSesID(wrappedReq.formSesID);
                        UploadedFile uf = newFilesObj[i];
                        if (uf != null) {
                            resp.addMessage(uf.originalName);
                            resp.addMessage(uf.commentField);
                            resp.addMessage(uf.localPath);
                            resp.addMessage(uf.id);
                            if (wrappedReq.extInfo) {
                                resp.addMessage(uf.fullName);
                            }
                        } else {
                            resp.setResponseStatus(false);
                            resp.setMessage("error: file name has not provided");
                        }
                    }
                    response.setContentType("text/xml;charset=utf-8");
                } else {
                    for (int i = 0; i < newFilesObj.length; i++) {
                        UploadedFile uf = newFilesObj[i];
                        String funcNum = request.getParameter("CKEditorFuncNum");
                        if (uf != null) {
                            resp.addScript("text/javascript", "window.parent.CKEDITOR.tools.callFunction(" + funcNum + ", 'Provider?type=getattach&formsesid=" + wrappedReq.formSesID + "&doctype=896&key=" + uf.id + "&field=rtfcontent&id=rtfcontent&file=" + XMLUtil.getAsTagValue(uf.originalName) + "', '');");
                        }
                    }
                    response.setContentType("text/html;charset=utf-8");
                }
            } catch (Exception e) {
                e.printStackTrace();
                resp.setResponseStatus(false);
                resp.setMessage(e.getMessage());
            }
            response.setCharacterEncoding("utf-8");

            response.getWriter().println(resp.toXML());
        }
    }


}
