package cashtracker.page.saldo

import com.flabser.script.*
import com.flabser.scriptprocessor.*
import com.flabser.script.events.*

class Calculate extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {
		def db = session.getCurrentDatabase()
		def saldoValue
		def viewParam = session.createViewEntryCollectionParam()
				.setPageNum(0)
				.setPageSize(0)
				.setUseFilter(true)
				.setCheckResponse(true)

		if(formData.getValue("id") == "operationsbycash"){
			String sf = "form = 'operation' and cash#number = " + formData.getNumberValueSilently("cashid", -1)
			saldoValue = SaldoUtils.getViewNumberTotal(db, viewParam.setQuery(sf))
		} else if (formData.getValue("id") == "search"){
			saldoValue = db.search(formData.getValue("keyword"), 0).getViewNumberTotal()
		} else if(formData.getValueSilently("ddbid").length() > 0) {
			def doc = db.getDocumentByID(formData.getValue("ddbid"))
			def date = _Helper.getDateAsString(doc.getValueDate("date"))
			viewParam.setUseFilter(false)
					.setCheckResponse(false)
					.setQuery("form = 'operation' and date#datetime <= '$date'")
			saldoValue = SaldoUtils.getViewNumberTotal(db, viewParam)
		} else {
			saldoValue = SaldoUtils.getViewNumberTotal(db, viewParam.setQuery("form = 'operation'"))
		}

		def saldoTag = new _Tag("saldo")
		saldoTag.addTag("sum", saldoValue[0].setScale(2, BigDecimal.ROUND_DOWN))
		saldoTag.addTag("plus", saldoValue[1].setScale(2, BigDecimal.ROUND_DOWN))
		saldoTag.addTag("minus", saldoValue[2].setScale(2, BigDecimal.ROUND_DOWN))

		setContent(new _XMLDocument(saldoTag))
	}
}
