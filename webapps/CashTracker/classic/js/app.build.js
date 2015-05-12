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

var nbApp = {
	isTouch : false,
	needDocsAction : ['delete_document'],
	allActionsNeedDocsEnabled : false
};

nbApp.init = function() {
	var md = new MobileDetect(window.navigator.userAgent);
	if (md.phone()) {
		$('body').addClass('phone');
		this.uiMakeTouch();
	} else if (md.tablet()) {
		$('body').addClass('tablet');
		this.uiMakeTouch();
	} else if (window.innerWidth <= 800) {
		$('body').addClass('phone');
		this.uiMakeTouch();
	}

	this.initScrollSpyActionBar();
	this.initScrollSpySide();
	this.initUI();
	this.uiToggleAvailableActions();
	$('body').removeClass('no_transition');
};

/*
 * uiMakeTouch
 */
nbApp.uiMakeTouch = function() {
	$('body').addClass('touch layout_fullscreen');
};

/*
 * uiToggleNavApp
 */
nbApp.uiToggleNavApp = function(e) {
	$('body').toggleClass('nav-app-open');
};

/*
 * uiToggleNavWorkspace
 */
nbApp.uiToggleNavWorkspace = function(e) {
	$('body').toggleClass('nav-ws-open');
};

/*
 * uiHideOpenedNav
 */
nbApp.uiHideOpenedNav = function(e) {
	$('body').removeClass('nav-app-open nav-ws-open');
};

nbApp.toggleSearchForm = function() {
	$('#search-form-block').toggleClass('search-open');
};

/*
 * uiToggleAvailableActions
 */
nbApp.uiToggleAvailableActions = function(e) {
	var hasSelectedDoc = $('[name=docid]:checked').length > 0;

	if (nbApp.allActionsNeedDocsEnabled && hasSelectedDoc) {
		return;
	}

	var actCount = nbApp.needDocsAction.length;
	for (var i = 0; i < actCount; i++) {
		if (hasSelectedDoc) {
			$('.action_' + nbApp.needDocsAction[i]).removeClass('disabled').removeAttr('disabled');
			nbApp.allActionsNeedDocsEnabled = true;
		} else {
			$('.action_' + nbApp.needDocsAction[i]).addClass('disabled').attr('disabled', 'disabled');
			nbApp.allActionsNeedDocsEnabled = false;
		}
	}
};

nbApp.parseXmlMessageToJson = function(xml) {
	return nb.utils.parseMessageToJson(xml); // $.xml2json(xml);
};

$(document).ready(function() {
	nbApp.init();
});

nbApp.attachInitFileUpload = function(elSelector) {

	$('#progress').css('display', 'none');

	if (typeof $.fn.fileupload === 'undefined') {
		return;
	}

	var $el = $(elSelector);
	if ($el.length === 0) {
		return;
	}

	$el.fileupload({
		url : 'Uploader',
		dataType : 'xml',
		send : function() {
			$('#progress').css('display', 'block');
		},
		done : function(e, data) {
			var $tpl = $('#template-rtfcontent-entry').children().clone();
			var msg = nbApp.parseXmlMessageToJson(data.result);

			if (typeof msg.message == 'undefined' || msg.message.length === 0) {
				$('.progress-bar', $('#progress').css('display', 'none')[0]).css('width', '0%');
				alert('file upload error: empty response message');
				return;
			}

			var filename = msg.message[0].toString();
			var hash = msg.message[1].toString();
			var formsesid = msg.message[3].toString(); // upload file id
			var fileUrl = 'Provider?type=getattach&formsesid=' + formsesid;
			fileUrl += '&field=rtfcontent&id=rtfcontent&file=' + filename;

			$tpl.appendTo('#attach-files');
			$tpl.append('<input type="hidden" name="filename" value="' + filename + '"/>');
			$tpl.append('<input type="hidden" name="fileid" value="' + formsesid + '"/>');
			$tpl.append('<input type="hidden" name="filehash" value="' + hash + '"/>');
			$('a.rtf-file', $tpl).attr({
				href : fileUrl,
				target : '_blank'
			}).text(filename);
			$('[data-click=add_comment]', $tpl).on('click', function() {
				nbApp.attachAddComment(this, hash);
			});
			$('[data-click=remove]', $tpl).on('click', function() {
				nbApp.attachRemove(this, formsesid, filename, hash, true);
			});

			setTimeout(function() {
				$('.progress-bar', $('#progress').css('display', 'none')[0]).css('width', '0%');
			}, 1000);
		},
		progressall : function(e, data) {
			var progress = parseInt(data.loaded / data.total * 100, 10);
			$('#progress .progress-bar').css('width', progress + '%').text(progress + '%');
		},
		fail : function() {
			$('.progress-bar', $('#progress').css('display', 'none')[0]).css('width', '0%');
			alert('file upload fail');
		}
	}).prop('disabled', !$.support.fileInput).parent().addClass($.support.fileInput ? undefined : 'disabled');

};

