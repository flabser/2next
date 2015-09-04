package nubis.page;

import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;
import com.flabser.users.ApplicationStatusType;
import com.flabser.users.User;
import com.flabser.users.VisibiltyType;


public class RegApp extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doPost(_Session session, _WebFormData formData, String lang) {
		VisibiltyType vis = VisibiltyType.ONLY_MEMBERS;
		User user = session.getAppUser();
		String appType = formData.getValueSilently("apptype");
		String appName = formData.getValueSilently("appname");
		String visibilty = formData.getValueSilently("visibilty");
		String description = formData.getValueSilently("description");
		if (visibilty.equals("public")) {
			vis = VisibiltyType.PUBLIC;
		}

		if (appType != null) {
			ApplicationProfile ap = new ApplicationProfile();
			ap.appType = appType;
			ap.appName = appName;
			ap.setVisibilty(vis);
			ap.setDesciption(description);
			ap.owner = user.getLogin();
			ap.dbName = ap.appType.toLowerCase() + ap.appId();
			ap.status = ApplicationStatusType.READY_TO_DEPLOY;
			if (ap.save()) {
				user.addApplication(ap);
				if (user.save()) {
					publishElement("process", "application-registered");
				} else {
					publishElement("error", "save-error");
				}
				publishElement("app-status", ap.getStatus().name());
			} else {
				publishElement("error", "save-error");
			}
		}
	}

	@Override
	public void doPut(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doDelete(_Session session, _WebFormData formData, String lang) {
	}
}
