package cashtracker.page;

import com.flabser.localization.LanguageType;
import com.flabser.script._AppEntourage;
import com.flabser.script._Exception;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;
import com.flabser.servlets.SessionCooks;


public class Welcome extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang, SessionCooks cooks) throws _Exception {

		String toLang = formData.getValueSilently("lang");
		try {
			if (toLang.length() > 0) {
				session.setLang(LanguageType.valueOf(toLang.toUpperCase()));
			} else if (session.getLanguage() == null || session.getLanguage().isEmpty()) {
				session.setLang(LanguageType.ENG);
			}
		} catch (Exception e) {
			publishElement("error", "the " + toLang + " language is not available");
		}

		_AppEntourage ent = session.getAppEntourage();
		publishElement("serverversion", ent.getServerVersion());
		publishElement("build", ent.getBuildTime());
		publishElement("appname", ent.getAppName());
		publishElement("availablelangs", ent.getAvailableLangs());
		publishElement("availableapps", ent.getAvailableApps());
	}

	@Override
	public void doPost(_Session session, _WebFormData formData, String lang, SessionCooks cooks) {
	}

	@Override
	public void doPut(_Session session, _WebFormData formData, String lang, SessionCooks cooks) {
	}

	@Override
	public void doDelete(_Session session, _WebFormData formData, String lang, SessionCooks cooks) {
	}
}
