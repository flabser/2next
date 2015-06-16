package cashtracker.page.views

import java.text.SimpleDateFormat
import java.util.List;

import cashtracker.dao.BudgetDAO;
import cashtracker.model.Budget;

import com.flabser.script._Exception;
import com.flabser.script._Session
import com.flabser.script._WebFormData
import com.flabser.script.events._DoScript


class Budgets extends _DoScript implements com.flabser.script._IContent {

	List <Budget> list

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {

		def dao = new BudgetDAO(session)

		def budgets = dao.findAll()
		if (budgets.size > 0) {
			def m_list = new Budgets()
			m_list.list = budgets
			publishElement("budgets", m_list)
		}
	}

	public StringBuffer toXML() throws _Exception {
		return new StringBuffer()
	}
}
