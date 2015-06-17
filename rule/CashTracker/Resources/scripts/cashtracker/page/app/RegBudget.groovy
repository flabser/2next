package cashtracker.page.app

import com.flabser.script._Session
import com.flabser.script._WebFormData
import com.flabser.script.events._DoScript
import com.flabser.script.*
import com.flabser.users.*
import com.flabser.solutions.cashtracker.*
import com.flabser.solutions.cashtracker.constants.*

import cashtracker.dao.BudgetDAO;
import cashtracker.model.Budget


class RegBudget extends _DoScript {

	@Override
	public void doPost(_Session session, _WebFormData formData, String lang) {

		def dao = new BudgetDAO(session)
		def budget = new Budget()

		budget.setName(formData.getValueSilently("budgetname"))
		budget.setRegDate(Database.sqlDateTimeFormat.format(new java.util.Date()))
		budget.setOwner(session.getUser())
		budget.setStatus(BudgetStatusType.ACTIVE)

		def res = dao.addBudget(budget)
		if (res > 0) {
			publishElement("process", "budget-registered")
		} else {
			publishElement("error"," saving-failed")
		}
	}

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doPut(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doDelete(_Session session, _WebFormData formData, String lang) {
	}
}
