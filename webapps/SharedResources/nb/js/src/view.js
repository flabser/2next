/**
 * sendSortRequest
 */
nb.xhr.sendSortRequest = function(pageId, column, direction) {

	if (nb.debug === true) {
		console.log('nb.xhr.sendSortRequest: ', pageId, column, direction);
	}

	return nb.ajax({
		type : 'POST',
		datatype : 'XML',
		url : 'Provider?param=sorting_mode~on&param=sorting_column~' + column.toLowerCase()
				+ '&param=sorting_direction~' + direction.toLowerCase(),
		data : {
			'type' : 'service',
			'operation' : 'tune_session',
			'element' : 'page',
			'id' : pageId
		}
	});
};
