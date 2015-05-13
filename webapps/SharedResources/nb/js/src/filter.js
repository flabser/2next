/**
 * chooseFilter
 */
nb.xhr.chooseFilter = function(pageId, column, keyword) {

	if (nb.debug === true) {
		console.log('nb.xhr.chooseFilter: ', pageId, column, keyword);
	}

	return nb.ajax({
		type : 'GET',
		datatype : 'XML',
		url : 'Provider?param=filter_mode~on&param=filtered_column~' + column + '&param=key_word~' + keyword,
		cache : false,
		data : {
			'type' : 'service',
			'operation' : 'tune_session',
			'element' : 'page',
			'id' : pageId
		}
	});
};

/**
 * resetFilter
 */
nb.xhr.resetFilter = function(pageId) {

	if (nb.debug === true) {
		console.log('nb.xhr.resetFilter: ', pageId);
	}

	return nb.ajax({
		type : 'POST',
		datatype : 'XML',
		url : 'Provider',
		cache : false,
		data : {
			'type' : 'service',
			'operation' : 'tune_session',
			'element' : 'page',
			'id' : pageId,
			'param' : 'filter_mode~reset_all'
		}
	});
};

/**
 * resetCurrentFilter
 */
nb.xhr.resetCurrentFilter = function(pageId, column) {

	if (nb.debug === true) {
		console.log('nb.xhr.resetCurrentFilter: ', pageId, column);
	}

	return nb.ajax({
		type : 'GET',
		datatype : 'XML',
		url : 'Provider?param=filter_mode~on&param=filtered_column~' + column,
		cache : false,
		data : {
			'type' : 'service',
			'operation' : 'tune_session',
			'element' : 'page',
			'id' : pageId
		}
	});
};
