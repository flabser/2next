package cashtracker.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cashtracker.dao.AccountDAO;
import cashtracker.dao.TransactionDAO;
import cashtracker.model.constants.TransactionType;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;


@Path("dashboard")
public class DashboardService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		return Response.ok(new DashboardData()).build();
	}

	@JsonRootName("dashboard")
	class DashboardData {

		private AccountDAO accountDAO = new AccountDAO(getSession());
		private TransactionDAO tdao = new TransactionDAO(getSession());

		public Map <String, Long> trByAccount = new HashMap <String, Long>();

		public int countOfExpense = 0;
		public int countOfIncome = 0;
		public int countOfTransfer = 0;

		public DashboardData() {
			countOfExpense = tdao.getCountByType(TransactionType.EXPENSE);
			countOfIncome = tdao.getCountByType(TransactionType.INCOME);
			countOfTransfer = tdao.getCountByType(TransactionType.TRANSFER);

			/*--select transaction_type, sum(amount), count(id) from transactions group by transaction_type;
			--select account_from, sum(amount), count(id) from transactions group by account_from;
			--select category, sum(amount), count(id) from transactions group by category;
			select transaction_type, sum(amount), count(id) from transactions as t
			  where date(t.date) = '2015-09-28'
			  group by transaction_type;*/

			
			/*for (Account acc : accountDAO.findAll()) {
				trByAccount.put(acc.getName(), (long) tdao.findAllByAccountFrom(acc).size());
			}*/
		}
	}
}