nbApp.attachPreview = function(el) {
	var $rtfe = $(el).parents('.rtf-entry');
	var $link = $rtfe.find('a.rtf-file').append('<i class="rtf-loading"></i>');

	$('<img class="attach-preview" style="display:none;">').attr('src', $link.attr('href')).on('load', function() {
		$(this).show();
		$('i.rtf-loading', $link).remove();
	}).on('error', function() {
		alert('load error');
		$('i.rtf-loading', $link).remove();
		$(this).remove();
	}).appendTo($rtfe);

	$(el).hide();
};

nbApp.attachAddComment = function(el, hash) {
	var $rtfe = $(el).parents('.rtf-entry');
	var $dlg = nb.dialog.show({
		message : $('<div><textarea rows="6"></textarea></div>').html(),
		title : 'Коментарий',
		buttons : {
			'Ok' : {
				text : 'Ok',
				click : function() {
					var comment = $('textarea', $dlg).val();
					if (comment.length === 0) {
						return false;
					}

					$('#frm').append('<input type="hidden" name="comment' + hash + '" value="' + comment + '">');
					$rtfe.find('a.rtf-file').after($('<span class="rtf-comment">').html(comment));

					$(el).hide();
					$dlg.dialog('close');
				}
			},
			'Cancel' : {
				text : 'Oтмена',
				click : function() {
					$dlg.dialog('close');
				}
			}
		}
	});
};

nbApp.attachRemove = function(el, formsesid, filename, hash, deleteEntry) {
	if (deleteEntry === true) {
		$(el).parents('.rtf-entry').remove();
	} else {
		$(el).parents('.rtf-entry').addClass('to-delete');
	}
	$('#' + hash).remove();

	$('#frm').append('<input type="hidden" name="deletertfcontentsesid" value="' + formsesid + '" />');
	$('#frm').append('<input type="hidden" name="deletertfcontentname" value="' + filename + '" />');
	$('#frm').append('<input type="hidden" name="deletertfcontentfield" value="rtfcontent" />');
};

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

nbApp.initUI = function() {

	$('.js-toggle-nav-app').mousedown(function(e) {
		e.preventDefault();
		nbApp.uiToggleNavApp();
	});

	$('.js-toggle-nav-ws').mousedown(function(e) {
		e.preventDefault();
		nbApp.uiToggleNavWorkspace();
	});

	if ($('.js-content-overlay')) {
		$('.js-content-overlay').mousedown(function(e) {
			e.preventDefault();
			nbApp.uiHideOpenedNav();
		});

		$('.js-content-overlay')[0].addEventListener('touchstart', function(e) {
			e.preventDefault();
			nbApp.uiHideOpenedNav();
		}, false);
	}

	$('[data-role="side-tree-toggle"]').click(function() {
		$(this).parent().toggleClass('side-tree-collapse');
	});

	$('[data-role="toggle"]').click(function() {
		$(this).parent().toggleClass($(this).data('toggle-class'));
	});

	// action
	$('.action_save_user_profile').click(function() {
		nbApp.saveUserProfile();
	});

	$('.action_save_and_close').click(function() {
		nbApp.saveDoc(frm.last_page.value);
	});

	$('.action_delete_document').click(function(e) {
		e.preventDefault();
		if ($(e.target).hasClass('disabled') === false) {
			nbApp.delSelectedDocument();
		}
		return false;
	});

	$('[name=docid]:checkbox').bind('change', function(e) {
		nbApp.uiToggleAvailableActions(e);
	});

	$('[data-toggle=docid]:checkbox').bind('click', function(e) {
		nbApp.uiToggleAvailableActions(e);
	});

	nbApp.attachInitFileUpload('#fileupload');

	//
	if (typeof $.fn.tabs !== 'undefined') {
		$('#tabs').tabs();
	}
};

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

