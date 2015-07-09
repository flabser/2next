package com.flabser.restful.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.restful.RestProvider;
import com.flabser.users.User;

@Path("/logs")
public class LogService extends RestProvider {
	private ISystemDatabase sysDatabase = DatabaseFactory.getSysDatabase();

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public LogsList get() {
		ArrayList<LogFile> fileList = new ArrayList<LogFile>();
		File logDir = new File("." + File.separator + "logs");
		if(logDir.isDirectory()){
			File[] list = logDir.listFiles();
			Arrays.sort(list, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
			for(int i = list.length; --i>=0;){
				fileList.add(new LogFile(list[i]));
			}
		}
		return  new LogsList(fileList);
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") int id) {
		System.out.println("GET " + id);
		User user = sysDatabase.getUser(id);
		return Response.ok(user).build();

	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") int id) {
		User user = sysDatabase.getUser(id);
		user.delete();
		return Response.ok().build();
	}

	@JsonRootName("logs")
	class LogsList extends ArrayList<LogFile> {
		private static final long serialVersionUID = -7008854834343193674L;
		public LogsList(Collection<? extends LogFile> m) {
			addAll(m);
		}
	}

	class LogFile {
		String name;
		long length;
		Date lastModified;

		public LogFile(File file) {
			this.name = file.getName();
			length = file.length();
			lastModified = new Date();

		}

		public String getName() {
			return name;
		}

		public long getLength() {
			return length;
		}

		public Date getLastModified() {
			return lastModified;
		}
	}
}
