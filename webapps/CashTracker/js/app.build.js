var nbApp = {
	isTouch : false,
	needDocsAction : ['delete_document'],
	allActionsNeedDocsEnabled : false
};

nbApp.init = function() {
	var md = new MobileDetect(window.navigator.userAgent);
	if (md.phone()) {
		this.uiMakeTouch('phone');
	} else if (md.tablet()) {
		this.uiMakeTouch('tablet');
	} else if (window.innerWidth <= 800) {
		this.uiMakeTouch('phone');
	}

	this.initScrollSpyActionBar();
	this.initScrollSpySide();
	this.initUI();
	this.uiToggleAvailableActions();
	$('body').removeClass('no_transition');
	$('#main-load').css('display', 'none');
};

nbApp.uiWindowResize = function() {
	if (window.innerWidth <= 800) {
		this.uiMakeTouch('phone');
	} else {
		$('body').removeClass('phone');
	}
};

/*
 * uiMakeTouch
 */
nbApp.uiMakeTouch = function(device) {
	$('body').addClass(device);
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
	$('body').toggleClass('search-open');
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
	return nb.utils.parseMessageToJson(xml);
};

$(document).ready(function() {
	nbApp.init();
	$('#main-load').css({
		'background-color' : 'rgba(255,255,255,.5)'
	});

	window.onunload = window.onbeforeunload = function() {
		$('#main-load').show();
	};
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

	$('#toggle-nav-app').mousedown(function(e) {
		e.preventDefault();
		nbApp.uiToggleNavApp();
	});

	$('#toggle-nav-ws').mousedown(function(e) {
		e.preventDefault();
		nbApp.uiToggleNavWorkspace();
	});

	$('#toggle-head-search').click(function() {
		nbApp.toggleSearchForm();
	});

	$('#search-close').mousedown(function() {
		nbApp.toggleSearchForm();
	});

	if ($('#content-overlay')) {
		$('#content-overlay').mousedown(function(e) {
			e.preventDefault();
			nbApp.uiHideOpenedNav();
		});

		$('#content-overlay')[0].addEventListener('touchstart', function(e) {
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

	$('.js-swipe-entry').on("touchend", function(e) {
		nbApp.touch.touchEnd(e);
	});
	$('.js-swipe-entry').on("touchmove", function(e) {
		nbApp.touch.touchMove(e);
	});
	$('.js-swipe-entry').bind("touchstart", function(e) {
		nbApp.touch.touchStart(e);
	});

	$('.entries').on("touchend", function(e) {
		// nbApp.touchEnd(e);
	});
	$('.entries').on("touchmove", function(e) {
		// nbApp.touchMove(e);
	});
	$('.entries').bind("touchstart", function(e) {
		// nbApp.touchStart(e);
	});
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
	var $side = $('.nav-app');

	if ($side.length) {
		offsetTop = $('.layout_header')[0].clientHeight;

		$(window).scroll(scrollSpySide);
		scrollSpySide();
	}

	function scrollSpySide() {
		if (window.pageYOffset > offsetTop) {
			if (!sideOnTop) {
				$side.css('top', '0px');
				sideOnTop = true;
			}
		} else {
			if (sideOnTop) {
				$side.css('top', '');
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

;
$(function() {
	nbApp.touch = {};

	var touch = nbApp.touch;
	var t = 150;
	var i = 80;
	var n = 200;
	var s = 70;
	var a = 10;
	var o = 10;
	var r = true;
	var l = "js-swipe";

	touch.touchStart = function(touchEvent) {
		if (1 === touchEvent.originalEvent.touches.length) {
			touch.tapSwipedMessage = Boolean(touch.swipedMsg);
			touch.hideSwipedMsg();
			touch.resetValue(touchEvent);
		}
	};

	touch.touchMove = function(touchEvent) {
		var changedTouch = touchEvent.originalEvent.changedTouches[0];
		if (touch.isValidTouch(touchEvent)) {
			touch.delta = touch.x - changedTouch.pageX;
			touch.defineUserAction(changedTouch);
			if (touch.startSwipe) {
				if (!touch.startSwipeTriggered) {
					touch.startSwipeTriggered = true;
				}
				r && touch.move(touchEvent.currentTarget);
				touchEvent.preventDefault();
			}
		}
	};

	touch.touchEnd = function(touchEvent) {
		if (touch.isValidTouch(touchEvent, true) && touch.startSwipe) {
			touch.swipedMsg = touchEvent.currentTarget;
			if (touch.delta > i || new Date - touch.startTime < n) {
				// qu.animations.setStyle(touch.swipedMsg, -s, 0, t);

				touch.swipedMsg.style.cssText = '';

				var parent = $(touch.swipedMsg).parent('.entry-wrap');
				parent.addClass('entry-action-open');

				touch.swipedMsg.classList.add(l);
			} else {
				touch.hideSwipedMsg();
			}
			touchEvent.stopPropagation();
			touchEvent.preventDefault()
		}
	};

	touch.hideSwipedMsg = function() {
		if (touch.swipedMsg) {
			var i = touch.swipedMsg;
			setTimeout(function() {
				i.classList.remove(l);
				i = null
			}, t);
			// qu.animations.setStyle(touch.swipedMsg, 0, 0, t);

			touch.swipedMsg.style.cssText = '';

			var parent = $(touch.swipedMsg).parent('.entry-wrap');
			parent.removeClass('entry-action-open');

			touch.swipedMsg = null
		}
	};

	touch.resetValue = function(t) {
		var i = t.originalEvent.changedTouches[0];
		touch.touchId = i.identifier;
		touch.startTime = new Date;
		touch.startSwipe = !1;
		touch.startScroll = !1;
		touch.delta = 0;
		touch.x = i.pageX;
		touch.y = i.pageY;
		touch.startSwipeTriggered = !1
	};

	touch.bindEvents = function(t, i) {
		t["touchstart " + i] = touch.touchStart;
		t["touchend " + i] = touch.touchEnd;
		t["touchmove " + i] = touch.touchMove
	};

	touch.defineUserAction = function(t) {
		Math.abs(touch.y - t.pageY) > o && !touch.startSwipe ? touch.startScroll = !0 : touch.delta > a
				&& !touch.startScroll && (touch.startSwipe = !0)
	};

	touch.isValidTouch = function(t, i) {
		var n = i ? "changedTouches" : "targetTouches";
		return t.originalEvent[n][0].identifier === touch.touchId
	};

	touch.move = function(el) {
		var i = Math.min(-touch.delta, 0);
		-s > i && (i = -s + (s + i) / 8);
		// qu.animations.setStyle(el, i, 0, 0)

		el.style.cssText = 'transition:transform 0ms ease-in-out;-moz-transform:translate3d(' + i + 'px,0,0);-webkit-transform:translate3d(' + i + 'px,0,0);transform:translate3d(' + i + 'px,0,0)';
	};
});
;
$(function() {

	var isShow = false;
	var clickYOffset = 0;
	var click = false;
	var arrowUp = '▲';
	var arrowDown = '▼';

	var to_top = $('<div class="to_top"><div class="to_top-panel"><div class="to_top-button"></div></div></div>');

	$('body').append(to_top);

	var btn = $('.to_top-button', to_top);

	$('.to_top-panel', to_top).click(function() {
		click = true;

		if (!clickYOffset) {
			clickYOffset = window.pageYOffset;
			btn.html(arrowDown);
			$.scrollTo($('body'), 100, {
				axis : 'y'
			});
		} else {
			btn.html(arrowUp);
			$.scrollTo($('body'), 100, {
				axis : 'y',
				offset : clickYOffset
			});
			clickYOffset = 0;
		}
	});

	$(document).bind('mousewheel', function(event, delta) {
		if (click) {
			click = false;
			clickYOffset = 0;
			btn.html(arrowUp);

			if (window.pageYOffset == 0 && delta > 0) {
				show_or_hide();
			}
		} else {
			if (isShow && window.pageYOffset == 0) {
				to_top.hide();
				isShow = false;
				clickYOffset = 0;
			}
		}
	});

	$(window).scroll(show_or_hide);

	function show_or_hide() {
		if (click) {
			return;
		}

		if (window.pageYOffset > 300) {
			if (!isShow) {
				to_top.show();
				btn.html(arrowUp);
				isShow = true;
			}
		} else {
			if (isShow) {
				to_top.hide();
				btn.html(arrowDown);
				isShow = false;
			}
		}
	}

	show_or_hide();
});
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
		ids = 'docid=' + dbID;
	} else {
		ids = nbApp.getSelectedDocumentsAsParam();
	}

	if (ids.length === 0) {
		nb.utils.notify({
			message : nb.getText('no_selected_documents', 'Не выбран документ для удаления'),
			type : 'error'
		}).show().remove(2000);
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

;
nbApp.inViewEdit = function() {
	var _ed = nbApp.inViewEdit;
	var $addNewNode = $('[data-action=add_new]');
	var $layoutContent = $('.layout_content');

	_ed.contentInit = function() {
		$('[data-action=save]').on('click', function(e) {
			e.preventDefault();
		})

		$('[data-action=close]').on('click', function(e) {
			$addNewNode.removeClass('hidden');
			window.history.back(-1);
			e.preventDefault();
		});

		$('.entry-link').on('click', function(e) {
			_ed.edit(this.href);
			e.preventDefault();
		});
	};

	_ed.edit = function(url) {
		//
		if (url != window.location) {
			window.history.pushState(null, null, url);
		}
		//
		$addNewNode.addClass('hidden');

		$('#main-load').show();
		$.get(url).then(function(r) {
			$layoutContent.html(r);
			$('#main-load').hide();
			_ed.contentInit();
		});
	};

	$addNewNode.on('click', function(e) {
		_ed.edit(this.href);
		e.preventDefault();
	});

	$('a', '.side').on('click', function(e) {
		_ed.edit(this.href);
		e.preventDefault();
	});

	$(window).bind('popstate', function(e) {
		_ed.edit(location.href);
		e.preventDefault();
	});
};

$(function() {
	// nbApp.inViewEdit();
});

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

nbApp.xhrGetCostCenterJson = function() {
	return nb.ajax({
		method : "GET",
		datatype : 'JSON',
		url : "Provider?type=page&id=costcenter-json"
	});
};
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
