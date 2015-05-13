/**
 * deleteDocument
 */
nb.xhr.deleteDocument = function(ck, typeDel) {

	if (nb.debug === true) {
		console.log('nb.xhr.deleteDocument: ', ck, typeDel);
	}

	return nb.ajax({
		type : 'POST',
		datatype : 'XML',
		url : 'Provider',
		data : {
			'type' : 'delete',
			'ck' : ck,
			'typedel' : typeDel
		}
	});
};

/**
 * restoreDeletedDocument
 */
nb.xhr.restoreDeletedDocument = function(ck) {

	if (nb.debug === true) {
		console.log('nb.xhr.restoreDeletedDocument: ', ck);
	}

	return nb.ajax({
		type : 'POST',
		datatype : 'XML',
		url : 'Provider',
		data : {
			'type' : 'undelete',
			'ck' : ck
		}
	});
};

/**
 * addDocumentToFavorite
 */
nb.xhr.addDocumentToFavorite = function(docId, docType) {

	if (nb.debug === true) {
		console.log('nb.xhr.addDocumentToFavorite: ', docId, docType);
	}

	return nb.ajax({
		type : 'POST',
		datatype : 'XML',
		url : 'Provider',
		data : {
			'type' : 'service',
			'operation' : 'add_to_favourites',
			'doctype' : docType,
			'key' : docId
		}
	})
};

/**
 * removeDocumentFromFavorite
 */
nb.xhr.removeDocumentFromFavorite = function(docId, docType) {

	if (nb.debug === true) {
		console.log('nb.xhr.removeDocumentFromFavorite: ', docId, docType);
	}

	return nb.ajax({
		type : 'POST',
		datatype : 'XML',
		url : 'Provider',
		data : {
			'type' : 'service',
			'operation' : 'remove_from_favourites',
			'doctype' : docType,
			'key' : docId
		}
	});
};

/**
 * markDocumentAsRead
 */
nb.xhr.markDocumentAsRead = function(docId, docType) {

	if (nb.debug === true) {
		console.log('nb.xhr.markDocumentAsRead: ', docId, docType);
	}

	return nb.ajax({
		type : 'POST',
		datatype : 'XML',
		url : 'Provider',
		data : {
			'type' : 'service',
			'operation' : 'mark_as_read',
			'id' : 'mark_as_read',
			'doctype' : docType,
			'key' : docId
		}
	});
};

/**
 * getUsersWichRead
 */
nb.xhr.getUsersWichRead = function(docId, docType) {

	if (nb.debug === true) {
		console.log('nb.xhr.getUsersWichRead: ', docId, docType);
	}

	return nb.ajax({
		type : 'GET',
		datatype : 'XML',
		url : 'Provider',
		data : {
			'type' : 'service',
			'operation' : 'users_which_read',
			'id' : 'users_which_read',
			'doctype' : docType,
			'key' : docId
		}
	});
};

/**
 * saveDocument
 */
nb.xhr.saveDocument = function(options) {

	options = options || {};
	var notify = nb.utils.notify({
		message : nb.getText('wait_while_document_save', 'Пожалуйста ждите... идет сохранение документа'),
		type : 'process'
	}).show();

	var xhrArgs = {
		cache : false,
		type : 'POST',
		datatype : 'XML',
		url : 'Provider',
		data : options.data || $('form').serialize(),
		beforeSend : function() {
			nb.utils.blockUI();
			$('.required, [required]', 'form').removeClass('required').removeAttr('required');
		},
		success : function(xml) {
			var jmsg = nb.utils.parseMessageToJson(xml);
			var msgText = jmsg.message[0];
			if (jmsg.status === 'ok') {
				notify.set({
					'text' : nb.getText('document_saved', 'Документ сохранен'),
					'type' : 'success'
				});
				//
				if (msgText.length > 0) {
					nb.dialog.info({
						message : msgText,
						close : function() {
							if (jmsg.redirect || options.redirect) {
								window.location.href = jmsg.redirect || options.redirect;
							}
						}
					});
				} else {
					if (jmsg.redirect || options.redirect) {
						setTimeout(function() {
							window.location.href = jmsg.redirect || options.redirect;
						}, 300);
					}
				}
			} else {
				if (msgText.indexOf('require:') === 0) {
					var fields = msgText.substr('require:'.length).split(',');
					for (i = 0; i < fields.length; i++) {
						$('#' + fields[i] + 'tbl').addClass('required');
						$('[name=' + fields[i] + ']').attr('required', 'required').addClass('required');
					}
					notify.set({
						'text' : nb.getText('required_field_not_filled', 'Не заполнены обязательные поля'),
						'type' : 'error'
					});
				} else {
					notify.set({
						'text' : msgText,
						'type' : 'error'
					});
				}
			}
		},
		error : function() {
			notify.set({
				'text' : nb.getText('error_xhr', 'Ошибка при выполнении запроса'),
				'type' : 'error'
			});
		}
	};

	var def = nb.ajax(xhrArgs);
	def.always(function() {
		nb.utils.unblockUI();
		notify.remove(2000);
	});

	return def;
};

/**
 * parseMessageToJson
 */
nb.utils.parseMessageToJson = function(xml) {

	var msg = {};
	$(xml).find('response').each(function(it) {
		msg.status = $(this).attr('status');
		msg.redirect = $('redirect', this).text();
		msg.message = [];
		$(this).find('message').each(function(it) {
			msg.message.push($(this).text());
		});
	});
	return msg;
};