nbApp.initScrollSpySide = function() {

	var offsetTop = 0;
	var sideOnTop = false;
	var $side = []; // $('.nav-app .side');

	if ($side.length) {
		offsetTop = $('.layout_header')[0].clientHeight;

		$(window).scroll(scrollSpySide);
		scrollSpySide();
	}

	function scrollSpySide() {
		if (window.pageYOffset > offsetTop) {
			if (!sideOnTop) {
				$side.css('margin-top', '0px');
				sideOnTop = true;
			}
		} else {
			if (sideOnTop) {
				$side.css('margin-top', '');
				sideOnTop = false;
			}
		}
	}
};

nbApp.initScrollSpyActionBar = function() {

	var offsetTop = 0;
	var isFixed = false;
	var $bar = $('.action-bar-top');

	if ($bar.length && $bar.find('.pagination, .btn').length) {
		var $parent = $bar.parent();
		$parent.css('min-height', $parent.height() + 'px');
		offsetTop = $bar[0].clientHeight + $parent.height();
		$bar.css('width', $bar[0].clientWidth);

		$(window).scroll(scrollSpyActionBar);
		scrollSpyActionBar();
	}

	function scrollSpyActionBar() {
		if (window.pageYOffset > offsetTop) {
			if (!isFixed) {
				$bar.addClass('action-bar-fixed');
				isFixed = true;
			}
		} else {
			if (isFixed) {
				$bar.removeClass('action-bar-fixed');
				isFixed = false;
			}
		}
	}
};

nbStrings = {
	'RUS' : {
		'yes' : 'Да',
		'no' : 'Нет',
		ok : 'Ok',
		cancel : 'Отмена',
		select : 'Выбрать',
		dialog_select_value : 'Вы не сделали выбор',
		no_selected_documents : 'Не выбран документ(ы)',
		search : 'Поиск',
		comment : 'Комментарий',
		document_saved : 'Документ сохранен',
		document_mark_as_read : 'Документ отмечен как прочтенный',
		wait_while_document_save : 'Пожалуйста ждите... идет сохранение документа',
		add_comment : 'Добавить комментарий',
		attention : 'Внимание'
	},
	'KAZ' : {
		'yes' : 'Ия',
		'no' : 'Жоқ',
		ok : 'Ok',
		cancel : 'Болдырмау',
		select : 'Таңдау',
		dialog_select_value : 'Вы не сделали выбор',
		no_selected_documents : 'Не выбран документ(ы)',
		search : 'Поиск',
		comment : 'Комментарий',
		document_saved : 'Документ сохранен',
		document_mark_as_read : 'Документ отмечен как прочтенный',
		wait_while_document_save : 'Пожалуйста ждите... идет сохранение документа',
		add_comment : 'Добавить комментарий',
		attention : 'Внимание'
	},
	'ENG' : {},
	'CHN' : {}
};

