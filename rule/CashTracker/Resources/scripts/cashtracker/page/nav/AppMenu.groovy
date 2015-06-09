package cashtracker.page.nav


import com.flabser.script.*
import com.flabser.scriptprocessor.*
import com.flabser.script.events.*
import com.flabser.script.outline.*

class AppMenu extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {

		def user = session.getUser()
		
		def map = [:]
		map.put("title",formData.getEncodedValueSilently("title"))
		map.put("entry",formData.getEncodedValueSilently("title"))
		map.put("entryid",formData.getEncodedValueSilently("entryid"))
		map.put("id",formData.getEncodedValueSilently("id"))
		if (formData.getEncodedValueSilently("id") == "operationsbycash") {
			map.put("customparam","&cashid=" + formData.getValueSilently("cashid"))
		}
		publishElement("current-entry", map)


		def outline = new _Outline("", "", "outline")
		def operations = new _Outline(getLocalizedWord("transactions", lang), getLocalizedWord("transactions", lang),	"_operations")
		def cash = new _OutlineEntry(getLocalizedWord("All", lang),	getLocalizedWord("All", lang),"operations",	"Provider?id=transactions&page=0")
		operations.addEntry(cash)

		outline.addEntry(operations)

		def users = new _Outline(getLocalizedWord("members", lang),	getLocalizedWord("members", lang),"users")
		users.addEntry(new _OutlineEntry(getLocalizedWord("members", lang),	getLocalizedWord("members", lang),"users","Provider?id=users"))
		outline.addEntry(users)

		if (user.hasRole("ct_glossary")) {
			def properties = new _Outline(getLocalizedWord("parameters", lang),getLocalizedWord("parameters", lang),"_glossary")
			properties.addEntry(new _OutlineEntry(getLocalizedWord("account", lang),getLocalizedWord("account", lang),"cash","Provider?id=cash"))
			properties.addEntry(new _OutlineEntry(getLocalizedWord("operation_type", lang),	getLocalizedWord("operation_type", lang),"category","Provider?id=category"))
			properties.addEntry(new _OutlineEntry(getLocalizedWord("cost_center", lang),getLocalizedWord("cost_center", lang),"costcenter",	"Provider?id=costcenter"))

			outline.addEntry(properties)
		}

		publishElement("outline", outline)
	}
}
