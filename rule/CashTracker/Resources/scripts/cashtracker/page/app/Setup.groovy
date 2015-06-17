package cashtracker.page.app

import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;
import com.flabser.script.*
import com.flabser.users.*
import com.flabser.solutions.cashtracker.*
import com.flabser.solutions.cashtracker.constants.*

class Setup extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {
		publishElement("defaultbudgetname", "My first budget")
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
}
