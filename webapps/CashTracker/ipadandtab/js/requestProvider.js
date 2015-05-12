nbApp.deleteDocument = function(docIds) {
	return nb.ajax({
		type : "POST",
		datatype : "XML",
		url : "Provider?type=page&id=delete_document&nocache=" + Math.random() * 300,
		data : docIds
	});
};

nbApp.getSaldoOnDate = function(ddbid) {
	return nb.ajax({
		method : "GET",
		url : "Provider?type=page&id=saldo_on_date&ddbid=" + ddbid
	});
};
