package cashtracker.page.app

import com.flabser.script.*
import com.flabser.users.*
import com.flabser.solutions.cashtracker.*
import com.flabser.solutions.cashtracker.constants.*

class RegUserComplete {

	public static int doProcess(_Session session, User user, String budgetName) {
		
		def res = 0
		def appName = session.getGlobalSettings().appName;
		ApplicationProfile ap = new ApplicationProfile()
		ap.appName = appName
		ap.owner = user.getUserID()
		def dbLogin = (user.getUserID().replace("@","_").replace(".","_")).replace("-","_").toLowerCase() 
		ap.dbName = appName.toLowerCase() + "_" + ap.owner
		ap.dbLogin = dbLogin
		ap.dbPwd = _Helper.getRandomValue()
		user.addApplication(ap)

		if(user.save()) {
			def db = session.getDatabase()
			def sql = "INSERT BUDGET(NAME, REGDATE, OWNER, STATUS) VALUES(" +
					"'" + budgetName + "'," +
					"'" + Database.sqlDateTimeFormat.format(new java.util.Date()) + "',"
			"'" + user.getUserID() + "'," +
					BudgetStatusType.ACTIVE.getCode() +
					")"
			res = db.insert(sql, user)
		}

		return res
	}
}
