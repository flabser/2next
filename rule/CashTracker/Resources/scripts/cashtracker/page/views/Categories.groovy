package cashtracker.page.views

import java.util.List;

import cashtracker.dao.CategoryDAO;
import cashtracker.model.Category;

import com.flabser.script._Exception;
import com.flabser.script._Session
import com.flabser.script._WebFormData
import com.flabser.script.events._DoScript


class Categories extends _DoScript implements com.flabser.script._IContent {

	List <Category> categories

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {

		def dao = new CategoryDAO(session)

		def categories = dao.findAll()
		if (categories.size > 0) {
			def m_list = new Categories()
			m_list.categories = categories
			publishElement("categories", m_list)
		}
	}

	public StringBuffer toXML() throws _Exception {
		return new StringBuffer()
	}

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {
		doProcess(session, formData, lang)
	}

	@Override
	public void doPost(_Session session, _WebFormData formData, String lang) {
		doProcess(session, formData, lang)
	}

	@Override
	public void doPut(_Session session, _WebFormData formData, String lang) {
	}

	@Override
	public void doDelete(_Session session, _WebFormData formData, String lang) {
	}
}
