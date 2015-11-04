package administrator.page;

import com.flabser.localization.LanguageType;
import com.flabser.script._AppEntourage;
import com.flabser.script._Exception;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;


public class Site extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) throws _Exception {

		String toLang = formData.getValueSilently("lang");
		try {
			if (toLang.length() > 0) {
				session.switchLang(LanguageType.valueOf(toLang.toUpperCase()));
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
	public void doPost(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doPut(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doDelete(_Session session, _WebFormData formData, String lang) {
	}
}
