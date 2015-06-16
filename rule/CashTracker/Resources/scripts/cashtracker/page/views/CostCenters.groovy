package cashtracker.page.views

import java.util.List;

import cashtracker.dao.CostCenterDAO;
import cashtracker.model.CostCenter;

import com.flabser.script._Exception;
import com.flabser.script._Session
import com.flabser.script._WebFormData
import com.flabser.script.events._DoScript


class CostCenters extends _DoScript implements com.flabser.script._IContent {

	List <Category> list

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {

		def dao = new CostCenterDAO(session)

		def costCenters = dao.findAll()
		if (costCenters.size > 0) {
			def m_list = new CostCenters()
			m_list.list = costCenters
			publishElement("costcenters", m_list)
		}
	}

	public StringBuffer toXML() throws _Exception {
		return new StringBuffer()
	}
}
