package cashtracker.page.controller

import cashtracker.dao.CostCenterDAO;
import cashtracker.model.CostCenter;

import com.flabser.script._Exception;
import com.flabser.script._Session
import com.flabser.script._WebFormData
import com.flabser.script.events._DoScript


class CostCenters extends _DoScript implements com.flabser.script._IContent {

	List <CostCenter> costcenters

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang) {
		def dao = new CostCenterDAO(session)
		def costCenters = dao.findAll()

		if (costCenters.size > 0) {
			def m_list = new CostCenters()
			m_list.costcenters = costCenters
			publishElement("costcenters", m_list)
		}
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

	public StringBuffer toXML() throws _Exception {
		return new StringBuffer()
	}
}
