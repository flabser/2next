package cashtracker.page.actionbar

import com.flabser.script.*
import com.flabser.scriptprocessor.*
import com.flabser.script.events.*
import com.flabser.script.actions.*



class Operations extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {

		def actionBar = new _ActionBar(session)
		def user = session.getCurrentAppUser()

		if (user.hasRole(["ct_operations"])) {
			def _new = new _Action(getLocalizedWord("Новая операция", lang), getLocalizedWord("Создать новую операцию", lang), "new_document")
			_new.setURL("Provider?type=edit&id=operation&key=")
			actionBar.addAction(_new)
			actionBar.addAction(new _Action(getLocalizedWord("Удалить", lang), getLocalizedWord("Удалить", lang), _ActionType.DELETE_DOCUMENT))
		}
		setContent(actionBar)
	}
}