var calendarStrings = {
	'RUS' : {
		monthNames : ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь',
				'Ноябрь', 'Декабрь'],
		dayNamesMin : ['Вс', 'Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб']
	},
	'KAZ' : {
		monthNames : ['Қаңтар', 'Ақпан', 'Наурыз', 'Сәуір', 'Мамыр', 'Маусым', 'Шілде', 'Тамыз', 'Қыркүйек', 'Қазан',
				'Қараша', 'Желтоқсан'],
		monthNamesShort : ['Қаңтар', 'Ақпан', 'Наурыз', 'Сәуір', 'Мамыр', 'Маусым', 'Шілде', 'Тамыз', 'Қыркүйек',
				'Қазан', 'Қараша', 'Желтоқсан'],
		dayNames : ['жексебі', 'дүйсенбі', 'сейсенбі', 'сәрсенбі', 'бейсенбі', 'жұма', 'сенбі'],
		dayNamesShort : ['жек', 'дүй', 'сей', 'сәр', 'бей', 'жұм', 'сен'],
		dayNamesMin : ['Жс', 'Дс', 'Сс', 'Ср', 'Бс', 'Жм', 'Сн']
	},
	'ENG' : {
		monthNames : ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October',
				'November', 'December'],
		monthNamesShort : ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
		dayNames : ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
		dayNamesShort : ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
		dayNamesMin : ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],
		weekHeader : 'Wk'
	},
	'CHN' : {
		closeText : '关闭',
		prevText : '&#x3c;上月',
		nextText : '下月&#x3e;',
		currentText : '今天',
		monthNames : ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
		monthNamesShort : ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '十一', '十二'],
		dayNames : ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
		dayNamesShort : ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
		dayNamesMin : ['日', '一', '二', '三', '四', '五', '六'],
		weekHeader : '周',
		yearSuffix : '年'
	}
};

nbApp.saveUserProfile = function() {

	if ($('#newpwd').val() != $('#newpwd2').val()) {
		nb.dialog.warn({
			message : 'Введенные пароли не совпадают'
		});
		return;
	}

	nbApp.xhrSaveUserProfile($('form')[0]).then(function(xml) {
		var redir = $(xml).find('redirect').text();
		if (redir === '') {
			redir = $(xml).find('history').find('entry[type=page]:last').text();
		}
		$.cookie('lang', $('select[name=lang]').val(), {
			path : '/',
			expires : 30
		});
		$.cookie('refresh', $('select[name=refresh]').val(), {
			path : '/',
			expires : 30
		});
		$.cookie('skin', $('select[name=skin]').val(), {
			expires : 30
		});
		if (redir === '') {
			window.history.back();
		} else {
			window.location = redir;
		}
	}, function(xhr, ajaxOptions, thrownError) {
		if (xhr.status === 400) {
			if (xhr.responseText.indexOf('Old password has not match') != -1) {
				nb.dialog.warn({
					message : 'Некорректно заполнено поле [пароль по умолчанию]'
				});
			} else {
				nb.dialog.error({
					message : xhr.responseText
				});
			}
		}
	});
};

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

nbApp.dialogChoiceCategory = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "category",
		title : el.title,
		maxHeight : 500,
		minHeight : 440,
		width : 500,
		href : "Provider?type=page&id=picklist-category&page=1",
		onExecute : function() {
			if (nb.form.setValues(dlg, null)) {
				var selected = $("[data-type='select']:checked", dlg[0]);

				var type_op;
				var requireDocument;
				var requireCostCenter;

				if ($(selected[0]).hasClass("js-response")) {
					var _parent = $(selected[0]).parents(".js-parent");
					var parentCat = _parent.children("label:first");
					var parentCatName = parentCat.text();
					$("#categorytbl").html(parentCatName + " / " + $("#categorytbl").text());

					type_op = $("[name='viewtext3']", parentCat).val();
					requireDocument = $("[name='viewtext5']", parentCat).val() == "1";
					requireCostCenter = $("[name='viewtext6']", parentCat).val() == "1";
				} else {
					type_op = $("[data-id=" + selected[0].value + "][name='viewtext3']").val();
					requireDocument = $("[data-id=" + selected[0].value + "][name='viewtext5']").val() == "1";
					requireCostCenter = $("[data-id=" + selected[0].value + "][name='viewtext6']").val() == "1";
				}

				if (type_op == "in" || type_op == "out") {
					$("input[name=targetcash]").val("");
					$("#targetcashtbl").html("");
					$("#control-row-targetcash").hide();
				} else if (type_op == "transfer") {
					$("#control-row-targetcash").show();
					$("input[name=costcenter]").val("");
					$("#costcentertbl").html("");
				} else if (type_op == "calcstuff") {
					$("#control-row-targetcash").show();
					$("input[name=costcenter]").val("");
					$("#costcentertbl").html("");
				} else if (type_op == "getcash") {
					requireCostCenter = true;
				} else if (type_op == "withdraw") {

				}

				if (requireDocument) {
					$("[name=documented]").attr("required", "required").attr("checked", true);
					$("[name=documented]").attr("onclick", "return false");
					$("[name=documented]").attr("onkeydown", "return false");
				} else {
					$("[name=documented]").removeAttr("required").removeAttr("disabled");
					$("[name=documented]").attr("onclick", null);
					$("[name=documented]").attr("onkeydown", null);
				}

				if (requireCostCenter) {
					$("[name=costcenter]").attr("required", "required");
				} else {
					$("[name=costcenter]").removeAttr("required").removeAttr("disabled");
				}

				$("#subcategorytbl").html("");
				$("#typeoperationtbl").attr("class", "operation-type-icon-" + type_op).attr("title", type_op);
				$("input[name=typeoperation]").val(type_op);

				$("input[name=subcategory]").val("");

				dlg.dialog("close");
			}
		},
		buttons : {
			"ok" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		},
		close : function() {
			$("input[name=summa]").focus();
		}
	});
};

