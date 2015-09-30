package cashtracker.page;

import com.flabser.script._Exception;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;


public class Exit extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) throws _Exception {

		publishElement("toworkspace", session.getWorkspaceURL());
		publishElement("tologin", session.getLoginURL());
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
