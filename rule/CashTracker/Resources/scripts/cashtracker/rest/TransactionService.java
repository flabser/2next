package cashtracker.rest;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
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

import org.apache.commons.io.FileUtils;

import cashtracker.dao.AccountDAO;
import cashtracker.dao.CategoryDAO;
import cashtracker.dao.CostCenterDAO;
import cashtracker.dao.TagDAO;
import cashtracker.dao.TransactionDAO;
import cashtracker.helper.PageRequest;
import cashtracker.helper.TransactionFilter;
import cashtracker.model.Tag;
import cashtracker.model.Transaction;
import cashtracker.model.TransactionFile;
import cashtracker.model.constants.TransactionType;
import cashtracker.pojo.Meta;
import cashtracker.validation.TransactionValidator;
import cashtracker.validation.ValidationError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flabser.env.Environment;
import com.flabser.restful.RestProvider;
import com.flabser.scheduler.tasks.TempFileCleaner;
import com.flabser.server.Server;
import com.flabser.users.User;


@Path("transactions")
public class TransactionService extends RestProvider {

	private TransactionValidator validator = new TransactionValidator();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("page") int page, @QueryParam("offset") int offset, @QueryParam("limit") int limit,
			@QueryParam("order_by") String orderBy, @QueryParam("direction") String direction,
			@QueryParam("type") String trType) {

		TransactionFilter filter = new TransactionFilter();
		PageRequest pr = new PageRequest((page - 1) * limit, limit, orderBy, direction);
		TransactionDAO dao = new TransactionDAO(getSession());
		int count;

		if (trType != null && !trType.isEmpty()) {
			TransactionType type = TransactionType.typeOf(trType.substring(0, 1).toUpperCase());
			filter.addTransactionType(type);
			count = dao.getCountByType(type);
		} else {
			count = dao.getCount().intValue();
		}

		List <Transaction> list = dao.find(filter, pr);
		_Response _resp = new _Response("success", list, new Meta(count, pr.getLimit(), pr.getOffset(), page));

		ObjectMapper om = new ObjectMapper();
		om.disable(SerializationFeature.WRAP_ROOT_VALUE);

		try {
			return Response.ok(om.writeValueAsString(_resp)).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.noContent().status(Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		TransactionDAO dao = new TransactionDAO(getSession());
		Transaction m = dao.findById(id);
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
	public Response create(Transaction m) {
		//
		if (m.getAccount() != null) {
			AccountDAO accountDao = new AccountDAO(getSession());
			m.setAccount(accountDao.findById(m.getAccount().getId()));
		}
		if (m.getCategory() != null) {
			CategoryDAO categoryDao = new CategoryDAO(getSession());
			m.setCategory(categoryDao.findById(m.getCategory().getId()));
		}
		if (m.getCostCenter() != null) {
			CostCenterDAO costCenterDao = new CostCenterDAO(getSession());
			m.setCostCenter(costCenterDao.findById(m.getCostCenter().getId()));
		}
		if (m.getTags() != null && !m.getTags().isEmpty()) {
			TagDAO tagDao = new TagDAO(getSession());
			List <Long> ids = m.getTags().stream().map(Tag::getId).collect(Collectors.toList());
			m.setTags(tagDao.findAllByIds(ids));
		}

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
		//
		if (m.getAccount() != null) {
			AccountDAO accountDao = new AccountDAO(getSession());
			m.setAccount(accountDao.findById(m.getAccount().getId()));
		}
		if (m.getTransferAccount() != null) {
			AccountDAO accountDao = new AccountDAO(getSession());
			m.setTransferAccount(accountDao.findById(m.getTransferAccount().getId()));
		}
		if (m.getCategory() != null) {
			CategoryDAO categoryDao = new CategoryDAO(getSession());
			m.setCategory(categoryDao.findById(m.getCategory().getId()));
		}
		if (m.getCostCenter() != null) {
			CostCenterDAO costCenterDao = new CostCenterDAO(getSession());
			m.setCostCenter(costCenterDao.findById(m.getCostCenter().getId()));
		}
		if (m.getTags() != null && !m.getTags().isEmpty()) {
			TagDAO tagDao = new TagDAO(getSession());
			List <Long> ids = m.getTags().stream().map(Tag::getId).collect(Collectors.toList());
			m.setTags(tagDao.findAllByIds(ids));
		}

		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		TransactionDAO dao = new TransactionDAO(getSession());
		Transaction pm = dao.findById(id);
		//
		if (pm == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		}
		//
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

	@GET
	@Path("{id}/{field}/{file}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getStream(@PathParam("id") long id, @PathParam("field") String fieldName,
			@PathParam("file") String fileName) {
		TransactionDAO dao = new TransactionDAO(getSession());
		Transaction m = dao.findById(id);
		//
		if (m == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		} else {
			User user = getUserSession().currentUser;
			File userTmpDir = new File(Environment.tmpDir + File.separator + user.getLogin());
			TransactionFile tFile = m.getAttachment(fieldName, fileName);
			if (tFile != null) {
				String fn = userTmpDir.getAbsolutePath() + File.separator + tFile.getRealFileName();
				File file = new File(fn);
				byte[] fileAsByteArray = tFile.getFile();
				try {
					FileUtils.writeByteArrayToFile(file, fileAsByteArray);
				} catch (IOException e) {
					Server.logger.errorLogEntry(e);
				}
				if (file != null && file.exists()) {
					TempFileCleaner.addFileToDelete(fn);
					try {
						String codedFileName = URLEncoder.encode(file.getName(), "UTF8");
						return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
								.header("Content-Disposition", "attachment; filename*=\"utf-8'" + codedFileName + "\"")
								.build();
					} catch (UnsupportedEncodingException e) {
						Server.logger.errorLogEntry(e);
					}

				} else {
					return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
				}
			}
			//
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
		}
	}

	@DELETE
	@Path("{id}/{field}/{file}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAttachment(@PathParam("id") long id, @PathParam("field") String fieldName,
			@PathParam("file") String fileName) {
		TransactionDAO dao = new TransactionDAO(getSession());
		Transaction m = dao.findById(id);
		//
		if (m == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		} else {
			if (m.deleteAttachment(fieldName, fileName)) {
				return Response.status(HttpServletResponse.SC_OK).build();
			}
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
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
	public static int calculatePageCount(int count, int limit) {
		return count > limit ? (int) Math.ceil((double) count / limit) : 1;
	}
}
