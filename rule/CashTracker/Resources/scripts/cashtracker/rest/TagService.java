package cashtracker.rest;

import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cashtracker.dao.TagDAO;
import cashtracker.model.Tag;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;


@Path("tags")
public class TagService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public TagsResponse get() {
		TagDAO dao = new TagDAO(getSession());
		return new TagsResponse(dao.findAll());
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Tag get(@PathParam("id") long id) {
		TagDAO dao = new TagDAO(getSession());
		Tag m = dao.findById(id);
		return m;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Tag create(Tag m) {
		TagDAO dao = new TagDAO(getSession());
		m.setId(dao.addTag(m));
		return m;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Tag update(@PathParam("id") long id, Tag m) {

		m.setId(id);
		TagDAO dao = new TagDAO(getSession());
		dao.updateTag(m);
		return m;
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {
		TagDAO dao = new TagDAO(getSession());
		Tag m = dao.findById(id);
		if (m != null) {
			dao.deleteTag(m);
		}
		return Response.ok().build();
	}

	@JsonRootName("tags")
	class TagsResponse extends ArrayList <Tag> {

		private static final long serialVersionUID = 1L;

		public TagsResponse(Collection <? extends Tag> m) {
			addAll(m);
		}
	}
}
