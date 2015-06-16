package cashtracker.page.controller

import cashtracker.dao.TransactionDAO;
import cashtracker.model.Transaction;

import com.flabser.script.*
import com.flabser.scriptprocessor.*
import com.flabser.script.events.*
import com.flabser.script.dbdata.*


class Transactions extends _DoScript implements com.flabser.script._IContent {

	List <Transaction> transactions

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {

		def dao = new TransactionDAO(session)
		def transactions = dao.findAll()

		if (transactions.size > 0) {
			def m_list = new Transactions()
			m_list.transactions = transactions
			publishElement("transactions", m_list)
		}
	}

	@Override
	public void doPost(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doPut(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doDelete(_Session session, _WebFormData formData, String lang) {
	}

	public StringBuffer toXML() throws _Exception {
		return new StringBuffer()
	}
}
