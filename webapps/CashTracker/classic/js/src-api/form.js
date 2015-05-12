nbApp.saveDoc = function(redirectUrl) {
	var def = nb.xhr.saveDocument({
		redirect : redirectUrl
	});
	return def;
};

nbApp.closeDoc = function(redirectUrl) {
	if (typeof redirectUrl !== 'undefined' && redirectUrl.length !== 0) {
		window.location.href = redirectUrl;
	} else {
		window.history.back();
	}
};

/*
 * clearFormField
 */
nbApp.clearFormField = function(fieldName) {
	nb.utils.clearField(fieldName);
};

/*
 * markRead
 */
nbApp.markRead = function(doctype, docid) {
	setTimeout(function() {
		nb.xhr.markDocumentAsRead(docid, doctype).then(function() {
			nb.utils.notify({
				message : nb.getText('document_mark_as_read')
			}).show().remove(1500);
		});
	}, 2000);
};

/*
 * usersWhichReadInTable
 */
nbApp.usersWhichReadInTable = function(el, doctype, id) {
	nb.xhr.getUsersWichRead(id, doctype).then(function(xml) {

		var $tblNode = $('#userswhichreadtbl');
		var row_cells = ['<tr><td>', '1', '</td><td>', '3', '</td></tr>'];

		$(xml).find('entry').each(function() {
			var username = $(this).attr('username');
			if (typeof username !== 'undefined') {
				if ($('td:contains("' + username + '")', $tblNode).length === 0) {
					row_cells[1] = username;
					row_cells[3] = $(this).attr('eventtime');
					$tblNode.append(row_cells.join(''));
				}
			}
		});
	});
};
