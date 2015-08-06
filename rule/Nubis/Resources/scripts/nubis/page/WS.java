package nubis.page;

import java.util.HashMap;

import com.flabser.restful.Application;
import com.flabser.script._AppEntourage;
import com.flabser.script._Exception;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;

public class WS extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) throws _Exception {
		HashMap<String, Application> list = session.getUser().getApplications();

		publishElement("apps", list.values());

		_AppEntourage ent = session.getAppEntourage();
		publishElement("templates", ent.getAvailableTemplates());
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
