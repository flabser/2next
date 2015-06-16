package cashtracker.page.controller

import cashtracker.dao.BudgetDAO;
import cashtracker.model.Budget;

import com.flabser.script._Exception;
import com.flabser.script._Session
import com.flabser.script._WebFormData
import com.flabser.script.events._DoScript


class Budgets extends _DoScript implements com.flabser.script._IContent {

	List <Budget> budgets

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {
		def dao = new BudgetDAO(session)
		def budgets = dao.findAll()

		if (budgets.size > 0) {
			def m_list = new Budgets()
			m_list.budgets = budgets
			publishElement("budgets", m_list)
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
