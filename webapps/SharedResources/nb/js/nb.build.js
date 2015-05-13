/**
 * @author Medet
 */

'use strict';

var nb = {
	APP_NAME : location.hostname,
	LANG_ID : 'RUS',
	debug : true,
	strings : {
		'yes' : 'Да',
		'no' : 'Нет',
		ok : 'Ok',
		cancel : 'Отмена',
		select : 'Выбрать',
		dialog_select_value : 'Вы не сделали выбор'
	},
	form : {},
	dialog : {},
	utils : {},
	xhr : {}
};

var nbApp = {/* local application namespace */};

/**
 * ajax
 */
nb.ajax = function(options) {

	var deferred = $.ajax(options);

	// error
	deferred.error(function(xhr) {
		console.error('nb.ajax : error', xhr);

		if (xhr.status == 400) {
			nb.dialog.error({
				title : nb.getText('error_xhr', 'Ошибка запроса'),
				message : xhr.responseText
			});
		}

		return xhr;
	});

	return deferred;
};

/**
 * getText
 */
nb.getText = function(stringKey, defaultText, langId) {
	if (nbStrings[langId || this.LANG_ID][stringKey]) {
		return nbStrings[langId || this.LANG_ID][stringKey];
	} else {
		return (defaultText !== undefined) ? defaultText : stringKey;
	}
};

/**
 * openXML
 */
nb.openXML = function() {
	window.location.href = window.location + '&onlyxml';
};

/**
 * setValues
 */
nb.form.setValues = function(currentNode) {

	var $dlgw = $(currentNode).parents('[role=dialog]');
	var $dlgWgt = $('[data-role="nb-dialog"]', $dlgw);

	var form = nb.utils.getForm($dlgWgt[0].dialogOptions.targetForm);
	var fieldName = $dlgWgt[0].dialogOptions.fieldName;

	var nodeList; // коллекция выбранных
	var isMulti = false;
	var itemSeparate = '';
	var displaySeparate = '<br/>'; // отобразить мульти значения разделителем

	if (!form) {
		nb.dialog.warn({
			title : 'Error',
			message : 'Error nb.form.setValues > form is not found: ' + form
		});
		return false;
	}

	nodeList = $('[data-type="select"]:checked', $dlgWgt[0]);
	if (nodeList.length > 0) {
		isMulti = nodeList.length > 1;
		if (!isMulti) {
			itemSeparate = '';
		}

		return _writeValues(nodeList);
	} else {
		if ($dlgWgt[0].dialogOptions.effect) {
			$dlgw.stop();
			$dlgw.effect($dlgWgt[0].dialogOptions.effect, {
				times : 2
			}, 300);
		}

		if ($('.js-no-selected-value', $dlgw[0]).length === 0) {
			(function() {
				var $_html = $('<div class="alert alert-danger js-no-selected-value" '
						+ 'style="border-radius:2px;top:55%;left:4%;right:4%;position:absolute;">'
						+ $dlgWgt[0].dialogOptions.errorMessage + '</div>');
				$dlgWgt.after($_html);
				setTimeout(function() {
					$_html.fadeOut({
						always : function() {
							$_html.remove();
						}
					});
				}, 800);
			})();
		}

		return false;
	}

	// write values to form
	function _writeValues() {
		if (isMulti) {
			$('[name=' + fieldName + ']', form).remove();
			var htm = [];
			nodeList.each(function(index, node) {
				$('<input type="hidden" name="' + fieldName + '" value="' + node.value + '" />').appendTo(form);
				htm.push('<li>' + $(node).data('text') + '</li>');
			});
			$('#' + fieldName + 'tbl').html(htm.join(''));
		} else {
			var $fieldNode = $('[name=' + fieldName + ']', form);
			if ($fieldNode.length === 0) {
				$fieldNode = $('<input type="hidden" name="' + fieldName + '" />');
				$(form).append($fieldNode[0]);
			}

			$fieldNode.val(nodeList[0].value);
			$('#' + fieldName + 'tbl').html('<li>' + nodeList.attr('data-text') + '</li>');
		}

		return true;
	}
};

/**
 * clearField
 */
nb.utils.clearField = function(fieldName, context) {
	$('[name=' + fieldName + ']').val('');
	$('#' + fieldName + 'tbl').html('');
};

/**
 * getForm
 */
nb.utils.getForm = function(el) {
	if (el === null || el === undefined) {
		return el;
	}

	if (typeof (el) === 'string' && (document[el] && document[el].nodeName === 'FORM')) {
		return document[el];
	}

	return el.form || el;
};

/**
 * blockUI
 */
nb.utils.blockUI = function() {
	var $el = $('#nb-block-ui');
	if ($el.length === 0) {
		$el = $('<div id="nb-block-ui" style="background:rgba(0,0,0,0.1);position:fixed;top:0;left:0;bottom:0;right:0;z-index:999;"/>');
		$el.appendTo('body');
	}

	$el.css('display', 'block');
};

/**
 * unblockUI
 */
