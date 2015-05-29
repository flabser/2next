package cashtracker.page.nav


import com.flabser.script.*
import com.flabser.scriptprocessor.*
import com.flabser.script.events.*
import com.flabser.script.outline.*

class AppMenu extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {

		def list = []
		def user = session.getUser()

		def rootTag = new _Element("current")
		def entryTag = new _Element("entry", formData.getEncodedValueSilently("title"))
//		entryTag.setAttr("entryid", formData.getValueSilently("entryid"))
//		entryTag.setAttr("id", formData.getValueSilently("id"))

		if (formData.getEncodedValueSilently("id") == "operationsbycash") {
			def customParam = new _Element("customparam", "&cashid=" + formData.getValueSilently("cashid"))
			rootTag.addElement(customParam)
		}

		rootTag.addElement(entryTag)
	//	publishElement(new _WebDocument(rootTag))

		//
		def operations = new _Outline(getLocalizedWord("Операции", lang),
				getLocalizedWord("Операции", lang),
				"_operations")
		def cash = new _OutlineEntry(getLocalizedWord("Все", lang),
				getLocalizedWord("Все", lang),
				"operations",
				"Provider?type=page&id=operations&page=0")
	/*	def documents = session.currentDatabase.getCollectionOfGlossaries("form='cash'", 0, 0)
		documents.getEntries().each {
			def document = it.getDocument()
			cash.addEntry(new _OutlineEntry(document.getValueString("name"),
					document.getValueString("name"),
					document.getValueString("name"),
					"Provider?type=page&id=operationsbycash&cashid=" + document.getDocID() + "&page=0"))
		}
		operations.addEntry(cash)
*/
		def outline = new _Outline("", "", "outline")
		outline.addOutline(operations)
		list.add(operations)

		//
		def users = new _Outline(getLocalizedWord("Участники", lang),
				getLocalizedWord("Участники", lang),
				"users")
		users.addEntry(new _OutlineEntry(getLocalizedWord("Участники", lang),
				getLocalizedWord("Участники", lang),
				"users",
				"Provider?type=page&id=users"))
		list.add(users)

		//
		if (true || user.hasRole("ct_glossary")) {
			def properties = new _Outline(getLocalizedWord("Параметры", lang),
					getLocalizedWord("Параметры", lang),
					"_glossary")
			properties.addEntry(new _OutlineEntry(getLocalizedWord("Касса", lang),
					getLocalizedWord("Касса", lang),
					"cash",
					"Provider?type=page&id=cash"))
			properties.addEntry(new _OutlineEntry(getLocalizedWord("Тип операции", lang),
					getLocalizedWord("Тип операции", lang),
					"category",
					"Provider?type=page&id=category"))
			properties.addEntry(new _OutlineEntry(getLocalizedWord("Место возникновения", lang),
					getLocalizedWord("Место возникновения", lang),
					"costcenter",
					"Provider?type=page&id=costcenter"))

			/*//
			def budgetParam = session.createViewEntryCollectionParam().setQuery("form = 'budget'")
			def budgets = session.currentDatabase.getCollectionOfDocuments(budgetParam)
			if (budgets.getCount()) {
				def budget = budgets.entries[0].document

				properties.addEntry(new _OutlineEntry(getLocalizedWord("Свойства бюджета", lang),
						getLocalizedWord("Свойства бюджета", lang),
						"budgets",
						"Provider?type=edit&element=document&id=budget&docid=${budget.getID()}"))
			}

			outline.addOutline(properties)
			list.add(properties)*/
		}

		publishElement("outline", outline)
	}
}
