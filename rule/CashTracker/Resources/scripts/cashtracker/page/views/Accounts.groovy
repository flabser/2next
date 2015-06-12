package cashtracker.page.views

import com.flabser.script._Session
import com.flabser.script._WebFormData
import com.flabser.script.events._DoScript
import cashtracker.dao.impl.AccountDAOImpl


class Accounts extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {

		def dao = new AccountDAOImpl(session)

		def accounts = dao.findAll()
		if (accounts.size > 0) {
			accounts.each { publishElement("accounts", it) }
		}
	}
}
