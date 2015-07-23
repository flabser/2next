package nubis.page

import com.flabser.script._Session
import com.flabser.script._WebFormData
import com.flabser.script.events._DoScript


class WS extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {
		def apps = [:]
		def list = session.getUser().applications
		list.each {
			// def el = new _Element(it.appID, it.appName)
			apps.put(it.defaultURL, it.appName)
		}
		publishElement("apps", apps)
		publishElement("apps-count", list.size())
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
