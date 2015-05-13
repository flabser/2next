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
