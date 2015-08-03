package nubis.page;

import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;
import com.flabser.solutions.DatabaseType;
import com.flabser.users.User;
import com.flabser.users.VisibiltyType;


public class RegApp extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {
		doPost(session, formData, lang);
	}

	@Override
	public void doPost(_Session session, _WebFormData formData, String lang) {
		VisibiltyType vis = VisibiltyType.ONLY_MEMBERS;
		User user = session.getUser();
		String appType = formData.getValueSilently("apptype");
		String appName = formData.getValueSilently("appname");
		String visibilty = formData.getValueSilently("visibilty");
		if (visibilty.equalsIgnoreCase("public")) {
			vis = VisibiltyType.PUBLIC;
		}

		if (appType != null) {
			ApplicationProfile ap = new ApplicationProfile();
			ap.appType = appType;
			ap.appName = appName;
			ap.setVisibilty(vis);
			ap.owner = user.getLogin();
			ap.dbLogin = (user.getLogin().replace("@", "_").replace(".", "_").replace("-", "_")).toLowerCase();
			ap.dbType = DatabaseType.POSTGRESQL;
			ap.dbName = ap.appType.toLowerCase() + ap.appId();
			ap.dbPwd = user.getDefaultDbPwd();
			if (ap.save()) {
				user.addApplication(ap);
				if (user.save()) {
					publishElement("process", "application-registered");
				} else {
					publishElement("error", "save-error");
				}
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
