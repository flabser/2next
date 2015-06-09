package cashtracker.form

import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;


class Transaction extends _DoScript {
	
	@Override
		public void doProcess(_Session session, _WebFormData formData, String lang) {

		def action = formData.getValueSilently("action")
		def key = formData.getValueSilently("key")
		def db = session.getDatabase()
		
		if (action == "open") {
			
			
			if (key == null) {
				
			}else {
			
			}
		}else if(action == "save") {
		
		
		
		}		
	}
	
}
