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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cashtracker.dao.AccountDAO;
import cashtracker.model.Account;
import cashtracker.pojo.Errors;
import cashtracker.pojo.Meta;
import cashtracker.validation.AccountValidator;
import cashtracker.validation.ValidationError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flabser.restful.RestProvider;


@Path("accounts")
public class AccountService extends RestProvider {

	private AccountValidator validator = new AccountValidator();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		AccountDAO dao = new AccountDAO(getSession());
		List <Account> list = dao.findAll();
		_Response resp = new _Response("success", list, new Meta(list.size(), 20, 0, 1));

		ObjectMapper om = new ObjectMapper();
		om.disable(SerializationFeature.WRAP_ROOT_VALUE);

		try {
			return Response.ok(om.writeValueAsString(resp)).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.noContent().status(Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		AccountDAO dao = new AccountDAO(getSession());
		Account m = dao.findById(id);
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
	public Response create(Account m) {
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		AccountDAO dao = new AccountDAO(getSession());
		return Response.ok(dao.add(m)).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, Account m) {
		m.setId(id);

		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		AccountDAO dao = new AccountDAO(getSession());
		Account pm = dao.findById(id);
		//
		if (pm == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		}
		//
		pm.setName(m.getName());
		pm.setAmountControl(m.getAmountControl());
		pm.setOpeningBalance(m.getOpeningBalance());
		pm.setCurrencyCode(m.getCurrencyCode());
		pm.setEnabled(m.isEnabled());
		pm.setNote(m.getNote());
		pm.setObservers(m.getObservers());
		pm.setOwner(m.getOwner());
		//
		return Response.ok(dao.update(pm)).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {
		AccountDAO dao = new AccountDAO(getSession());
		Account m = dao.findById(id);
		if (m != null) {
			if (dao.existsTransactionByAccount(m)) {
				Errors msg = new Errors();
				msg.setMessage("used");
				return Response.status(Status.BAD_REQUEST).entity(msg).build();
			} else {
				dao.delete(m);
			}
		}
		return Response.status(Status.NO_CONTENT).build();
	}

	class _Response {

		public String status;
		public List <Account> accounts;
		public Meta meta;

		public _Response(String status, List <Account> list, Meta meta) {
			this.status = status;
			this.accounts = list;
			this.meta = meta;
		}
	}
}
