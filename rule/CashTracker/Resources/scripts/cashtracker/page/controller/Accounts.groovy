package cashtracker.page.controller

import cashtracker.model.Account
import cashtracker.dao.AccountDAO;

import com.flabser.script._Exception;
import com.flabser.script._Session
import com.flabser.script._WebFormData
import com.flabser.script.events._DoScript


class Accounts extends _DoScript implements com.flabser.script._IContent {

	List <Account> accounts

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {
		def dao = new AccountDAO(session)
		def accounts = dao.findAll()

		if (accounts.size > 0) {
			def m_list = new Accounts()
			m_list.accounts = accounts
			publishElement("accounts", m_list)
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
