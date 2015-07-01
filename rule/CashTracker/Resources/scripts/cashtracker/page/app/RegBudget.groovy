package cashtracker.page.app

import cashtracker.dao.BudgetDAO
import cashtracker.model.Budget
import cashtracker.model.constants.BudgetState

import com.flabser.script.*
import com.flabser.script.events._DoScript
import com.flabser.solutions.postgresql.Database
import com.flabser.users.*


class RegBudget extends _DoScript {

	@Override
	public void doPost(_Session session, _WebFormData formData, String lang) {

		def dao = new BudgetDAO(session)
		def budget = new Budget()

		budget.setName(formData.getValueSilently("budgetname"))
		budget.setRegDate(Database.sqlDateTimeFormat.format(new java.util.Date()))
		budget.setOwner(session.getUser())
		budget.setStatus(BudgetState.ACTIVE)

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
