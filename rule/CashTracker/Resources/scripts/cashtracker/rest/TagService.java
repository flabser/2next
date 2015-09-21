package cashtracker.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cashtracker.dao.TagDAO;
import cashtracker.helper.PageRequest;
import cashtracker.model.Errors;
import cashtracker.model.Tag;
import cashtracker.validation.TagValidator;
import cashtracker.validation.ValidationError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flabser.restful.RestProvider;


@Path("tags")
public class TagService extends RestProvider {

	private TagValidator validator = new TagValidator();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("page") int page, @QueryParam("limit") int limit) {
		TagDAO dao = new TagDAO(getSession());

		PageRequest pr = new PageRequest(page * limit, limit, "", "");
		List <Tag> list = dao.findAll(pr);
		_Response resp = new _Response("success", list, new Meta(list.size(), -1, -1));

		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
		om.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

		try {
			return Response.ok(om.writeValueAsString(resp)).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		TagDAO dao = new TagDAO(getSession());
		Tag m = dao.findById(id);
		return Response.ok(m).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Tag m) {
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		TagDAO dao = new TagDAO(getSession());
		return Response.ok(dao.add(m)).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, Tag m) {
		m.setId(id);

		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		TagDAO dao = new TagDAO(getSession());
		//
		Tag pm = dao.findById(id);
		pm.setName(m.getName());
		//
		return Response.ok(dao.update(pm)).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {
		TagDAO dao = new TagDAO(getSession());
		Tag m = dao.findById(id);
		if (m != null) {
			if (dao.existsTransactionByTag(m)) {
				Errors msg = new Errors();
				msg.setMessage("used");
				return Response.status(Status.BAD_REQUEST).entity(msg).build();
			} else {
				dao.delete(m);
			}
		}
		return Response.status(Status.NO_CONTENT).build();
	}

	class Meta {

		public int total = 0;
		public int limit = 20;
		public int offset = 0;

		public Meta(int total, int limit, int offset) {
			this.total = total;
			this.limit = limit;
			this.offset = offset;
		}
	}

	class _Response {

		public String status;
		public List <Tag> tags;
		public Meta meta;

		public _Response(String status, List <Tag> list, Meta meta) {
			this.status = status;
			this.tags = list;
			this.meta = meta;
		}
	}
}
