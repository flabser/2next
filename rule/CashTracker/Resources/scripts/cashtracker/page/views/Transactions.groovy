package cashtracker.page.views

import com.flabser.script.*
import com.flabser.scriptprocessor.*
import com.flabser.script.events.*
import com.flabser.script.dbdata.*
import cashtracker.dao.impl.TransactionDAOImpl


class Transactions extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {

		def dao = new TransactionDAOImpl(session)

		def transactions = dao.findAll()
		if (transactions.size > 0) {
			transactions.each {
				publishElement("transactions", it)
			}
		}
	}
}
