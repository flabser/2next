package cashtracker.page.app

import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;

import com.flabser.script.*
import com.flabser.users.*
import com.flabser.solutions.cashtracker.*
import com.flabser.solutions.cashtracker.constants.*

class RegBudget extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {
		def db = session.getDatabase()
		def user = session.getUser()
		def budgetName = formData.getValueSilently("budgetname")

		def sql = "INSERT BUDGET(NAME, REGDATE, OWNER, STATUS) VALUES(" +
				"'" + budgetName + "'," +
				"'" + Database.sqlDateTimeFormat.format(new java.util.Date()) + "',"
		"'" + user.getUserID() + "'," +
				BudgetStatusType.ACTIVE.getCode() +
				")"
		def res = db.insert(sql, user)
		if (res > 0 ) {
			publishElement("process", "budget-registered")
		}else {
			publishElement("error","saving-failed")
		}
	}
}
