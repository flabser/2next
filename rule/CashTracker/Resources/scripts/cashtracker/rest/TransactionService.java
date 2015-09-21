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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import cashtracker.dao.TransactionDAO;
import cashtracker.helper.PageRequest;
import cashtracker.model.Transaction;
import cashtracker.model.constants.TransactionType;
import cashtracker.validation.TransactionValidator;
import cashtracker.validation.ValidationError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flabser.restful.RestProvider;


@Path("transactions")
public class TransactionService extends RestProvider {

	private TransactionValidator validator = new TransactionValidator();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("page") int page, @QueryParam("offset") int offset, @QueryParam("limit") int limit,
			@QueryParam("order_by") String orderBy, @QueryParam("direction") String direction,
			@QueryParam("type") String trType) {

		PageRequest pr = new PageRequest((page - 1) * limit, limit, orderBy, direction);
		TransactionDAO dao = new TransactionDAO(getSession());
		TransactionType type = null;
		long count;
		if (trType != null && !trType.isEmpty()) {
			type = TransactionType.typeOf(trType.substring(0, 1).toUpperCase());
			count = dao.getCountTransactions(type);
		} else {
			count = dao.getCountTransactions();
		}
		//
		ResponseBuilder resp = Response.ok();
		//
		String urlPrev = "transactions?limit=" + pr.getLimit() + "&offset=" + (pr.getOffset() - pr.getLimit())
				+ "&order_by=" + orderBy + "&type=" + trType;
		resp.link(urlPrev, "prev");
		if (count > offset) {
			String urlNext = "transactions?limit=" + pr.getLimit() + "&offset=" + (pr.getOffset() + pr.getLimit())
					+ "&order_by=" + orderBy + "&type=" + trType;
			resp.link(urlNext, "next");
		}
		//
		List <Transaction> list = dao.find(pr, type);
		_Response _resp = new _Response("success", list, new Meta(count, pr.getLimit(), pr.getOffset()));

		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
		om.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

		try {
			return resp.entity(om.writeValueAsString(_resp)).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		TransactionDAO dao = new TransactionDAO(getSession());
		Transaction m = dao.findById(id);
		return Response.ok(m).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Transaction m) {
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		TransactionDAO dao = new TransactionDAO(getSession());
		return Response.ok(dao.add(m)).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, Transaction m) {
		m.setId(id);
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		TransactionDAO dao = new TransactionDAO(getSession());
		return Response.ok(dao.update(m)).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {
		TransactionDAO dao = new TransactionDAO(getSession());
		Transaction m = dao.findById(id);
		if (m != null) {
			dao.delete(m);
		}
		return Response.status(Status.NO_CONTENT).build();
	}

	class Meta {

		public long total = 0;
		public int limit = 20;
		public int offset = 0;

		public Meta(long total, int limit, int offset) {
			this.total = total;
			this.limit = limit;
			this.offset = offset;
		}
	}

	class _Response {

		public String status;
		public List <Transaction> transactions;
		public Meta meta;

		public _Response(String status, List <Transaction> list, Meta meta) {
			this.status = status;
			this.transactions = list;
			this.meta = meta;
		}
	}

	//
	public boolean hasNext(int offset, int count) {
		return offset < count;
	}

	public static int calculatePageCount(int count, int limit) {
		return (count > limit) ? (int) Math.ceil((double) count / limit) : 1;
	}
}
