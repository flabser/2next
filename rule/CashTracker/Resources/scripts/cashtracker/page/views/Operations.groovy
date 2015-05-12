package cashtracker.page.views

import java.text.SimpleDateFormat
import com.flabser.script.*
import com.flabser.scriptprocessor.*
import com.flabser.script.events.*

class Operations extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {

		def viewParam = session.createViewEntryCollectionParam()
		viewParam.setQuery("form = 'operation'")
				.setPageNum(formData.getNumberValueSilently("page", 0))
				.setUseFilter(true)
				.setCheckResponse(true)
				.setDateFormat(new SimpleDateFormat("dd.MM.yyyy"))

		setContent(session.getCurrentDatabase().getCollectionOfDocuments(viewParam))
	}
}
