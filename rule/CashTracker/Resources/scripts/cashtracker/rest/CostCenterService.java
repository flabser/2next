package cashtracker.rest;

import java.util.ArrayList;
import java.util.Collection;
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

import cashtracker.dao.CostCenterDAO;
import cashtracker.helper.PageRequest;
import cashtracker.model.CostCenter;
import cashtracker.pojo.Errors;
import cashtracker.pojo.Meta;
import cashtracker.validation.CostCenterValidator;
import cashtracker.validation.ValidationError;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flabser.restful.RestProvider;


@Path("cost-centers")
public class CostCenterService extends RestProvider {

	private CostCenterValidator validator = new CostCenterValidator();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("page") int page, @QueryParam("limit") int limit) {
		CostCenterDAO dao = new CostCenterDAO(getSession());
		PageRequest pr = new PageRequest(page * limit, limit, "", "");
		List <CostCenter> list = dao.findAll(pr);
		_Response resp = new _Response("success", list, new Meta(list.size(), -1, -1));

		ObjectMapper om = new ObjectMapper();
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
		CostCenterDAO dao = new CostCenterDAO(getSession());
		CostCenter m = dao.findById(id);
		//
		if (m == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		}
		//
		return Response.ok(m).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(CostCenter m) {
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		CostCenterDAO dao = new CostCenterDAO(getSession());
		return Response.ok(dao.add(m)).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, CostCenter m) {
		m.setId(id);

		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		CostCenterDAO dao = new CostCenterDAO(getSession());
		CostCenter pm = dao.findById(id);
		//
		if (pm == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		}
		//
		pm.setName(m.getName());
		//
		return Response.ok(dao.update(pm)).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {
		CostCenterDAO dao = new CostCenterDAO(getSession());
		CostCenter m = dao.findById(id);
		if (m != null) {
			if (dao.existsTransactionByCostCenter(m)) {
				Errors msg = new Errors();
				msg.setMessage("used");
				return Response.status(Status.BAD_REQUEST).entity(msg).build();
			} else {
				dao.delete(m);
			}
		}
		return Response.status(Status.NO_CONTENT).build();
	}

	@JsonRootName("cost-centers")
	class CostCenters extends ArrayList <CostCenter> {

		private static final long serialVersionUID = 1L;

		public CostCenters(Collection <? extends CostCenter> m) {
			addAll(m);
		}
	}

	class _Response {

		public String status;
		public List <CostCenter> costCenters;
		public Meta meta;

		public _Response(String status, List <CostCenter> list, Meta meta) {
			this.status = status;
			this.costCenters = list;
			this.meta = meta;
		}
	}
}
