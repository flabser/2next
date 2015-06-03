package cashtracker.page.app

import com.flabser.script.*
import com.flabser.users.*
import com.flabser.solutions.cashtracker.*
import com.flabser.solutions.cashtracker.constants.*

class RegUserComplete {

	public static int doProcess(_Session session, User user, String budgetName) {

		def db = session.getDatabse()
		
		def sql = "INSERT BUDGET(NAME, REGDATE, OWNER, STATUS) VALUES(" +
		"'" + budgetName + "'," +
		"'" + Database.sqlDateTimeFormat.format(new java.util.Date()) + "',"
		"'" + user.getUserID() + "'," + 
		BudgetStatusType.ACTIVE.getCode() + 
		")"
		
		def res = db.insert(sql, user)
		

		return res
	}
}