nbApp.dialogChoiceTargetCash = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "targetcash",
		title : el.title,
		href : "Provider?type=page&id=picklist-cash&page=1",
		buttons : {
			"ok" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		},
		close : function() {
			$("input[name=summa]").focus();
		}
	});
};

nbApp.dialogChoiceCostCenter = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "costcenter",
		title : el.title,
		href : "Provider?type=page&id=picklist-costcenter&page=1",
		buttons : {
			"ok" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		},
		close : function() {
			$("textarea[name=basis]").focus();
		}
	});
};

nbApp.dialogChoiceBossAndDemp = function(el, fieldName, isMulti) {
	var dlg = nb.dialog.show({
		effect : "shake",
		targetForm : el.form.name,
		fieldName : fieldName,
		dialogFilterListItem : ".tree-entry",
		title : el.title,
		maxHeight : 500,
		minHeight : 440,
		width : 500,
		href : "Provider?type=view&id=bossandemppicklist&page=1&fieldName=" + fieldName + "&isMulti=" + isMulti,
		onLoad : function() {
			if (isMulti === false) {
				$("[type='checkbox'][data-type='select']", dlg[0]).attr("type", "radio");
			}
		},
		buttons : {
			"select" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		}
	});
};

nbApp.dialogChoiceAccessRoles = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "accessroles",
		title : el.title,
		href : "Provider?type=view&id=picklist-roles&page=1",
		buttons : {
			"ok" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		}
	});
};

nbApp.dialogChoiceCategoryForFormula = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "formula",
		title : el.title,
		maxHeight : 500,
		minHeight : 440,
		width : 500,
		href : "Provider?type=page&id=picklist-category-formula&page=1",
		onExecute : function() {
			var selected = $("[data-type='select']:checked", dlg[0]);
			if (selected.length === 0) {
				return false;
			}

			var ddbid = selected.data("id");
			nb.ajax({
				url : "Provider?type=page&id=get-document-data",
				method : "GET",
				dataType : "json",
				data : {
					ddbid : ddbid,
					items : "comment"
				}
			}).then(function(result) {
				$("[name='formula']").val("#" + ddbid + "@" + result.comment + "#");
				dlg.dialog("close");
			});
		},
		buttons : {
			"ok" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		},
		close : function() {
			$("[name=formula]").focus();
		}
	});
};

nbApp.sendInvite = function(email) {
	nbApp.xhrSendInvite(email).then(function(res) {
		console.log(res);
	});
};

nbApp.oauth = function(providerID) {

	var config = {
		"vk" : {
			providerID : "vk",
			authorization : "https://oauth.vk.com/authorize",
			client_id : "4832372",
			response_type : "token",
			state : "",
			scope : "email",
			redirect_uri : "http://localhost:7777/CashTracker/Provider?type=page&id=alloperations"
		}
	};

	return new JSO(config[providerID]);
};

nbApp.xhrGetSaldoOnDate = function(ddbid) {
	return nb.ajax({
		method : "GET",
		url : "Provider?type=page&id=saldo_on_date&ddbid=" + ddbid
	});
};

nbApp.xhrGetSaldoSum = function() {
	return nb.ajax({
		method : "GET",
		url : "Provider?type=page&id=saldo-sum"
	});
};

