package cashtracker.form

import com.flabser.script._Session
import com.flabser.script._WebFormData
import com.flabser.script.actions.*
import com.flabser.script.events._DoScript

import cashtracker.dao.TransactionDAO;


class Transaction extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {

		def dao = new TransactionDAO(session)
		def user = session.getUser()
		def action = formData.getValueSilently("action")
		def key = formData.getValueSilently("key")
		def db = session.getDatabase()

		if (action == "open") {
			def actionBar = session.createActionBar()
			actionBar.addAction(new _Action(getLocalizedWord("save_close", lang),
					getLocalizedWord("save_close", lang), _ActionType.SAVE_AND_CLOSE))
			def closeDoc = new _Action(getLocalizedWord("close", lang),
					getLocalizedWord("close", lang), _ActionType.CLOSE)
			actionBar.addAction(closeDoc)
			publishElement("actionbar",actionBar)

			def accounts = db.select("SELECT * FROM ACCOUNT", user)
			if (accounts.size > 0) {
				publishElement("accounts", accounts)
			}

			if (key == null) {
			} else {
			}
		} else if(action == "save") {
			cashtracker.model.Transaction t = new cashtracker.model.Transaction()
			dao.addTransaction(t)
		}
	}
}
