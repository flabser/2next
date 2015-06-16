package cashtracker.page.views

import com.flabser.script._Exception;
import com.flabser.script._Session
import com.flabser.script._WebFormData
import com.flabser.script.events._DoScript

import cashtracker.model.Account
import cashtracker.dao.AccountDAO;


class Accounts extends _DoScript implements com.flabser.script._IContent {

	List <Account> accounts

	public void doProcess(_Session session, _WebFormData formData, String lang) {

		def dao = new AccountDAO(session)

		def accounts = dao.findAll()
		if (accounts.size > 0) {
			def m_list = new Accounts()
			m_list.accounts = accounts
			publishElement("accounts", m_list)
		}
	}

	public StringBuffer toXML() throws _Exception {
		return new StringBuffer()
	}

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {
		doProcess(session, formData, lang)
	}

	@Override
	public void doPost(_Session session, _WebFormData formData, String lang) {
		doProcess(session, formData, lang)
	}

	@Override
	public void doPut(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doDelete(_Session session, _WebFormData formData, String lang) {
	}
}
