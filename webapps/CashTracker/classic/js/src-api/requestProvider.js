nbApp.xhrSaveUserProfile = function(formNode) {
	return nb.ajax({
		type : 'POST',
		url : 'Provider?type=save&element=user_profile',
		datatype : 'html',
		data : $(formNode).serialize()
	});
};

nbApp.xhrDeleteDocument = function(docIds) {
	return nb.ajax({
		type : 'POST',
		datatype : 'XML',
		url : 'Provider?type=page&id=delete_document&nocache=' + Date.now(),
		data : docIds
	});
};

nbApp.xhrDocThreadExpand = function(id, cdoctype) {
	return nb.ajax({
		method : 'GET',
		datatype : 'html',
		url : 'Provider',
		data : {
			'type' : 'view',
			'id' : 'docthread',
			'parentdocid' : id,
			'parentdoctype' : cdoctype,
			'command' : 'expand`' + id + '`' + cdoctype
		}
	});
};

nbApp.xhrDocThreadCollapse = function(id, cdoctype) {
	return nb.ajax({
		method : 'GET',
		url : 'Provider?type=view&id=docthread&command=collaps`' + id + '`' + cdoctype
	});
};
