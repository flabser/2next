package cashtracker.page.views

import com.flabser.script._Exception;
import com.flabser.script._Session
import com.flabser.script._WebFormData
import com.flabser.script.events._DoScript

import cashtracker.model.Account
import cashtracker.dao.AccountDAO;


class Accounts extends _DoScript implements com.flabser.script._IContent {

	List <Account> list

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {

		def dao = new AccountDAO(session)

		def accounts = dao.findAll()
		if (accounts.size > 0) {
			def acs = new Accounts()
			acs.list = accounts
			publishElement("accounts", acs)
		}
	}

	public StringBuffer toXML() throws _Exception {
		return new StringBuffer()
	}
}
