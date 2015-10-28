package nubis.page;

import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;
import com.flabser.users.ApplicationStatusType;
import com.flabser.users.User;


public class UnregApp extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doPost(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doPut(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doDelete(_Session session, _WebFormData formData, String lang) {
		User user = session.getUser();
		String appID = formData.getValueSilently("app");
		ApplicationProfile ap = user.getApplicationProfile(appID);

		if (ap != null) {
			if (ap.owner.equals(user.getLogin())) {

				ap.setStatus(ApplicationStatusType.READY_TO_REMOVE);
				if (ap.save()) {
					publishElement("process", "application-ready-to-remove");

				} else {
					publishElement("error", "save-error");
				}
			}
		}
	}
}
