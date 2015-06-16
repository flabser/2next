package cashtracker.page.saldo

import com.flabser.script.*
import com.flabser.scriptprocessor.*
import com.flabser.script.events.*

class Saldo extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {
		def db = session.getDatabase()
		def saldoValue

		publishElement("calculate", [sum:"0", plus:"0", minus:"0"])
	}
}
