package cashtracker.rest;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cashtracker.dao.TagDAO;
import cashtracker.model.Tag;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;
import com.flabser.script._Session;
import com.flabser.users.UserSession;


@Path("tags")
public class TagService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public TagsResponse get() {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		TagDAO dao = new TagDAO(new _Session(getAppEnv(), userSession));
		return new TagsResponse(dao.findAll());
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Tag get(@PathParam("id") long id) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		TagDAO dao = new TagDAO(new _Session(getAppEnv(), userSession));
		Tag m = dao.findById(id);
		return m;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Tag create(Tag m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		TagDAO dao = new TagDAO(new _Session(getAppEnv(), userSession));
		m.setId(dao.addTag(m));
		return m;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Tag update(@PathParam("id") long id, Tag m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		m.setId(id);
		TagDAO dao = new TagDAO(new _Session(getAppEnv(), userSession));
		dao.updateTag(m);
		return m;
	}

	@JsonRootName("tags")
	class TagsResponse extends ArrayList <Tag> {

		private static final long serialVersionUID = 1L;

		public TagsResponse(Collection <? extends Tag> m) {
			addAll(m);
		}
	}
}
