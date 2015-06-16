package cashtracker.page.saldo

import com.flabser.script.*
import com.flabser.scriptprocessor.*
import com.flabser.script.events.*

class Saldo extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {
		publishElement("calculate", [sum:"0", plus:"0", minus:"0"])
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
