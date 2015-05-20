package cashtracker.page.ws


import com.flabser.script.*
import com.flabser.scriptprocessor.*
import com.flabser.script.events.*
import com.flabser.script.actions.*

class AvailableApps extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {
		publishElement("app_name", session.getAppEntourage().getAppName())
		//publishElement("availableapps", session.getAppEntourage().getAvailableApps())
	}
}
