package cashtracker.page.views

import java.util.List;

import com.flabser.script.*
import com.flabser.scriptprocessor.*
import com.flabser.script.events.*
import com.flabser.script.dbdata.*

import cashtracker.dao.TransactionDAO;
import cashtracker.model.Transaction;


class Transactions extends _DoScript implements com.flabser.script._IContent {

	List <Transaction> list

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {

		def dao = new TransactionDAO(session)

		def transactions = dao.findAll()
		if (transactions.size > 0) {
			def m_list = new Transactions()
			m_list.list = transactions
			publishElement("transactions", m_list)
		}
	}

	public StringBuffer toXML() throws _Exception {
		return new StringBuffer()
	}
}
