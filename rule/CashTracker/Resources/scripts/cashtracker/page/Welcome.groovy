package cashtracker.page


import com.flabser.script.*
import com.flabser.scriptprocessor.*
import com.flabser.script.events.*
import com.flabser.script.actions.*


class Welcome extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {

		def ent = session.getAppEntourage()
		publishElement("serverversion", ent.getServerVersion())
		publishElement("build", ent.getBuildTime())
		publishElement("org", ent.getGeneralName())
		publishElement("appname", ent.getAppName())
		publishElement("availablelangs", ent.getAvailableLangs())
		publishElement("availableapps", ent.getAvailableApps())
	}
}
