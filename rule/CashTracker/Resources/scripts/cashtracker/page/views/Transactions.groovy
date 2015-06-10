package cashtracker.page.views

import java.text.SimpleDateFormat
import com.flabser.script.*
import com.flabser.scriptprocessor.*
import com.flabser.script.events.*
import com.flabser.script.dbdata.*

class Transactions extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {

		def db = session.getDatabase()
		def sql = "SELECT * FROM TRANSACTION"
		def rs = db.select(sql, session.getUser())
	
		//publishElement("view", new _DataBox(rs))
	}

}
