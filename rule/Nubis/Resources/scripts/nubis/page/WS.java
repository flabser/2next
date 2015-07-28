package nubis.page;

import java.util.Collection;

import com.flabser.script._AppEntourage;
import com.flabser.script._Exception;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;
import com.flabser.users.ApplicationProfile;

public class WS extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) throws _Exception {
		Collection<ApplicationProfile> list = session.getUser().getApplications();

		publishElement("apps", list);
		publishElement("apps-count", list.size());

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
