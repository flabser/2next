nbApp.appendSaldoToElTitleByDocDate = function(el, ddbid) {
	nbApp.xhrGetSaldoOnDate(ddbid).then(function(result) {
		$(el).attr('title', result);
	});
};

$(document).ready(function() {
	$('.js_saldo_on_date', '.view').on('mouseover', function() {
		nbApp.appendSaldoToElTitleByDocDate(this, $(this).data('ddbid'));
		$(this).off('mouseover');
	});

	$('.action-delete', '.view').on('click', function(e) {
		nbApp.delSelectedDocument($(this).data('ddbid'));
		e.preventDefault();
	});
});

nbApp.expandCollapseCategory = function(el, id, cdoctype) {
	// js-entrywrap{@id}

	if (el.classList.contains('minus')) {
		nb.ajax({
			method : 'GET',
			datatype : 'html',
			url : 'Provider',
			data : {
				'type' : 'page',
				'id' : 'category-child',
				'parentdocid' : id,
				'parentdoctype' : cdoctype,
				'command' : 'expand`' + id + '`' + cdoctype
			}
		}).then(function() {
			console.log(this)
		});
	} else {
		nb.ajax({
			method : 'GET',
			url : 'Provider?type=page&id=category-child&command=collaps`' + id + '`' + cdoctype
		}).then(function() {
			console.log(this)
		});
	}
};
