package cashtracker.page

import com.flabser.script.*
import com.flabser.script.events._DoScript
import com.flabser.users.*


class SetupPage extends _DoScript {

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
