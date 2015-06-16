package cashtracker.page.views

import java.util.List;

import cashtracker.dao.CategoryDAO;
import cashtracker.model.Category;

import com.flabser.script._Exception;
import com.flabser.script._Session
import com.flabser.script._WebFormData
import com.flabser.script.events._DoScript


class Categories extends _DoScript implements com.flabser.script._IContent {

	List <Category> list

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {

		def dao = new CategoryDAO(session)

		def categories = dao.findAll()
		if (categories.size > 0) {
			def m_list = new Categories()
			m_list.list = categories
			publishElement("categories", m_list)
		}
	}

	public StringBuffer toXML() throws _Exception {
		return new StringBuffer()
	}
}
