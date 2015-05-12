/*
 * getSelectedDocumentsIDs
 */
nbApp.getSelectedDocumentsIDs = function(checkboxName) {
	var $checked = $('input[name=' + (checkboxName || 'docid') + ']:checked');
	if ($checked.length === 0) {
		return [];
	}

	var result = [];
	$checked.each(function() {
		result.push($(this).attr('id'));
	});

	return result;
};

/*
 * getSelectedDocumentsAsParam
 */
nbApp.getSelectedDocumentsAsParam = function() {
	var ids = nbApp.getSelectedDocumentsIDs();
	if (ids.length === 0) {
		return '';
	}

	var result = [];
	for ( var id in ids) {
		result.push('docid=' + ids[id]);
	}

	return result.join('&');
};

/*
 * delSelectedDocument
 */
nbApp.delSelectedDocument = function(dbID, typedel) {

	var ids;
	if (dbID) {
		ids = ['docid=' + dbID];
	} else {
		ids = nbApp.getSelectedDocumentsAsParam();
	}

	if (ids.length === 0) {
		nb.dialog.warn({
			message : nb.getText('no_selected_documents', 'Не выбран документ для удаления')
		});
		return;
	}

	var def = nbApp.xhrDeleteDocument(ids);
	def.then(function(msg) {
		var deletedCount = $(msg).find('deleted').attr('count');
		var undeletedCount = $(msg).find('undeleted').attr('count');
		var deletedEntry = [];
		var undeletedEntry = [];

		$(msg).find('deleted').find('entry').not(':contains("undefined")').each(function() {
			deletedEntry.push('<li>' + $(this).text() + '</li>');
		});
		$(msg).find('undeleted').find('entry').not(':contains("undefined")').each(function() {
			undeletedEntry.push('<li>' + $(this).text() + '</li>');
		});

		var dlgTitle;
		if (deletedEntry.length === 0 || undeletedCount > 0) {
			dlgTitle = nb.getText('error_delete', 'Ошибка удаления');
		} else {
			dlgTitle = nb.getText('deletion_successful', 'Удаление завершено успешно');
		}

		var html = [];
		html.push('<div style=font-size:.9em;><b>Удалено:</b> ');
		html.push(deletedCount);
		html.push('<ul>');
		html.push(deletedEntry.join(''));
		html.push('</ul>');
		html.push('<b>Не удалено:</b> ');
		html.push(undeletedCount);
		html.push('<ul>');
		html.push(undeletedEntry.join(''));
		html.push('</ul></div>');

		var $dlg = nb.dialog.show({
			title : dlgTitle,
			message : html.join(''),
			buttons : {
				'Ok' : {
					text : nb.getText('ok'),
					click : function() {
						$dlg.dialog('close');
					}
				}
			},
			close : function() {
				location.reload();
			}
		});
	}, function() {
		nb.dialog.error({
			message : nb.getText('error_delete', 'Ошибка удаления')
		});
	});
};

/*
 * docAddToFav
 */
nbApp.docAddToFav = function(el, docid, doctype) {
	nb.xhr.addDocumentToFavorite(docid, doctype).then(function() {
		$(el).attr({
			'src' : '/SharedResources/img/iconset/star_full.png',
			'onclick' : 'nbApp.docRemoveFromFav(this, "' + docid + '", "' + doctype + '")'
		});
	}, function() {
		nb.dialog.error({
			message : 'Ошибка добавления в избранное'
		});
	});
};

/*
 * docRemoveFromFav
 */
nbApp.docRemoveFromFav = function(el, docid, doctype) {
	nb.xhr.removeDocumentFromFavorite(docid, doctype).then(function() {
		$(el).attr({
			'src' : '/SharedResources/img/iconset/star_empty.png',
			'onclick' : 'nbApp.docAddToFav(this, "' + docid + '", "' + doctype + '")'
		});
	}, function() {
		nb.dialog.error({
			message : 'Ошибка удаления из избранного'
		});
	});
};

/*
 * viewSortColumn
 */
nbApp.viewSortColumn = function(pageId, column, direction) {
	nb.xhr.sendSortRequest(pageId, column, direction).then(function() {
		window.location.reload();
	});
};

/*
 * viewThreadExpand
 */
nbApp.viewThreadExpand = function(id, cdoctype, pos, s) {
	nbApp.xhrDocThreadExpand(id, cdoctype).then(function(data) {
		$(data).insertAfter('.' + id);

		var href = 'javascript:nbApp.viewThreadCollapse("' + id + '", "' + cdoctype + '", "' + pos + '", "' + s + '")';
		$('#a' + id).attr('href', href);
		$('#img' + id).attr('src', '/SharedResources/img/classic/1/minus1.png');
	});
};

/*
 * viewThreadCollapse
 */
nbApp.viewThreadCollapse = function(id, cdoctype, pos, s) {
	nbApp.xhrDocThreadCollapse(id, cdoctype).then(function() {
		$('.' + id).next('tr').replaceWith('');
	});

	var href = 'javascript:nbApp.viewThreadExpand("' + id + '", "' + cdoctype + '", "' + pos + '" , "' + s + '")';
	$('#a' + id).attr('href', href);
	$('#img' + id).attr('src', '/SharedResources/img/classic/1/plus1.png');
};

/*
 * filterChoose
 */
nbApp.filterChoose = function(keyword, column) {
	nb.xhr.chooseFilter(page_id.value, column, keyword).then(function() {
		window.location.reload();
	});
};

/*
 * filterReset
 */
nbApp.filterReset = function() {
	nb.xhr.resetFilter(page_id.value).then(function() {
		window.location.reload();
	});
};

/*
 * filterResetCurrent
 */
nbApp.filterResetCurrent = function(column) {
	nb.xhr.resetCurrentFilter(page_id.value, column).then(function() {
		window.location.reload();
	});
};

function toggleFilterList(el) {
	$(el).parents('.filter-entry-list-wrapper').find('.filter-entry-list').toggleClass('visible');
}
