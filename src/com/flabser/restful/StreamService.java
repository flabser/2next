package com.flabser.restful;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.env.Environment;
import com.flabser.scheduler.tasks.TempFileCleaner;
import com.flabser.server.Server;
import com.flabser.users.User;

@Path("/stream")
public class StreamService extends RestProvider {

	@GET
	@Path("/{model}/{id}/{field}/{file}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getFile(@PathParam("model") String model, @PathParam("id") long id,@PathParam("field") String fieldName, @PathParam("file") String fileName) {
		File file = null;
		String fn = null;
		if (!model.equalsIgnoreCase("users")){

		}else{
			ISystemDatabase sysDb = DatabaseFactory.getSysDatabase();
			User user = sysDb.getUser(id);

			if (fieldName.equalsIgnoreCase("avatar")){
				File userTmpDir = new File(Environment.tmpDir + File.separator + user.getLogin());
				fn = userTmpDir.getAbsolutePath() + File.separator + "___" + user.getAvatar().getRealFileName();
				File fileToWriteTo = new File(fn);
				byte[] fileAsByteArray = sysDb.getUserAvatarStream(user.id);
				try {
					FileUtils.writeByteArrayToFile(fileToWriteTo, fileAsByteArray);
				} catch (IOException e) {
					Server.logger.errorLogEntry(e);
				}

				file = new File(fn);
			}
		}
		if(file != null && file.exists()){
			TempFileCleaner.addFileToDelete(fn);
			return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
					.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" )
					.build();
		}else{
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
		}
	}

}