nb.utils.unblockUI = function() {
	$('#nb-block-ui').css('display', 'none');
};

/**
 * notify
 */
nb.utils.notify = function(opt) {

	var $nwrap = $('#nb-notify-wrapper');
	if (!$nwrap.length) {
		$nwrap = $('<div id="nb-notify-wrapper" class="nb-notify"></div>').appendTo('body');
	}
	var $el = $('<div class="nb-notify-entry-' + (opt.type || 'info') + '">' + opt.message + '</div>').appendTo($nwrap);

	return {
		show : function() {
			$el.css('display', 'block');
			return this;
		},
		hide : function() {
			$el.css('display', 'none');
			return this;
		},
		set : function(opt) {
			for ( var key in opt) {
				if (key === 'text') {
					$el.text(opt[key]);
				} else if (key === 'type') {
					$el.attr('class', 'nb-notify-entry-' + opt[key]);
				}
			}
			return this;
		},
		remove : function(timeout, callback) {
			if ($el === null) {
				return;
			}

			if (timeout && timeout > 0) {
				var _this = this;
				setTimeout(function() {
					$el.remove();
					$el = null;
					callback && callback();
				}, timeout);
			} else {
				$el.remove();
				$el = null;
				callback && callback();
			}
		}
	};
};

$(document).ready(function() {
	nb.LANG_ID = $.cookie('lang') || 'RUS';

	$(':checkbox').bind('click', function() {
		var $checkbox = $(this);

		if (!$checkbox.data('toggle')) {
			return true;
		}

		var name = this.name || $checkbox.data('toggle');
		var $el = $('[name=' + name + ']:checkbox:visible');

		if ($checkbox.is(':checked')) {
			$el.each(function() {
				this.checked = true;
			});
		} else {
			$el.each(function() {
				this.checked = false;
			});
		}
	});
});

/**
 * dialog
 */
nb.dialog = {
	_props : {
		title : nb.APP_NAME
	},
	info : function(opt) {
		opt.className = 'dialog-info';
		opt.width = opt.width || '360';
		opt.height = opt.height || '210';
		opt.buttons = opt.buttons || {
			'Ok' : function() {
				$(this).dialog('close');
			}
		};

		return this.show(opt);
	},
	warn : function(opt) {
		opt.className = 'dialog-warn';
		opt.width = opt.width || '360';
		opt.height = opt.height || '210';
		opt.buttons = opt.buttons || {
			'Ok' : function() {
				$(this).dialog('close');
			}
		};

		return this.show(opt);
	},
	error : function(opt) {
		opt.className = 'dialog-error';
		opt.width = opt.width || '360';
		opt.height = opt.height || '210';
		opt.buttons = opt.buttons || {
			'Ok' : function() {
				$(this).dialog('close');
			}
		};

		return this.show(opt);
	},
	execute : function(dlgInnerNode) {
		var $dlgw = $(dlgInnerNode).parents('[role=dialog]');
		var $dlgWgt = $('[data-role=nb-dialog]', $dlgw);

		$dlgWgt[0].dialogOptions.onExecute(arguments);
	},
	show : function(options) {
		var $dialog;

		options.id = options.id || null;
		options.title = options.title || this._props.title;
		options.href = options.href || null;
		options.className = options.className || '';
		options.message = options.message || null;
		options.filter = options.filter;
		options.dialogFilterListItem = options.dialogFilterListItem || 'li';
		options.buttons = options.buttons || null;
		options.dialogClass = 'nb-dialog ' + (options.dialogClass ? options.dialogClass : '');
		options.errorMessage = options.errorMessage || nb.strings.dialog_select_value;

		options.onLoad = options.onLoad || null;
		options.onExecute = options.onExecute || function() {
			if (nb.form.setValues($dialog, null)) {
				$dialog.dialog('close');
			}
		};

		options.autoOpen = true;
		if (options.modal === false) {
			options.modal = false;
		} else {
			options.modal = true;
		}
		options.width = options.width || '360';
		// options.height = options.height || '420';
		options.position = options.position || 'center';

		if (options.id === null && options.href) {
			options.id = 'dlg_' + options.href.replace(/[^a-z0-9]/gi, '');

			$dialog = $('#' + options.id);
			if ($dialog[0]) {
				if ($dialog.dialog('isOpen') === true) {
					return;
				} else {
					$dialog.dialog('open');
					return;
				}
			}
		} else if (options.id !== null) {
			$dialog = $('#' + options.id);
			if ($dialog[0]) {
				if ($dialog.dialog('isOpen') === true) {
					return;
				} else {
					$dialog.dialog('open');
					return;
				}
			}
		}

		if (options.id === null) {
			options.close = options.close || function() {
				$dialog.dialog('destroy');
				$dialog.remove();
			};
		}

		var $dlgContainer;

		if (options.href) {
			$dlgContainer = $('<div data-role="nb-dialog" id="' + options.id + '" class="nb-dialog-container '
					+ options.className + '"><div class="loading-state"></div></div>');
		} else {
			if (options.id) {
				$dlgContainer = $('<div data-role="nb-dialog" id="' + options.id + '" class="nb-dialog-container '
						+ options.className + '">' + options.message + '</div>');
			} else {
				$dlgContainer = $('<div data-role="nb-dialog" class="nb-dialog-container ' + options.className + '">'
						+ options.message + '</div>');
			}
		}

		if (options.href) {
			$dialog = $dlgContainer.load(options.href, '', function(response, status, xhr) {
				if (status === 'error') {
					$dlgContainer.html('<div class="alert alert-danger">' + status + '</div>');

					console.log('nb.dialog : load callback', xhr);
				} else {
					try {
						if (options.onLoad !== null) {
							options.onLoad(response, status, xhr);
						}
					} catch (e) {
						console.log('nb.dialog', e);
					}

					try {
						if (options.filter !== false) {
							new nb.dialog.Filter($dlgContainer, options.dialogFilterListItem, 13);
						}
					} catch (e) {
						console.log('nb.dialog', e);
					}
				}
			}).dialog(options);

			$dialog.on('click', 'a', function(e) {
				e.preventDefault();
				$dlgContainer.load(this.href);
			});

			$dialog.on('change', 'select', function(e) {
				e.preventDefault();
				$dlgContainer.load(this.href);
			});
		} else {
			$dialog = $dlgContainer.dialog(options);
		}

		$dialog[0].dialogOptions = options;

		if (nb.debug === true) {
			console.log('nb.dialog: ', options);
		}

		return $dialog;
	}
};

