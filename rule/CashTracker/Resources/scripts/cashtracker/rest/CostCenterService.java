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
import javax.ws.rs.core.Response.Status;

import cashtracker.dao.CostCenterDAO;
import cashtracker.model.CostCenter;
import cashtracker.model.Errors;
import cashtracker.validation.CostCenterValidator;
import cashtracker.validation.ValidationError;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;


@Path("cost-centers")
public class CostCenterService extends RestProvider {

	private CostCenterValidator validator = new CostCenterValidator();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		CostCenterDAO dao = new CostCenterDAO(getSession());
		return Response.ok(new CostCenters(dao.findAll())).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		CostCenterDAO dao = new CostCenterDAO(getSession());
		CostCenter m = dao.findById(id);
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
		return Response.ok(dao.update(m)).build();
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
				return Response.ok(msg).status(Status.BAD_REQUEST).build();
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
}
