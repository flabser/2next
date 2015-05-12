package com.flabser.servlets.sitefiles;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import com.flabser.dataengine.IDatabase;
import com.flabser.users.UserSession;
import com.flabser.util.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RequestWrapper extends HttpServletRequestWrapper{
	public ArrayList<UploadedFile> uploadedFiles = new ArrayList<>();
	public String formSesID = "";
	public boolean extInfo;

    private boolean richTextUpload = false;
	private File tempDir;
	private Integer maxSizeInMemory = 2000;


	DiskFileItemFactory factory = new DiskFileItemFactory(maxSizeInMemory, tempDir);

	public RequestWrapper(HttpServletRequest hreq) throws AttachmentHandlerException, FileUploadException, UnsupportedEncodingException{
		super(hreq);		
		String fieldName = "";
		final String usedCharset = hreq.getCharacterEncoding();

        ServletFileUpload upload = new ServletFileUpload(factory);
		//--------Add this code for progress bar------------
		// set file upload progress listener
		FileUploadListener listener = new FileUploadListener();

		HttpSession session = hreq.getSession();

		session.setAttribute("LISTENER", listener);

		// upload servlet allows to set upload listener
		upload.setProgressListener(listener);
		//--------------------------------------------------


		List<FileItem> items = upload.parseRequest(hreq);
		Iterator<FileItem> iter = items.iterator();

		while (iter.hasNext()) {
			FileItem item = iter.next();
			if (item.isFormField()) {
				if (item.getFieldName().equalsIgnoreCase("type")){
                    richTextUpload = item.getString(usedCharset).equalsIgnoreCase("richtextupload");
				}else if(item.getFieldName().equalsIgnoreCase("formsesid")){
					formSesID = item.getString(usedCharset);
				}else if(item.getFieldName().equalsIgnoreCase("info")){
					if(item.getString().equalsIgnoreCase("1")){
						extInfo = true;
					}
				}
			}else{
				System.out.println("UPLOADER field : " + item);
			}
		}

        UserSession userSession = (UserSession)session.getAttribute("usersession");
        IDatabase db = userSession.getDataBase();
      /*  try {
    //        uploadedFiles = db.insertBlobTables(items);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
	}

	public UploadedFile[] getFilesArray(){
		UploadedFile[] files = new UploadedFile[uploadedFiles.size()];
		for (int i = 0; i < files.length; i++){
			files[i] = uploadedFiles.get(i);
		}
		return files;
	}

    public boolean isRichTextUpload() {
        return richTextUpload;
    }

	private UploadedFile parseFile(FileItem item, String tmpFolder) throws Exception {
		String name = item.getName();
		long filelen = item.getSize();

		if (name != null && (name.length() > 0)) {			
			name = FilenameUtils.getName(name);

            String tmpFileName = Util.getFileName(name, tmpFolder);
            File tempFile = new File(tmpFileName);
			item.write(tempFile);
			return new UploadedFile(tempFile.getName(), filelen, tempFile.getAbsolutePath(), item.getContentType());
		}else{
			return null;
		}
	}	
}