/**
 * nb.dialog.Filter
 */
nb.dialog.Filter = function(_containerNode, _filterNode, _initCount, _triggerLen) {

	var inputEl = null;
	var initCount = _initCount || 13;
	var triggerLen = _triggerLen || 2;
	var timeout = 300;
	var to = null;
	var enabledViewSearch = false;
	var filterNode = _filterNode || '.item';
	var $containerNode = _containerNode;
	var $dlgw = $containerNode.parents('[role=dialog]');
	var $collection;

	init();

	function init() {
		$collection = $(filterNode, $containerNode[0]);

		var isHierarchical = $('.toggle-response', $containerNode[0]).length > 0;
		if ($collection.length < initCount) {
			if (!isHierarchical) {
				return;
			}
		}

		if ($('.dialog-filter', $dlgw).length === 0) {
			$containerNode.before('<div class="dialog-filter"></div>');
		}

		$('.dialog-filter', $dlgw).append(
				'<label>Фильтр: <label><input type="text" name="keyword" data-role="search" />');

		inputEl = $('.dialog-filter input[data-role=search]', $dlgw);
		inputEl.on('keyup', function(e) {
			try {
				clearTimeout(to);
				if (e.keyCode === 13) {
					return;
				}
			} catch (ex) {
				console.log(ex);
			}

			to = setTimeout(function() {
				$collection = $(filterNode, $containerNode[0]);
				filter(e.target.value);
			}, timeout);
		});
	}

	function filter(value) {
		try {
			if (value.length >= triggerLen) {
				var hiddenCount = 0;
				$collection.attr('style', '');

				var re = new RegExp(value, 'gim');

				$collection.each(function(index, node) {
					if (!re.test(node.textContent)) {
						if ($(':checked', node).length === 0) {
							$(node).attr('style', 'display:none;');
							hiddenCount++;
						}
					}
				});

				if ($collection.length > hiddenCount) {
					inputEl.attr('title', 'By keyword [' + value + '] filtered ' + ($collection.length - hiddenCount));
				} else {
					inputEl.attr('title', 'filter_no_results');
				}
			} else {
				$collection.attr('style', '');
				inputEl.attr('title', '');
			}
		} catch (e) {
			console.log(e);
		}
	}
};

/**
 * windowOpen
 */
nb.windowOpen = function(url, id, callbacks) {
	var features, width = 800, height = 600;
	var top = (window.innerHeight - height) / 2, left = (window.innerWidth - width) / 2;
	if (top < 0) top = 0;
	if (left < 0) left = 0;
	features = 'top=' + top + ',left=' + left;
	features += ',height=' + height + ',width=' + width + ',resizable=yes,scrollbars=yes,status=no';

	var wid = 'window-' + (id || url.hashCode());

	var w = window.open('', wid, features);
	if ('about:blank' === w.document.URL || w.document.URL === '') {
		w = window.open(url, wid, features);

		if (callbacks && callbacks.onclose) {
			var timer = setInterval(function() {
				if (w.closed) {
					clearInterval(timer);
					callbacks.onclose();
				}
			}, 1000);
		}
	}
	w.focus();
};

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

var nbStrings = {
	'RUS' : {},
	'KAZ' : {},
	'ENG' : {},
	'CHN' : {}
};

nbStrings.RUS = {
	'yes' : 'Да',
	'no' : 'Нет',
	ok : 'Ok',
	cancel : 'Отмена',
	select : 'Выбрать',
	dialog_select_value : 'Вы не сделали выбор'
};

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
