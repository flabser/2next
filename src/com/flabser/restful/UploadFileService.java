package com.flabser.restful;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.flabser.env.Environment;
import com.flabser.users.UserSession;
import com.flabser.util.Util;

@Path("/file")
public class UploadFileService extends RestProvider {

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		UserSession us = getUserSession();
		String login = us.currentUser.getLogin();

		File userTmpDir = new File(Environment.tmpDir + File.separator + login);
		if (!userTmpDir.exists()) {
			userTmpDir.mkdir();
		}

		String tempFileName = Util.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890", 32);
		String uploadedFileLocation = userTmpDir + File.separator + tempFileName;

		writeToFile(uploadedInputStream, uploadedFileLocation);



		return Response.status(200).entity(tempFileName).build();

	}

	private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {

		try {
			OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
			out = null;
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

}
