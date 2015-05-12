package cashtracker.page.app

import com.flabser.script.*

class VerifyDoc {

	public String code
	public String email

	private VerifyDoc(String code, String email) {
		this.code = code
		this.email = email
	}

	public static VerifyDoc findVerifyDocByEMail(_Session session, String email) {
		def viewParam = session.createViewEntryCollectionParam()
				.setQuery("form = 'verify_email' and viewtext = '${email}'")
		def cols = session.getCurrentDatabase().getCollectionOfDocuments(viewParam)

		if (cols.getCount() == 0) {
			return null
		}

		def entries = cols.getEntries()
		String entryId = entries[0].getID()
		String m_email = entries[0].getViewText(0)
		String m_code = entries[0].getViewText(1)

		return new VerifyDoc(m_code, m_email)
	}

	public static VerifyDoc createVerifyDoc(_Session session, RegWebForm regForm) {

		def db = session.getCurrentDatabase()

		// create verify doc
		def verifyCode = _Helper.getRandomValue() + _Helper.getRandomValue() + _Helper.getRandomValue()
		def verifyDoc = new _Document(db)
		verifyDoc.setForm("verify_email")
		verifyDoc.addField("email", regForm.email)
		verifyDoc.setViewText(regForm.email)
		verifyDoc.addViewText(verifyCode)
		verifyDoc.addViewText(regForm.budgetName)
		verifyDoc.addReader(regForm.email)
		verifyDoc.addReader("[observer]")
		verifyDoc.addEditor("[observer]")

		if (verifyDoc.save("[observer]")) {
			return new VerifyDoc(verifyCode, regForm.email)
		} else {
			return null
		}
	}
}
