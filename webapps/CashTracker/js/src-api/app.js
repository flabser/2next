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
