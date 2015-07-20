package cashtracker.page

import com.flabser.localization.LanguageType
import com.flabser.script.*
import com.flabser.script.actions.*
import com.flabser.script.events.*
import com.flabser.scriptprocessor.*


class Welcome extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {

		String toLang = formData.getValueSilently("lang")
		try {
			if (toLang.length() > 0) {
				session.switchLang(LanguageType.valueOf(toLang.toUpperCase()))
				publishElement("process", "lang-switched")
			}
		} catch(Exception e) {
			publishElement("error", "the " + toLang + " language is not available")
		}

		def ent = session.getAppEntourage()
		publishElement("serverversion", ent.getServerVersion())
		publishElement("build", ent.getBuildTime())
		publishElement("appname", ent.getAppName())
		publishElement("availablelangs", ent.getAvailableLangs())
		publishElement("availableapps", ent.getAvailableApps())
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
