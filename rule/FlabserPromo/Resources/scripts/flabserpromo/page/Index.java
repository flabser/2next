package flabserpromo.page;

import com.flabser.localization.LanguageType;
import com.flabser.script._AppEntourage;
import com.flabser.script._Exception;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;
import com.flabser.servlets.SessionCooks;

public class Index extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang, SessionCooks cooks) throws _Exception {

		String toLang = formData.getValueSilently("lang");
		try {
			if (toLang.length() > 0) {
				LanguageType l = LanguageType.valueOf(toLang.toUpperCase().trim());
				session.switchLang(l);
				cooks.saveLang(l);
			} else if (session.getLang() == null || session.getLang().isEmpty()) {
				session.switchLang(LanguageType.ENG);
			}
		} catch (Exception e) {
			publishElement("error", "the " + toLang + " language is not available");
		}

		_AppEntourage ent = session.getAppEntourage();
		publishElement("availablelangs", ent.getAvailableLangs());
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
