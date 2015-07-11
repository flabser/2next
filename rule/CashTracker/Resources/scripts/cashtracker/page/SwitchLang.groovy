package cashtracker.page

import com.flabser.localization.LanguageType
import com.flabser.script._Session
import com.flabser.script._WebFormData
import com.flabser.script.events._DoScript

class SwitchLang  extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {

		def toLang = formData.getValueSilently("lang")
		try {
			session.switchLang(LanguageType.valueOf(toLang.toUpperCase()))
			publishElement("process", "lang-switched")
		} catch(IllegalArgumentException e) {
			publishElement("error", "the " + toLang + " language is not available")
		}
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
