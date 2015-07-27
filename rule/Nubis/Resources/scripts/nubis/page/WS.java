package nubis.page;

import java.util.Collection;

import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;
import com.flabser.users.ApplicationProfile;

public class WS extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {
		Collection<ApplicationProfile> list = session.getUser().getApplications();

		publishElement("apps", list);
		publishElement("apps-count", list.size());
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