nbApp.xhrSendInvite = function(email) {
	return nb.ajax({
		cache : false,
		method : "POST",
		url : "Provider?type=page&id=send-invite&invite_to=" + email + "&nocache=" + Date.now()
	});
};

/*
 * appendSaldoToElTitleByDocDate
 */
nbApp.appendSaldoToElTitleByDocDate = function(el, ddbid) {
	nbApp.xhrGetSaldoOnDate(ddbid).then(function(result) {
		$(el).attr("title", result);
	});
};

$(document).ready(function() {
	$(".js_saldo_on_date", ".view").on("mouseover", function() {
		nbApp.appendSaldoToElTitleByDocDate(this, $(this).data("ddbid"));
		$(this).off("mouseover");
	});
});

nbApp.wlc = {};

nbApp.wlc.init = function() {

	var $regForm = $('form[name=form-reg]');
	$regForm.submit(function(e) {
		e.preventDefault();
		nbApp.wlc.reg(this);
	});
	$('input', $regForm).blur(function() {
		if ($(this).attr('required')) {
			if ($(this).val()) {
				$(this).removeClass('invalid');
			}
		}
	});
	$('input', $regForm).focus(function() {
		$(this).removeClass('invalid');
		$(".reg-email-invalid,.reg-email-exists,.reg-pwd-weak").css("height", "0px");
	});
	//
	$('.js-ShowLoginForm').click(nbApp.wlc.loginFormOpen);

/*	$('.js-content-overlay')[0].addEventListener('touchstart', function(e) {
		e.preventDefault();
		nbApp.wlc.loginFormClose();
	}, false);

	$('.js-content-overlay').mousedown(function(e) {
		e.preventDefault();
		nbApp.wlc.loginFormClose();
	});*/

	if (location.hash === "#sign-in") {
		nbApp.wlc.loginFormOpen();
	}
};

nbApp.wlc.loginFormOpen = function() {
	$("body").addClass("login-form-open");
};

nbApp.wlc.loginFormClose = function() {
	$("body").removeClass("login-form-open");
};

nbApp.wlc.login = function(form) {
	form.submit();
};

nbApp.wlc.setLang = function(lang) {
	$.cookie('lang', lang);
	window.location.reload();
};

nbApp.wlc.reg = function(form) {
	if ($(form).hasClass("process")) {
		return false;
	}

	$('input', form).removeClass('invalid');
	$(".reg-email-invalid,.reg-email-exists,.reg-pwd-weak", form).css("height", "0px");
	$(form).addClass("process");

	nb.ajax({
		method : 'POST',
		datatype : 'text',
		url : 'Provider?client=' + screen.height + 'x' + screen.width,
		data : $(form).serialize(),
		success : function(result) {
			var pr = result.split(",");
			if (pr.indexOf("email") != -1) {
				$("input[name=email]", form).addClass("invalid");
				$(".reg-email-invalid").css("height", "auto");
			}
			if (pr.indexOf("user-exists") != -1) {
				$(".reg-email-exists", form).css("height", "auto");
			}
			if (pr.indexOf("pwd-weak") != -1) {
				$("input[name=pwd]", form).addClass("invalid");
				$(".reg-pwd-weak", form).css("height", "auto");
			}
			//
			var isReg = false;
			if (pr.indexOf("user-reg") != -1) {
				console.log("user-reg");
				isReg = true;
			}
			if (pr.indexOf("ok") != -1) {
				console.log("ok");
			}
			//
			if (false && isReg) {
				var $loginForm = $('form[name=login-form]');
				$("[name=login]", $loginForm).val($("input[name=email]", form).val());
				$("[name=pwd]", $loginForm).val($("input[name=pwd]", form).val());
				nbApp.wlc.login($loginForm[0]);
			}

			if (pr.indexOf("verify-email-send") != -1) {
				nb.utils.notify({
					type : "info",
					message : "Для завершения регистрации подтвердите свой email"
				}).show();
			}
		},
		error : function(err) {
			console.log(err);
		},
		complete : function() {
			$(form).removeClass("process");
		}
	});
};
