package cashtracker.page.ws

import com.flabser.script.*
import com.flabser.scriptprocessor.*
import com.flabser.script.events.*
import com.flabser.script.actions.*


class AvailableApps extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {
		publishElement("app_name", session.getAppEntourage().getAppName())
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
